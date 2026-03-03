package inf112.zoombiegeddon.model.entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import inf112.zoombiegeddon.controller.PlayerController;
import inf112.zoombiegeddon.model.animations.AnimationHandler;
import inf112.zoombiegeddon.view.levels.AbstractLevel;

/**
 * Represents the player character in the game.
 * This class handles player-specific logic including physics interaction,
 * animations based on movement states, and user-triggered actions like jumping.
 */
public class Player extends Entity implements IDamageable {

    private static final float JUMP_IMPULSE = 10.0f;
    private static final float MOVE_IMPULSE = 0.7f;
    private static final float VELOCITY_THRESHOLD = 0.1f;

    private final PlayerController controller;
    private final AnimationHandler animationHandler;
    private boolean isRunning = false;

    private int HP;
    private final int maxHP;
    private float hitTimer;
    private final float InvulnerabilityCooldown = 1.0f;
    private boolean isDead = false;
    private final float bodyW;
    private final float bodyH;
    private final Rectangle bounds = new Rectangle();

    /**
     * Constructs a new Player instance.
     * Initializes the physics body, sets the user data for collision detection,
     * and calculates dimensions based on the idle animation frames.
     *
     * @param world            The Box2D world where the player's body resides.
     * @param spawnPos         The initial spawn coordinates.
     * @param animationHandler The handler containing all player-related animations.
     */
    public Player(World world, Vector2 spawnPos, AnimationHandler animationHandler, int Hp) {

        float spriteDim = 32 * AbstractLevel.UNIT_SCALE;
        float actualW = 11 * AbstractLevel.UNIT_SCALE;
        float actualH = 20 * AbstractLevel.UNIT_SCALE;

        this.bodyW = actualW;
        this.bodyH = actualH;


        //player body is 11px X 20 px
        super(new PhysicsComponent(world, spawnPos, actualW, actualH, "player"));

        this.physics.getBody().setUserData(this);
        this.animationHandler = animationHandler;
        this.HP = Hp;
        this.maxHP = Hp;

        setSize(spriteDim, spriteDim);

        this.controller = new PlayerController(this,null);
    }

    /**
     * Updates the player's logic.
     * synchronizes the sprite's position with the physics body and updates the
     * animation frame based on the current state and elapsed time.
     *
     * @param dt Time in seconds since the last update.
     */
    @Override
    public void update(float dt) {
        //System.out.println("Player: isGrounded: " + isGrounded() + " isRunning: " + isRunning + " isFacingRight: " + facingRight);
        controller.update(dt);

        updateAnimationState(dt, VELOCITY_THRESHOLD);

        if (hitTimer > 0) hitTimer -= dt;

        setRegion(animationHandler.getFrame(currentState, stateTime, facingRight));
        super.update(dt);
    }

    /**
     * Updates the entity's animation state based on current velocity and grounded status.
     * Handles switching between IDLE, JUMPING, FALLING, and moving states, as well as
     * tracking state time and determining which direction the sprite should face.
     *
     * @param dt                The time in seconds since the last update.
     * @param velocityThreshold The minimum velocity required to trigger a state change.
     */
    @Override
    protected void updateAnimationState(float dt, float velocityThreshold) {
        stateTime += dt;
        Vector2 velocity = physics.getVelocity();

        // Determine orientation
        if (velocity.x > velocityThreshold) facingRight = true;
        else if (velocity.x < -velocityThreshold) facingRight = false;

        // Determine state
        if (!isGrounded()) {
            currentState = (velocity.y > velocityThreshold) ? EntityState.JUMPING : EntityState.FALLING;
        } else if (Math.abs(velocity.x) > velocityThreshold) { // Use velocity instead of controller-buttons
            currentState = determineMovingState();
        } else {
            currentState = EntityState.IDLE;
        }

        // Reset state timer if the state has changed
        if (previousState != currentState) {
            stateTime = 0;
            previousState = currentState;
        }
    }

    /**
     * Determines whether the player is in a WALKING or RUNNING state
     * when horizontal movement is detected.
     *
     * @return The appropriate EntityState based on the isRunning flag.
     */
    @Override
    protected EntityState determineMovingState() {
        return isRunning ? EntityState.RUNNING : EntityState.WALKING;
    }

    /**
     * Applies an upward impulse to the player's physics body if they are on the ground.
     */
    public void jump() {
        if (isGrounded()) {
            physics.applyImpulse(new Vector2(0, JUMP_IMPULSE));
        }
    }

    /**
     * Applies horizontal force to the player's physics body.
     *
     * @param left  True if the player should move left.
     * @param right True if the player should move right.
     */
    public void applyHorizontalImpulse(boolean left, boolean right) {
        if (left) physics.applyImpulse(new Vector2(-MOVE_IMPULSE, 0));
        if (right) physics.applyImpulse(new Vector2(MOVE_IMPULSE, 0));
    }

    /**
     * Sets the running state of the player.
     *
     * @param running True to enable running speed/animations, false for walking.
     */
    public void setRunning(boolean running) {
        this.isRunning = running;
    }

    /**
     * Checks if the player is currently in the running state.
     *
     * @return True if running, false otherwise.
     */
    public boolean isRunning() {
        return isRunning;
    }


    @Override
    public void takeDamage(int amount) {
        if (hitTimer <= 0) {
            this.HP -= amount;
            hitTimer = InvulnerabilityCooldown;
        }
        if (this.HP <= 0) {
            isDead = true;
            System.out.println("player dead");
        }
    }

    @Override
    public int getHealth() {
        return HP;
    }

    @Override
    public int getMaxHealth() {
        return maxHP;
    }

    @Override
    public boolean isDead() {
        return isDead;
    }

    @Override
    public void heal(int amount) {
        this.HP += (int) amount;
    }

    //PLACEHOLDER CODE FOR TESTING
    public Bullet createBullet(World world) {
        // Spawn bullet slightly in front of the player based on facingRight
        float offset = facingRight ? 0.5f : -0.5f;
        Vector2 spawnPos = new Vector2(physics.getPosition().x + offset, physics.getPosition().y);
        return new Bullet(world, spawnPos, facingRight);
    }

    public Rectangle getBounds() {
        Vector2 p = physics.getPosition();
        bounds.set(p.x - bodyW / 2f, p.y - bodyH / 2f, bodyW, bodyH);
        return bounds;
    }
}
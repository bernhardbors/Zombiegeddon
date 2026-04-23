package inf112.zoombiegeddon.model.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import inf112.zoombiegeddon.controller.PlayerController;
import inf112.zoombiegeddon.events.GameEvent;
import inf112.zoombiegeddon.events.GameEventBus;
import inf112.zoombiegeddon.model.animations.AnimationHandler;
import inf112.zoombiegeddon.model.entity.enemies.Enemy;
import inf112.zoombiegeddon.model.entity.enemies.IEnemy;
import inf112.zoombiegeddon.model.levels.LevelModel;

/**
 * Represents the player character in the game.
 * This class handles player-specific logic including physics interaction,
 * animations based on movement states, and user-triggered actions like jumping.
 */
public class Player extends Entity implements IPlayer {

    private static final float JUMP_IMPULSE = 23.0f;
    private static final float MOVE_IMPULSE = 1.0f;
    private static final float VELOCITY_THRESHOLD = 0.1f;

    private float coyoteTimer = 0f;
    private final float COYOTE_TIME_MAX = 0.15f; // 150ms window

    private float jumpBufferTimer = 0f;
    private final float JUMP_BUFFER_MAX = 0.10f;

    private final PlayerController controller;
    private final AnimationHandler animationHandler;
    private boolean isRunning = false;
    private boolean wasMovingHorizontally = false;
    private boolean wasRunningWhileMoving = false;

    private int HP;
    private final int maxHP;
    private float hitTimer;
    private final float InvulnerabilityCooldown = 1.0f;
    private boolean isDead = false;
    private boolean inGoal =  false;
    private final Rectangle bounds = new Rectangle();

    private float spriteDim;

    private float speedMultiplier = 1.0f;
    private float speedBoostRemaining = 0f;
    private float pickupVisualEffectRemaining = 0f;
    private final Color pickupVisualEffectColor = new Color(Color.YELLOW);

    /**
     * Constructs a new player.
     *
     * @param world The Box2D world where the player exists.
     * @param spawnPos The initial spawn position of the player.
     * @param animationHandler The animation handler used for player animations.
     * @param Hp The starting and maximum health of the player.
     */
    public Player(World world, Vector2 spawnPos, AnimationHandler animationHandler, int Hp) {

        this.spriteDim = 32 * LevelModel.UNIT_SCALE;
        float actualW = 11 * LevelModel.UNIT_SCALE;
        float actualH = 20 * LevelModel.UNIT_SCALE;

        super(new PhysicsComponent(world, spawnPos, actualW, actualH, "player"));

        this.physics.getBody().setUserData(this);
        this.animationHandler = animationHandler;
        this.HP = Hp;
        this.maxHP = Hp;

        this.width = actualW;
        this.height = actualH;

        this.controller = new PlayerController(this);
    }

    /**
     * Updates the player's logic.
     * synchronizes the sprite's position with the physics body and updates the
     * animation frame based on the current state and elapsed time.
     *
     * @param dt Time in seconds since the last update.
     */
    public void update(float dt) {
        if (isDead) {
            if (hitTimer > 0) hitTimer -= dt;
            return;
        }
        controller.update(dt);
        postHorizontalMoveStateEvents();

        updateAnimationState(dt, VELOCITY_THRESHOLD);
        if (hitTimer > 0) hitTimer -= dt;

        if (speedBoostRemaining > 0f) {
            speedBoostRemaining -= dt;
            if (speedBoostRemaining <= 0f) {
                speedBoostRemaining = 0f;
                speedMultiplier = 1.0f;
            }
        }

        if (pickupVisualEffectRemaining > 0f) {
            pickupVisualEffectRemaining = Math.max(0f, pickupVisualEffectRemaining - dt);
        }

        if (isGrounded()) {
            coyoteTimer = COYOTE_TIME_MAX;
        } else {
            coyoteTimer -= dt;
        }

        if (jumpBufferTimer > 0) {
            jumpBufferTimer -= dt;
        }

        if (jumpBufferTimer > 0 && coyoteTimer > 0) {
            executeJump();
        }

        Vector2 vel = physics.getVelocity();

        if (vel.y > 0.1f) {
            if (!controller.isJumpPressed()) {
                physics.getBody().setGravityScale(5.0f);
            } else {
                physics.getBody().setGravityScale(1.5f);
            }
        } else {
            physics.getBody().setGravityScale(2.0f);
        }
 
       }

    public void applySpeedBoost(float multiplier, float durationSeconds) {
        speedMultiplier = Math.max(1.0f, multiplier);
        speedBoostRemaining = Math.max(0f, durationSeconds);
    }

    public float getActiveSpeedMultiplier() {
        return speedMultiplier;
    }

    public void triggerPickupVisualEffect(float durationSeconds, Color effectColor) {
        pickupVisualEffectRemaining = Math.max(0f, durationSeconds);
        if (effectColor != null) {
            pickupVisualEffectColor.set(effectColor);
        }
    }

    public boolean hasPickupVisualEffect() {
        return pickupVisualEffectRemaining > 0f;
    }

    public float getPickupFxIntensity() {
        if (pickupVisualEffectRemaining <= 0f) {
            return 0f;
        }
        return Math.min(1f, pickupVisualEffectRemaining);
    }

    public float getPickupFxColorR() {
        return pickupVisualEffectColor.r;
    }

    public float getPickupFxColorG() {
        return pickupVisualEffectColor.g;
    }

    public float getPickupFxColorB() {
        return pickupVisualEffectColor.b;
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


        if (velocity.x > velocityThreshold) facingRight = true;
        else if (velocity.x < -velocityThreshold) facingRight = false;


        if (!isGrounded()) {
            currentState = (velocity.y > velocityThreshold) ? EntityState.JUMPING : EntityState.FALLING;
        } else if (Math.abs(velocity.x) > velocityThreshold) { // Use velocity instead of controller-buttons
            currentState = determineMovingState();
        } else {
            currentState = EntityState.IDLE;
        }


        if (previousState != currentState) {
            stateTime = 0;
            previousState = currentState;
        }
    }


    /**
     * Determines whether the player should use the walking or running animation state.
     *
     * @return {@link EntityState#RUNNING} if the player is running, otherwise {@link EntityState#WALKING}.
     */
    @Override
    public EntityState determineMovingState() {
        return isRunning ? EntityState.RUNNING : EntityState.WALKING;
    }

    /**
     * Makes the player jump if grounded and posts a jump event.
     */
    public void jump() {
        jumpBufferTimer = JUMP_BUFFER_MAX;
    }

    private void executeJump() {
        physics.applyImpulse(new Vector2(0, JUMP_IMPULSE));

        jumpBufferTimer = 0;
        coyoteTimer = 0;

        GameEventBus.post(GameEvent.PLAYER_JUMP);
    }

    /**
     * Posts movement-related events when the player's horizontal movement state changes.
     */
    private void postHorizontalMoveStateEvents() {
        Vector2 vel = physics.getVelocity();
        boolean movingHorizontally = isGrounded() && Math.abs(vel.x) > VELOCITY_THRESHOLD;
        boolean runningNow = movingHorizontally && isRunning;

        if (!wasMovingHorizontally && movingHorizontally) {
            GameEventBus.post(GameEvent.PLAYER_MOVE_START);

            if (runningNow) {
                GameEventBus.post(GameEvent.PLAYER_RUN_START);
            }
        } else if (wasMovingHorizontally && !movingHorizontally) {
            if (wasRunningWhileMoving) {
                GameEventBus.post(GameEvent.PLAYER_RUN_STOP);
            }
            GameEventBus.post(GameEvent.PLAYER_MOVE_STOP);
        } else if (movingHorizontally) {
            if (!wasRunningWhileMoving && runningNow) {
                GameEventBus.post(GameEvent.PLAYER_RUN_START);
            } else if (wasRunningWhileMoving && !runningNow) {
                GameEventBus.post(GameEvent.PLAYER_RUN_STOP);
            }
        }

        wasMovingHorizontally = movingHorizontally;
        wasRunningWhileMoving = runningNow;
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
        if (isDead) return;

        if (hitTimer <= 0) {
            this.HP -= amount;
            hitTimer = InvulnerabilityCooldown;
        }

        if (this.HP <= 0) {
            this.HP = 0;
            isDead = true;
            GameEventBus.post(GameEvent.PLAYER_DEATH);
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
        this.HP = Math.min(this.HP + amount, maxHP);
    }

    /**
     * Returns whether the player has reached the goal.
     *
     * @return True if the player is in the goal area, false otherwise.
     */
    public boolean inGoal(){
        return inGoal;
    }

    /**
     * Sets whether the player is in the goal area.
     *
     * @param bool True if the player is in the goal, false otherwise.
     */
    public void setInGoal(boolean bool){
        if (!this.inGoal && bool) {
            GameEventBus.post(GameEvent.LEVEL_FINISHED);
        }
        this.inGoal = bool;
    }

    /**
     * Returns the player's bounding rectangle based on its current physics position.
     *
     * @return The player's bounds.
     */
    public Rectangle getBounds() {
        Vector2 p = physics.getPosition();
        bounds.set(p.x - width / 2f, p.y - height / 2f, width, height);
        return bounds;
    }

    /**
     * Handles interaction between the player and an enemy.
     *
     * @param enemy The enemy involved in the collision.
     * @param hitWithFeet True if the player hit the enemy from above.
     */
    public void hitEnemy(IEnemy enemy, boolean hitWithFeet) {
        if (hitWithFeet) {
            enemy.takeDamage(1);

            Vector2 vel = this.physics.getVelocity();
            this.physics.getBody().setLinearVelocity(vel.x, 0);
            executeJump();
        } else {
            this.takeDamage(1);
        }
    }


    @Override
    public void setPosition(float x, float y) {
        physics.getBody().setTransform(x,y,physics.getBody().getAngle());
        physics.getBody().setLinearVelocity(0,0);
    }

    @Override
    public Vector2 getPosition() {
        return physics.getPosition();
    }


    @Override
    public PhysicsComponent getPhysics() {
        return physics;
    }


    @Override
    public boolean isGrounded() {
        return isGrounded;
    }

    @Override
    public float getWidth() {
        return width;
    }


    @Override
    public float getHeight() {
        return height;
    }


    @Override
    public float getScaledSpriteDimensions() {
        return spriteDim;
    }

    @Override
    public AnimationHandler getAnimationHandler() {
        return animationHandler;
    }
}
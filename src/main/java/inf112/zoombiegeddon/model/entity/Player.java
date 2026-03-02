package inf112.zoombiegeddon.model.entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import inf112.zoombiegeddon.model.animations.AnimationHandler;

/**
 * Represents the player character in the game.
 * This class handles player-specific logic including physics interaction,
 * animations based on movement states, and user-triggered actions like jumping.
 */
public class Player extends Entity {

    /** Pixels Per Meter - used to scale between physics world and screen pixels. */
    public static final float PPM = 32.0f;

    private static final float JUMP_IMPULSE = 6.0f;
    private static final float MOVE_IMPULSE = 0.7f;
    private static final float VELOCITY_THRESHOLD = 0.1f;

    private final AnimationHandler animationHandler;
    private boolean isRunning = false;

    /**
     * Constructs a new Player instance.
     * Initializes the physics body, sets the user data for collision detection,
     * and calculates dimensions based on the idle animation frames.
     *
     * @param world            The Box2D world where the player's body resides.
     * @param spawnPos         The initial spawn coordinates.
     * @param animationHandler The handler containing all player-related animations.
     */
    public Player(World world, Vector2 spawnPos, AnimationHandler animationHandler) {
        super(new PhysicsComponent(world, spawnPos,
                animationHandler.getAnimations().get(EntityState.IDLE).getKeyFrame(0).getRegionWidth() / PPM,
                animationHandler.getAnimations().get(EntityState.IDLE).getKeyFrame(0).getRegionHeight() / PPM,
                null));

        this.animationHandler = animationHandler;
        this.physics.getBody().setUserData(this);

        float width = animationHandler.getFrame(EntityState.IDLE, 0, true).getRegionWidth() / PPM;
        float height = animationHandler.getFrame(EntityState.IDLE, 0, true).getRegionHeight() / PPM;
        setSize(width, height);
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
        updateAnimationState(dt, VELOCITY_THRESHOLD);

        setRegion(animationHandler.getFrame(currentState, stateTime, facingRight));
        setPosition(
                physics.getPosition().x - getWidth() / 2,
                physics.getPosition().y - getHeight() / 2
        );
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
        if (!isGrounded) {
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
        if (isGrounded) {
            physics.applyImpulse(new Vector2(0, JUMP_IMPULSE));
            isGrounded = false;
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
}
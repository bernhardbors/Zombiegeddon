package inf112.zoombiegeddon.model.entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import inf112.zoombiegeddon.model.animations.AnimationHandler;

/**
 * Represents an enemy entity in the game world.
 * This class handles automated movement logic (AI), physics interactions,
 * and animation state management based on velocity and direction.
 */
public class Enemy extends Entity {

    /** Pixels Per Meter - used to scale between physics world and screen pixels. */
    public static final float PPM = 32.0f;

    private static final float MOVE_SPEED = 2.0f;
    private static final float VELOCITY_THRESHOLD = 0.1f;

    private final AnimationHandler animationHandler;
    private boolean movingRight = true;

    /**
     * Constructs a new Enemy instance.
     * Initializes the physics body and sets dimensions based on the idle animation.
     *
     * @param world            The Box2D world where the enemy's body resides.
     * @param spawnPos         The initial spawn coordinates.
     * @param animationHandler The handler containing all enemy-related animations.
     */
    public Enemy(World world, Vector2 spawnPos, AnimationHandler animationHandler) {
        // Initialize superclass with PhysicsComponent scaled by PPM
        super(new PhysicsComponent(world, spawnPos,
                animationHandler.getFrame(EntityState.IDLE, 0, true).getRegionWidth() / PPM,
                animationHandler.getFrame(EntityState.IDLE, 0, true).getRegionHeight() / PPM,
                null));

        this.animationHandler = animationHandler;
        this.physics.getBody().setUserData(this);

        // Set sprite size based on the animation frame dimensions
        float width = animationHandler.getFrame(EntityState.IDLE, 0, true).getRegionWidth() / PPM;
        float height = animationHandler.getFrame(EntityState.IDLE, 0, true).getRegionHeight() / PPM;
        setSize(width, height);
    }

    /**
     * Updates the enemy's logic.
     * Applies automated movement, updates the animation state, and synchronizes
     * the sprite position with the physics body.
     *
     * @param dt Time in seconds since the last update.
     */
    @Override
    public void update(float dt) {

        float currentVelocityX = physics.getVelocity().x;

        if (Math.abs(currentVelocityX) < 0.1f) {
            reverseDirection();
            applyPatrolMovement();
        }

        updateAnimationState(dt, VELOCITY_THRESHOLD);

        setRegion(animationHandler.getFrame(currentState, stateTime, facingRight));
        setPosition(
                physics.getPosition().x - getWidth() / 2,
                physics.getPosition().y - getHeight() / 2
        );
    }

    /**
     * Applies horizontal velocity to the physics body to facilitate patrolling.
     */
    private void applyPatrolMovement() {
        float horizontalVelocity = movingRight ? MOVE_SPEED : -MOVE_SPEED;
        physics.getBody().setLinearVelocity(horizontalVelocity, physics.getVelocity().y);
    }

    /**
     * Simple implementation returning WALKING state for standard enemy movement.
     *
     * @return The WALKING EntityState.
     */
    @Override
    protected EntityState determineMovingState() {
        return EntityState.WALKING;
    }

    /**
     * Reverses the movement direction of the enemy.
     * Typically called when the enemy hits a wall or reaches a platform edge.
     */
    public void reverseDirection() {
        movingRight = !movingRight;
    }
}
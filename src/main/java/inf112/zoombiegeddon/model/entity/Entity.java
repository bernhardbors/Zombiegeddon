package inf112.zoombiegeddon.model.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;



/**
 * Base class for all game entities that have physical representation and animations.
 * Extends the LibGDX {@link Sprite} class to handle visual rendering while
 * maintaining a reference to a {@link PhysicsComponent} for movement and collision.
 */
public abstract class Entity implements IEntity {
    protected PhysicsComponent physics;
    protected EntityState currentState = EntityState.IDLE;
    protected EntityState previousState = EntityState.IDLE;

    protected float stateTime = 0f;
    protected boolean facingRight = true;
    protected boolean isGrounded = false;

    private int contactPoints = 0;

    protected float width;
    protected float height;

    /**
     * Constructs an Entity with a specific physics component.
     *
     * @param physics The physics component managing this entity's body.
     */
    public Entity(PhysicsComponent physics) {
        this.physics = physics;
    }

    public EntityState getCurrentState() { return currentState; }
    public float getStateTime() { return stateTime; }
    public boolean isFacingRight() { return facingRight; }


    /**
     * Determines which state the entity should be in when it is moving on the ground.
     * This allows subclasses to choose between states like WALKING or RUNNING.
     *
     * @return The determined EntityState for horizontal movement.
     */
    public abstract EntityState determineMovingState();

    /**
     * Updates the entity's animation state based on current velocity and grounded status.
     * Handles switching between IDLE, JUMPING, FALLING, and moving states, as well as
     * tracking state time and determining which direction the sprite should face.
     *
     * @param dt                The time in seconds since the last update.
     * @param velocityThreshold The minimum velocity required to trigger a state change.
     */
    protected void updateAnimationState(float dt, float velocityThreshold) {
        stateTime += dt;
        Vector2 velocity = physics.getVelocity();

        // Determine orientation
        if (velocity.x > velocityThreshold) facingRight = true;
        else if (velocity.x < -velocityThreshold) facingRight = false;

        // Determine state
        if (!isGrounded) {
            currentState = (velocity.y > velocityThreshold) ? EntityState.JUMPING : EntityState.FALLING;
        } else if (Math.abs(velocity.x) > 0.1f ) {
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
     * Sets whether the entity is currently touching the ground.
     * Usually called by a contact listener.
     *
     * @param grounded True if the entity is on the ground, false otherwise.
     */
    public void setGrounded(boolean grounded) {
        if (grounded) {
            contactPoints++;
        } else {
            contactPoints--;
        }
        if (contactPoints < 0) contactPoints = 0;

        isGrounded = (contactPoints > 0);

        // DEBUG PRINT
        System.out.println("Entity " + this.getClass().getSimpleName() + " contact points: " + contactPoints + " isGrounded: " + isGrounded);
    }

}
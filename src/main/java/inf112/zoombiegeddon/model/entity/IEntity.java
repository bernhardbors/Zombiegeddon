package inf112.zoombiegeddon.model.entity;

import com.badlogic.gdx.math.Vector2;

/**
 * Defines the common behavior for game entities.
 * Provides access to position, physics, size, grounded state, and animation state.
 */
public interface IEntity {

    /**
     * Sets the entity position
     *
     * @param x pos on the x-axis
     * @param y pos on the y-axis
     */
    void setPosition(float x, float y);

    /**
     * Gets the current position of the entity from the physics component.
     *
     * @return A Vector2 representing the world coordinates.
     */
    Vector2 getPosition();

    /**
     * Provides access to the entity's physics component.
     *
     * @return The PhysicsComponent instance.
     */
    PhysicsComponent getPhysics();

    /**
     * Sets whether the entity is currently touching the ground.
     * Usually called by a contact listener.
     *
     * @param grounded True if the entity is on the ground, false otherwise.
     */
    void setGrounded(boolean grounded);

    /**
     * Checks whether the entity is currently on the ground.
     *
     * @return true if the entity is on the ground, false otherwise
     */
    boolean isGrounded();

    /**
     * Returns the width of the entity.
     *
     * @return the entity width
     */
    float getWidth();

    /**
     * Returns the height of the entity.
     *
     * @return the entity height
     */
    float getHeight();

    /**
     * Returns the movement speed of the entity.
     *
     * @return the entity speed
     */
    float getSpeed();

    /**
     * Returns the current animation state of the entity.
     *
     * @return the current entity state
     */
    EntityState getCurrentState();

    /**
     * Returns the time spent in the current state.
     *
     * @return time spent in the current state
     */
    float getStateTime();

    /**
     * Returns whether the entity is facing right.
     *
     * @return true if the entity is facing right, false otherwise
     */
    boolean isFacingRight();

    /**
     * Updates the entity logic.
     *
     * @param dt Time in seconds since the last update.
     */
    void update(float dt);
}

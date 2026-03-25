package inf112.zoombiegeddon.model.entity;

import com.badlogic.gdx.math.Vector2;
import inf112.zoombiegeddon.model.animations.AnimationHandler;

/**
 * Defines the common behavior for game entities.
 * Provides access to position, physics, size, grounded state, and animations.
 */
public interface IEntity {

    /**
     * Sets the entity position
     *
     * @param x pos on the x-axis
     * @param y pos on the y-axis
     */
    public void setPosition(float x, float y);

    /**
     * Gets the current position of the entity from the physics component.
     *
     * @return A Vector2 representing the world coordinates.
     */
    public Vector2 getPosition();

    /**
     * Provides access to the entity's physics component.
     *
     * @return The PhysicsComponent instance.
     */
    public PhysicsComponent getPhysics();

    /**
     * Sets whether the entity is currently touching the ground.
     * Usually called by a contact listener.
     *
     * @param grounded True if the entity is on the ground, false otherwise.
     */
    public void setGrounded(boolean grounded);

    /**
     *
     * @return boolean true if on ground false if not.
     */
    public boolean isGrounded();

    /**
     * Sets the entity Width
     */
    public void setWidth();

    /**
     * Sets the entity Height
     */
    public void setHeight();

    /**
     * @return entity Width
     */
    public float getWidth();

    /**
     * @return entity Height
     */
    public float getHeight();

    /**
     * Returns the scaled sprite dimension of the entity.
     *
     * @return The scaled sprite dimension.
     */
    public float getScaledSpriteDimensions();

    /**
     * Returns the animation handler used by the entity.
     *
     * @return The animation handler.
     */
    public AnimationHandler getAnimationHandler();
}

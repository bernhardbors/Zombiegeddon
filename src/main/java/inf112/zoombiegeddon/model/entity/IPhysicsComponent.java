package inf112.zoombiegeddon.model.entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Defines the physics contract for an entity's physics component.
 * Consumers should depend on this interface rather than the concrete
 * PhysicsComponent class.
 */
public interface IPhysicsComponent {

    /**
     * @return the current world position of the entity
     */
    Vector2 getPosition();

    /**
     * @return the current linear velocity of the entity
     */
    Vector2 getVelocity();

    /**
     * Sets the linear velocity of the body directly.
     *
     * @param velocity the new velocity vector
     */
    void setLinearVelocity(Vector2 velocity);

    /**
     * Returns the underlying Box2D body.
     * Intended for use by physics/destruction systems only.
     *
     * @return the raw Box2D body
     */
    Body getBody();
}

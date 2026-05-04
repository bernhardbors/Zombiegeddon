package inf112.zoombiegeddon.model.world.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import inf112.zoombiegeddon.model.entity.IPhysicsComponent;
import inf112.zoombiegeddon.model.entity.pickups.IPickup;

import java.util.List;
import java.util.function.Function;

/**
 * Manages the Box2D physics world and pickup lifecycle.
 */
public interface IWorldPhysics {

    /**
     * Registers a factory for creating pickups of the given type.
     *
     * @param rewardType the pickup type identifier (case-insensitive)
     * @param factory    a function that creates a pickup at a given world position
     */
    void registerPickupFactory(String rewardType, Function<Vector2, IPickup> factory);

    /**
     * Spawns a pickup of the given type at the given position.
     * If the world is currently locked the spawn is queued until the step completes.
     *
     * @param rewardType    the pickup type identifier; must not be {@code null}
     * @param spawnPosition the world position; must not be {@code null}
     * @return {@code true} if the spawn was created or queued
     */
    boolean spawnPickup(String rewardType, Vector2 spawnPosition);

    /** @return an unmodifiable view of all currently active pickups */
    List<IPickup> getPickups();

    /** @return the underlying Box2D {@link World} instance */
    World getWorld();

    /**
     * Advances the physics simulation using a fixed-timestep accumulator.
     *
     * @param delta time in seconds since the last frame
     */
    void update(float delta);

    /** Releases all native resources used by the Box2D world. */
    void dispose();

    /**
     * Removes the physics body belonging to the given component from the world.
     *
     * @param component the physics component whose body should be destroyed
     */
    void destroyBody(IPhysicsComponent component);
}

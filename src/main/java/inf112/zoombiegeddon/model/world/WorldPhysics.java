package inf112.zoombiegeddon.model.world;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Manages the Box2D physics world and handles the simulation step logic.
 * <p>
 * This class implements a fixed timestep to ensure deterministic physics
 * simulation regardless of the frame rate.
 * </p>
 *
 * @version 1.0
 */
public class WorldPhysics {

    private final World world;
    private float accumulatedTime = 0;

    /**
     * Initializes a new physics world with the specified gravity.
     * * @param gravity a {@link Vector2} representing the gravity vector (e.g., 0, -9.81f).
     */
    public WorldPhysics(Vector2 gravity) {
        this.world = new World(gravity, true);
    }

    /**
     * @return the underlying Box2D {@link World} instance.
     */
    public World getWorld() {
        return world;
    }

    /**
     * Advances the physics simulation.
     * <p>
     * Uses an accumulator pattern to ensure that the physics world steps at
     * a constant rate (60Hz), preventing "speed up" or "slow down" issues
     * caused by fluctuating frame rates.
     * </p>
     * * @param delta the time in seconds since the last render frame.
     *
     * @see <a href="https://gafferongames.com/post/fix_your_timestep/">Fix Your Timestep!</a>
     */
    public void update(float delta) {
        float timeStep = 1 / 60f;
        accumulatedTime += delta;
        while (accumulatedTime >= timeStep) {
            world.step(timeStep, 6, 2);
            accumulatedTime -= timeStep;
        }
    }

    /**
     * Releases all native resources used by the Box2D world.
     * Should be called when the game screen or application is closed.
     */
    public void dispose() {
        world.dispose();
    }
}


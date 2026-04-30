package inf112.zoombiegeddon.model.world.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import inf112.zoombiegeddon.model.entity.pickups.IPickup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Manages the Box2D physics world and handles the simulation step logic.
 * <p>
 * This class implements a fixed timestep to ensure deterministic physics
 * simulation regardless of the frame rate.
 * </p>
 */
public class WorldPhysics {

    private record PendingPickupSpawn(String rewardType, Vector2 spawnPosition) {
    }

    private final World world;
    private final WorldContactListener contactListener;
    private final List<IPickup> pickups;
    private final Map<String, Function<Vector2, IPickup>> pickupFactories;
    private final List<PendingPickupSpawn> pendingPickupSpawns;
    private float accumulatedTime = 0;

    private static final Logger logger = LoggerFactory.getLogger(WorldPhysics.class);

    /**
     * Initializes a new physics world with the specified gravity.
     * @param gravity a {@link Vector2} representing the gravity vector (e.g., 0, -9.81f).
     */
    public WorldPhysics(Vector2 gravity) {
        this.world = new World(gravity, true);
        this.pickups = new ArrayList<>();
        this.pickupFactories = new HashMap<>();
        this.pendingPickupSpawns = new ArrayList<>();

        this.contactListener = new WorldContactListener(this);
        this.world.setContactListener(contactListener);
    }

    /**
     * Registers a factory function for creating pickups of the given type.
     * Does nothing if either argument is {@code null}.
     *
     * @param rewardType the pickup type identifier (case-insensitive)
     * @param factory    a function that creates a pickup at a given position
     */
    public void registerPickupFactory(String rewardType, Function<Vector2, IPickup> factory) {
        if (rewardType == null || factory == null) {
            return;
        }
        pickupFactories.put(rewardType.trim().toLowerCase(), factory);
    }

    /**
     * Spawns a pickup of the given type at the given position.
     * If the physics world is currently locked (mid-step), the spawn is queued
     * and executed at the end of the current step.
     *
     * @param rewardType    the pickup type identifier; must not be {@code null}
     * @param spawnPosition the world position to spawn at; must not be {@code null}
     * @return true if the spawn was created or queued, false if a required argument was null
     */
    public boolean spawnPickup(String rewardType, Vector2 spawnPosition) {
        if (rewardType == null || spawnPosition == null) {
            return false;
        }

        if (world.isLocked()) {
            pendingPickupSpawns.add(new PendingPickupSpawn(rewardType, new Vector2(spawnPosition)));
            return true;
        }

        return createPickupNow(rewardType, spawnPosition);
    }

    private boolean createPickupNow(String rewardType, Vector2 spawnPosition) {
        if (rewardType == null || spawnPosition == null) {
            return false;
        }

        Function<Vector2, IPickup> factory = pickupFactories.get(rewardType.trim().toLowerCase());
        if (factory == null) {
            logger.error("No pickup factory registered for reward type: {}", rewardType);
            return false;
        }

        IPickup pickup = factory.apply(new Vector2(spawnPosition));
        if (pickup == null) {
            logger.error("No pickup registered for reward type: {}", rewardType);
            return false;
        }

        pickups.add(pickup);
        return true;
    }

    private void processPendingPickupSpawns() {
        if (pendingPickupSpawns.isEmpty() || world.isLocked()) {
            return;
        }

        for (PendingPickupSpawn pendingSpawn : pendingPickupSpawns) {
            if (!createPickupNow(pendingSpawn.rewardType(), pendingSpawn.spawnPosition())) {
                logger.error("Failed to spawn queued pickup for reward type: {} ", pendingSpawn.rewardType());
            }
        }
        pendingPickupSpawns.clear();
    }

    public List<IPickup> getPickups() {
        return Collections.unmodifiableList(pickups);
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
            contactListener.processQueuedRemovals(world);
            processPendingPickupSpawns();
            pickups.removeIf(IPickup::isCollected);
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


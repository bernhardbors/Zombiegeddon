package inf112.zoombiegeddon.model.world.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import inf112.zoombiegeddon.model.entity.pickups.HealPickup;
import inf112.zoombiegeddon.model.entity.pickups.IPickup;
import inf112.zoombiegeddon.model.entity.pickups.SpeedBoostPickup;

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
 *
 * @version 1.0
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

    /**
     * Initializes a new physics world with the specified gravity.
     * * @param gravity a {@link Vector2} representing the gravity vector (e.g., 0, -9.81f).
     */
    public WorldPhysics(Vector2 gravity) {
        this.world = new World(gravity, true);
        this.pickups = new ArrayList<>();
        this.pickupFactories = new HashMap<>();
        this.pendingPickupSpawns = new ArrayList<>();
        registerDefaultPickupFactories();

        this.contactListener = new WorldContactListener(this);
        this.world.setContactListener(contactListener);
    }

    private void registerDefaultPickupFactories() {
        registerPickupFactory("speed", spawn -> new SpeedBoostPickup(world, spawn));
        registerPickupFactory("heal", spawn -> new HealPickup(world, spawn));
    }

    public void registerPickupFactory(String rewardType, Function<Vector2, IPickup> factory) {
        if (rewardType == null || factory == null) {
            return;
        }
        pickupFactories.put(rewardType.trim().toLowerCase(), factory);
    }

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
            System.err.println("No pickup factory registered for reward type: " + rewardType);
            return false;
        }

        IPickup pickup = factory.apply(new Vector2(spawnPosition));
        if (pickup == null) {
            System.err.println("Pickup factory returned null for reward type: " + rewardType);
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
                System.err.println("Failed to spawn queued pickup for reward type: " + pendingSpawn.rewardType());
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


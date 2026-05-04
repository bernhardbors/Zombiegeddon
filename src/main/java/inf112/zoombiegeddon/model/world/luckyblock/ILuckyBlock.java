package inf112.zoombiegeddon.model.world.luckyblock;

import com.badlogic.gdx.physics.box2d.Body;
import inf112.zoombiegeddon.model.world.physics.WorldPhysics;

/**
 * Defines the base behavior for Lucky Blocks.
 */
public interface ILuckyBlock {

    /**
     * Called when a player breaks the block.
     *
     * @param worldPhysics the physics system used to update the world
     */
    void breakBlock(WorldPhysics worldPhysics);

    /**
     * Placeholder for spawning a future reward entity.
     *
     * @param worldPhysics the physics system used to spawn the reward
     */
    void spawnReward(WorldPhysics worldPhysics);

    /**
     * @return true if this block has already been broken.
     */
    boolean isBroken();

    /**
     * @return Box2D body used by this Lucky Block.
     */
    Body getBody();
}
package inf112.zoombiegeddon.model.world.luckyblock;

import com.badlogic.gdx.physics.box2d.Body;
import inf112.zoombiegeddon.model.world.physics.IWorldPhysics;

/**
 * Defines the base behavior for Lucky Blocks
 */
public interface ILuckyBlock {

    /**
     * Called when a player breaks the block.
     */
    void breakBlock(IWorldPhysics worldPhysics);

    /**
     * Placeholder for spawning a future reward entity.
     */
    void spawnReward(IWorldPhysics worldPhysics);

    /**
     * @return true if this block has already been broken.
     */
    boolean isBroken();

    /**
     * @return Box2D body used by this Lucky Block.
     */
    Body getBody();
}
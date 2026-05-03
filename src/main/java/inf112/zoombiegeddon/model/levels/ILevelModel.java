package inf112.zoombiegeddon.model.levels;

import inf112.zoombiegeddon.model.entity.IEntity;
import inf112.zoombiegeddon.model.entity.player.IPlayer;
import inf112.zoombiegeddon.model.entity.enemies.IEnemy;
import inf112.zoombiegeddon.model.world.luckyblock.LuckyBlockLayerFactory;
import inf112.zoombiegeddon.model.world.physics.IWorldPhysics;
import inf112.zoombiegeddon.model.world.triggers.TriggerZone;

import java.util.List;

/**
 * Defines the runtime state of a single game level: physics, entities, triggers, and timing.
 */
public interface ILevelModel {

    /**
     * Advances the level by one frame.
     *
     * @param delta time in seconds since the last frame
     */
    void update(float delta);

    /**
     * Returns all active entities (player + enemies) as a single list.
     *
     * @return a new list of all active entities
     */
    List<IEntity> getAllEntities();

    /** @return the player instance, or {@code null} if not yet spawned */
    IPlayer getPlayer();

    /** @return the list of active enemies */
    List<IEnemy> getEnemies();

    /** @return the physics world managing this level */
    IWorldPhysics getPhysics();

    /** @return the current sign text to display, or {@code null} if none */
    String getCurrentText();

    /** @return total elapsed time in seconds since the level started */
    float getLevelTime();

    /** @return the trigger zone for this level */
    TriggerZone getTriggerZone();

    /** @return the lucky-block factory used to populate and render lucky blocks */
    LuckyBlockLayerFactory getLuckyBlockFactory();
}

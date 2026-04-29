package inf112.zoombiegeddon.model.world.triggers;

import inf112.zoombiegeddon.model.entity.IPlayer;

/**
 * Trigger action that teleports the player to a predefined spawn position.
 * The spawn coordinates are defined in the TiledMap object's properties
 * and are scaled to world units upon construction.
 */
public class SpawnAction implements ITriggerAction{
    private float x;
    private float y;

    /**
     * Constructs a new SpawnAction with the given spawn coordinates.
     *
     * @param spawnX The x-coordinate of the spawn position in world units.
     * @param spawnY The y-coordinate of the spawn position in world units.
     */
    public SpawnAction(float spawnX, float spawnY) {
        this.x = spawnX;
        this.y = spawnY;

    }
    /**
     * Triggers the spawn action on the given player.
     * Teleports the player to the predefined spawn position and
     * resets their velocity.
     *
     * @param player The player that entered the spawn zone.
     * @return Always returns null.
     */
    @Override
    public String onTrigger(IPlayer player) {
        player.setPosition(this.x, this.y);
        return null;
    }
}

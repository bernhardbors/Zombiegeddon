package inf112.zoombiegeddon.model.world.triggers;

import inf112.zoombiegeddon.model.entity.player.IPlayer;

/**
 * Defines an action that is triggered when the player enters a trigger zone.
 * Implementations handle specific zone behaviors such as killing the player,
 * displaying sign text, completing the level, or teleporting the player.
 */
public interface ITriggerAction {
    /** @return Text that is supposed to be shown (null if no text) */
    String onTrigger(IPlayer player);
}

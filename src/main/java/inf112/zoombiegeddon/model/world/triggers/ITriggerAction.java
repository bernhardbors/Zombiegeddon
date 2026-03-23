package inf112.zoombiegeddon.model.world.triggers;

import inf112.zoombiegeddon.model.entity.Player;

public interface ITriggerAction {
    /** @return Text that is supposed to be shown (null if no text) */
    String onTrigger(Player player);
}

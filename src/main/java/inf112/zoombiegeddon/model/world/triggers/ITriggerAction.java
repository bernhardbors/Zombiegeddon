package inf112.zoombiegeddon.model.world.triggers;

import inf112.zoombiegeddon.model.entity.IPlayer;

public interface ITriggerAction {
    /** @return Text that is supposed to be shown (null if no text) */
    String onTrigger(IPlayer player);
}

package inf112.zoombiegeddon.model.world.triggers;

import inf112.zoombiegeddon.events.GameEvent;
import inf112.zoombiegeddon.events.GameEventBus;
import inf112.zoombiegeddon.model.entity.IPlayer;

public class KillAction implements ITriggerAction {

    @Override
    public String onTrigger(IPlayer player) {
        player.takeDamage(player.getHealth());
        return null;
    }
}

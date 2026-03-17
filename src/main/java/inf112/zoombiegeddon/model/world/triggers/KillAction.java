package inf112.zoombiegeddon.model.world.triggers;

import inf112.zoombiegeddon.model.entity.Player;

public class KillAction implements ITriggerAction {

    @Override
    public String onTrigger(Player player) {
        player.takeDamage(player.getHealth());
        return null;
    }
}

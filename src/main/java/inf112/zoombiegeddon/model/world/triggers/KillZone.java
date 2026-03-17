package inf112.zoombiegeddon.model.world.triggers;

import inf112.zoombiegeddon.model.entity.Player;

public class KillZone implements ITriggerAction {

    @Override
    public String onTrigger(Player player) {
        player.takeDamage(player.getHealth());
        System.out.println("No skulle du vært død");
        return null;
    }
}

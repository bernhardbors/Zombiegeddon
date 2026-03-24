package inf112.zoombiegeddon.model.world.triggers;

import inf112.zoombiegeddon.model.entity.Player;

public class GoalAction implements ITriggerAction {
    @Override
    public String onTrigger(Player player) {
        player.setInGoal(true);
        return null;
    }
}

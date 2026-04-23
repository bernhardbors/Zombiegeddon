package inf112.zoombiegeddon.model.world.triggers;

import inf112.zoombiegeddon.model.entity.IPlayer;

public class GoalAction implements ITriggerAction {
    @Override
    public String onTrigger(IPlayer player) {
        player.setInGoal(true);
        return null;
    }
}

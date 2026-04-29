package inf112.zoombiegeddon.model.world.triggers;

import inf112.zoombiegeddon.model.entity.IPlayer;

/**
 * Trigger action that marks the player as having reached the goal zone.
 * When activated, this action notifies the player that they have entered
 * the goal area, which signals level completion.
 */
public class GoalAction implements ITriggerAction {

    /**
     * Triggers the goal action on the given player.
     * Sets the player's in-goal state to true, which posts a
     * {@link inf112.zoombiegeddon.events.GameEvent#LEVEL_FINISHED} event.
     *
     * @param player The player that entered the goal zone.
     * @return Always returns null.
     */
    @Override
    public String onTrigger(IPlayer player) {
        player.setInGoal(true);
        return null;
    }
}

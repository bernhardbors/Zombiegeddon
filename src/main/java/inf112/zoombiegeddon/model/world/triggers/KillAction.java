package inf112.zoombiegeddon.model.world.triggers;
import inf112.zoombiegeddon.model.entity.IPlayer;

/**
 * Trigger action that instantly kills the player upon entering a kill zone.
 * When activated, this action deals damage equal to the player's current
 * health, reducing it to zero and triggering death.
 */
public class KillAction implements ITriggerAction {

    /**
     * Triggers the kill action on the given player.
     * Deals damage equal to the player's full current health,
     * which sets {@code isDead} to true and posts a
     * {@link inf112.zoombiegeddon.events.GameEvent#PLAYER_DEATH} event.
     *
     * @param player The player that entered the kill zone.
     * @return Always returns null.
     */
    @Override
    public String onTrigger(IPlayer player) {
        player.takeDamage(player.getHealth());
        return null;
    }
}

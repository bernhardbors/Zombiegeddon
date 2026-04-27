package inf112.zoombiegeddon.model.world.triggers;

import inf112.zoombiegeddon.model.entity.IPlayer;

/**
 * Trigger action that displays a text message when the player enters a sign zone.
 * The text is defined in the TiledMap object's properties and returned
 * to the caller for rendering on screen.
 */
public class SignAction implements ITriggerAction{
    private String text;

    /**
     * Constructs a new SignAction with the given display text.
     *
     * @param text The message to display when the player enters the sign zone.
     */
    public SignAction(String text) { this.text = text; }

    /**
     * Triggers the sign action on the given player.
     * Returns the sign's text so it can be rendered on screen.
     *
     * @param player The player that entered the sign zone.
     * @return The text message associated with this sign.
     */
    @Override
    public String onTrigger(IPlayer player) {
        return text;
    }
}

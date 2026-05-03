package inf112.zoombiegeddon.model.entity.player;

/**
 * Provides read-only access to the visual state of the player.
 * Intended for use by the View layer only – no game logic should
 * depend on this interface.
 */
public interface IPlayerVisuals {
    /**
     * @return true if a pickup visual effect is currently active
     */
    boolean hasPickupVisualEffect();

    /**
     * @return intensity of the active pickup effect, between 0 and 1
     */
    float getPickupFxIntensity();
}

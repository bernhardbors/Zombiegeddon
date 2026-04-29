package inf112.zoombiegeddon.model.entity.pickups;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import inf112.zoombiegeddon.model.entity.IPlayer;

/**
 * Represents a collectible pickup in the game world.
 */
public interface IPickup {

    /**
     * Applies the pickup's effect to the player.
     * @param player The player to apply the effect to.
     */
    void applyEffect(IPlayer player);

    /**
     * Marks the pickup as collected and applies its effect.
     * @param player The player collecting the pickup.
     */
    void collect(IPlayer player);

    /** @return True if this pickup has already been collected. */
    boolean isCollected();

    /** @return The Box2D body of this pickup. */
    Body getBody();

    /** @return The world position of this pickup. */
    Vector2 getPosition();

    /** @return The render size of this pickup in world units. */
    float getRenderSize();

    /** @return A string identifier for the pickup type. */
    String getPickupType();

    /** @return The color associated with this pickup's visual effect. */
    Color getPickupColor();
}

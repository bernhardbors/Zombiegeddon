package inf112.zoombiegeddon.model.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import inf112.zoombiegeddon.model.entity.enemies.IEnemy;

/**
 * Interface for the player entity.
 * Combines basic entity properties with damage mechanics and player-specific actions.
 */
public interface IPlayer extends IEntity, IDamageable {
    /** Makes the player jump if grounded. */
    void jump();

    /**
     * Applies a horizontal movement impulse.
     * @param left  True if moving left.
     * @param right True if moving right.
     */
    void applyHorizontalImpulse(boolean left, boolean right);

    /** @param running True if the player is running. */
    void setRunning(boolean running);

    /** @return True if the player is currently running. */
    boolean isRunning();

    /** @return The current speed multiplier including any active boosts. */
    float getActiveSpeedMultiplier();

    /**
     * Applies a temporary speed boost to the player.
     * @param multiplier      The speed multiplier to apply.
     * @param durationSeconds How long the boost lasts.
     */
    void applySpeedBoost(float multiplier, float durationSeconds);

    /**
     * Triggers a visual pickup effect on the player.
     * @param durationSeconds How long the effect lasts.
     * @param effectColor     The color of the effect.
     */
    void triggerPickupVisualEffect(float durationSeconds, Color effectColor);

    /** @return True if a pickup visual effect is currently active. */
    boolean hasPickupVisualEffect();

    /** @return The intensity of the active pickup visual effect. */
    float getPickupFxIntensity();

    /** @return The red component of the pickup effect color. */
    float getPickupFxColorR();

    /** @return The green component of the pickup effect color. */
    float getPickupFxColorG();

    /** @return The blue component of the pickup effect color. */
    float getPickupFxColorB();

    /** @return True if the player is currently in a goal zone. */
    boolean inGoal();

    /** @param bool True if the player is in a goal zone. */
    void setInGoal(boolean bool);

    /** @return The player's bounding rectangle in world units. */
    Rectangle getBounds();

    /**
     * Handles collision between the player and an enemy.
     * @param enemy       The enemy that was hit.
     * @param hitWithFeet True if the player landed on top of the enemy.
     */
    void hitEnemy(IEnemy enemy, boolean hitWithFeet);
}

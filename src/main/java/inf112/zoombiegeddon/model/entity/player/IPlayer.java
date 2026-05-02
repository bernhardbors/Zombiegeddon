package inf112.zoombiegeddon.model.entity.player;

import com.badlogic.gdx.math.Rectangle;
import inf112.zoombiegeddon.events.GameEvent;
import inf112.zoombiegeddon.model.entity.IDamageable;
import inf112.zoombiegeddon.model.entity.IEntity;
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

    void setRunning(boolean running);

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
     */
    void triggerPickupEffect(float durationSeconds);

    /** Notifies the player whether the jump key is currently held. */
    void setJumpHeld(boolean held);

    /** @return true if the player is currently in the goal zone */
    boolean inGoal();

    /**
     * Posts {@link GameEvent#LEVEL_FINISHED} the first time the player enters the goal.
     *
     * @param bool true if the player is in the goal area
     */
    void setInGoal(boolean bool);

    /** @return the player's bounding rectangle in world coordinates */
    Rectangle getBounds();

    /**
     * Handles collision between the player and an enemy.
     * @param enemy       The enemy that was hit.
     * @param hitWithFeet True if the player landed on top of the enemy.
     */
    void hitEnemy(IEnemy enemy, boolean hitWithFeet);
}

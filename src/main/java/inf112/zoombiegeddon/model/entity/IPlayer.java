package inf112.zoombiegeddon.model.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import inf112.zoombiegeddon.model.entity.enemies.IEnemy;

/**
 * Interface for the player entity.
 * Combines basic entity properties with damage mechanics and player-specific actions.
 */
public interface IPlayer extends IEntity, IDamageable {
    void jump();
    void applyHorizontalImpulse(boolean left, boolean right);
    void setRunning(boolean running);
    boolean isRunning();
    float getActiveSpeedMultiplier();

    void applySpeedBoost(float multiplier, float durationSeconds);
    void triggerPickupVisualEffect(float durationSeconds, Color effectColor);
    boolean hasPickupVisualEffect();
    float getPickupFxIntensity();
    float getPickupFxColorR();
    float getPickupFxColorG();
    float getPickupFxColorB();

    boolean inGoal();
    void setInGoal(boolean bool);
    Rectangle getBounds();

    void hitEnemy(IEnemy enemy, boolean hitWithFeet);
}

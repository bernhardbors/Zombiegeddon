package inf112.zoombiegeddon.model.entity.pickups;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import inf112.zoombiegeddon.app.GameConstants;
import inf112.zoombiegeddon.events.GameEvent;
import inf112.zoombiegeddon.events.GameEventBus;
import inf112.zoombiegeddon.model.entity.player.IPlayer;

/**
 * A pickup that restores health to the player upon collection.
 * Also triggers a visual effect regardless of whether healing was applied.
 */
public class HealPickup extends Pickup {

    private static final int HEALTH_AMOUNT = 1;
    private static final float FX_DURATION_SECONDS = 1;

    /**
     * Constructs a heal pickup at the given position.
     * @param world         The Box2D world.
     * @param spawnPosition The position to spawn the pickup.
     */
    public HealPickup(World world, Vector2 spawnPosition) {
        super(world, spawnPosition, 8f * GameConstants.UNIT_SCALE, "heal");
    }

    @Override
    public void applyEffect(IPlayer player) {
        if (player.getHealth() < player.getMaxHealth()) {
            player.heal(HEALTH_AMOUNT);
            GameEventBus.post(GameEvent.PICKUP_HEALTH);
        }
        player.triggerPickupEffect(FX_DURATION_SECONDS);
    }

}

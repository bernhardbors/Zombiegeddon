package inf112.zoombiegeddon.model.entity.pickups;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import inf112.zoombiegeddon.events.GameEvent;
import inf112.zoombiegeddon.events.GameEventBus;
import inf112.zoombiegeddon.model.entity.Player;
import inf112.zoombiegeddon.view.levels.AbstractLevel;

public class HealPickup extends Pickup {

    private static final int HEALTH_AMOUNT = 1;
    private static final float FX_DURATION_SECONDS = 1;

    public HealPickup(World world, Vector2 spawnPosition) {
        super(world, spawnPosition, 8f * AbstractLevel.UNIT_SCALE, "heal", Color.RED);
    }

    @Override
    public void applyEffect(Player player) {
        if (player.getHealth() < player.getMaxHealth()) {
            player.heal(HEALTH_AMOUNT);
            GameEventBus.post(GameEvent.PICKUP_HEALTH);
        }
        player.triggerPickupVisualEffect(FX_DURATION_SECONDS, getPickupColor());

    }

}

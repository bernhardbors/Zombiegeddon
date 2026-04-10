package inf112.zoombiegeddon.model.entity.pickups;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import inf112.zoombiegeddon.model.entity.Player;
import inf112.zoombiegeddon.view.levels.AbstractLevel;

public class HealPickup extends Pickup {

    int healthAmount = 1;
    float FX_DURATION_SECONDS = 1;

    public HealPickup(World world, Vector2 spawnPosition) {
        super(world, spawnPosition, 8f * AbstractLevel.UNIT_SCALE, "heal");
    }

    @Override
    public void applyEffect(Player player) {
        if (player.getHealth() < player.getMaxHealth()) {
            player.heal(healthAmount);
        }
        player.triggerPickupVisualEffect(FX_DURATION_SECONDS);
    }

}

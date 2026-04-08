package inf112.zoombiegeddon.model.entity.pickups;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import inf112.zoombiegeddon.model.entity.Player;
import inf112.zoombiegeddon.view.levels.AbstractLevel;

public class SpeedBoostPickup extends Pickup {

    private static final float SPEED_MULTIPLIER = 1.5f;
    private static final float BOOST_DURATION_SECONDS = 5f;
    private static final float FX_DURATION_SECONDS = 0.9f;

    public SpeedBoostPickup(World world, Vector2 spawnPosition) {
        super(world, spawnPosition, 8f * AbstractLevel.UNIT_SCALE, "speed");
    }

    @Override
    public void applyEffect(Player player) {
        player.applySpeedBoost(SPEED_MULTIPLIER, BOOST_DURATION_SECONDS);
        player.triggerPickupVisualEffect(FX_DURATION_SECONDS);
    }
}

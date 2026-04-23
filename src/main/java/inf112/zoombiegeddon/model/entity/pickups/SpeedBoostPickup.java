package inf112.zoombiegeddon.model.entity.pickups;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import inf112.zoombiegeddon.events.GameEvent;
import inf112.zoombiegeddon.events.GameEventBus;
import inf112.zoombiegeddon.model.entity.Player;
import inf112.zoombiegeddon.model.levels.LevelModel;

public class SpeedBoostPickup extends Pickup {

    private static final float SPEED_MULTIPLIER = 3.5f;
    private static final float BOOST_DURATION_SECONDS = 5f;
    private static final float FX_DURATION_SECONDS = 5f;

    public SpeedBoostPickup(World world, Vector2 spawnPosition) {
        super(world, spawnPosition, 8f * LevelModel.UNIT_SCALE, "speed", Color.YELLOW);
    }

    @Override
    public void applyEffect(Player player) {
        player.applySpeedBoost(SPEED_MULTIPLIER, BOOST_DURATION_SECONDS);
        player.triggerPickupVisualEffect(FX_DURATION_SECONDS, getPickupColor());
        GameEventBus.post(GameEvent.PICKUP_SPEED);
    }
}

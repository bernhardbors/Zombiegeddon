package inf112.zoombiegeddon.model.entity.pickups;

import inf112.zoombiegeddon.events.GameEvent;
import inf112.zoombiegeddon.events.GameEventBus;
import inf112.zoombiegeddon.model.entity.AbstractEntityTest;
import inf112.zoombiegeddon.model.entity.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SpeedBoostPickupTest extends AbstractEntityTest {

    @BeforeEach
    void clearEventBus() {
        GameEventBus.clear();
    }

    @Test
    void testApplyEffect() {
        Player player = new Player(mockedWorld, spawnPos, 3);
        SpeedBoostPickup pickup = new SpeedBoostPickup(mockedWorld, spawnPos);

        pickup.applyEffect(player);
        assertTrue(player.hasPickupVisualEffect());
        assertEquals(3.5f, player.getActiveSpeedMultiplier());

        //Checks that the boost disappears after 5f, and the speed multiplier
        //goes back to 1.0f.
        player.update(6f);
        assertFalse(player.hasPickupVisualEffect());
        assertEquals(1.0f, player.getActiveSpeedMultiplier());
    }

    @Test
    void applyEffectPostsSpeedPickupEvent() {
        boolean[] received = {false};
        GameEventBus.register(e -> { if (e == GameEvent.PICKUP_SPEED) received[0] = true; });

        Player player = new Player(mockedWorld, spawnPos, 3);
        SpeedBoostPickup pickup = new SpeedBoostPickup(mockedWorld, spawnPos);
        pickup.applyEffect(player);

        assertTrue(received[0]);
    }

    @Test
    void collectAppliesBoostOnce() {
        Player player = new Player(mockedWorld, spawnPos, 3);
        SpeedBoostPickup pickup = new SpeedBoostPickup(mockedWorld, spawnPos);

        pickup.collect(player);
        assertEquals(3.5f, player.getActiveSpeedMultiplier());
        assertTrue(pickup.isCollected());
    }

    @Test
    void collectIsIdempotentAndDoesNotReapplyBoost() {
        Player player = new Player(mockedWorld, spawnPos, 3);
        SpeedBoostPickup pickup = new SpeedBoostPickup(mockedWorld, spawnPos);

        pickup.collect(player);
        assertEquals(3.5f, player.getActiveSpeedMultiplier());

        player.update(6f); // boost expires
        assertEquals(1.0f, player.getActiveSpeedMultiplier());

        pickup.collect(player); // second collect: no-op due to isCollected guard
        assertEquals(1.0f, player.getActiveSpeedMultiplier());
    }
}

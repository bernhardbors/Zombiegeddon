package inf112.zoombiegeddon.model.entity.pickups;

import inf112.zoombiegeddon.model.entity.AbstractEntityTest;
import inf112.zoombiegeddon.model.entity.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SpeedBoostPickupTest extends AbstractEntityTest {

    @Test
    void testApplyEffect() {
        Player player = new Player(mockedWorld, spawnPos, mockedAnims, 3);
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
}

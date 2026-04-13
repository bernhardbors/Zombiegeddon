package inf112.zoombiegeddon.model.entity.pickups;

import inf112.zoombiegeddon.model.entity.AbstractEntityTest;
import inf112.zoombiegeddon.model.entity.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HealPickupTest extends AbstractEntityTest {



    @Test
    void testApplyEffect(){
        HealPickup pickup = new HealPickup(mockedWorld, spawnPos);
        Player player = new Player(mockedWorld, spawnPos, mockedAnims, 3);

        player.takeDamage(1);
        pickup.applyEffect(player);

        //Player gets 1 more life if hp is < maxHP
        assertEquals(3, player.getHealth());
        assertTrue(player.hasPickupVisualEffect());

        pickup.applyEffect(player);

        //Player does not get more life if hp = maxHP
        assertEquals(3, player.getHealth());
        assertTrue(player.hasPickupVisualEffect());
    }
}

package inf112.zoombiegeddon.model.entity.pickups;

import inf112.zoombiegeddon.events.GameEvent;
import inf112.zoombiegeddon.events.GameEventBus;
import inf112.zoombiegeddon.model.entity.AbstractEntityTest;
import inf112.zoombiegeddon.model.entity.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HealPickupTest extends AbstractEntityTest {

    @BeforeEach
    void clearEventBus() {
        GameEventBus.clear();
    }

    @Test
    void testApplyEffect(){
        HealPickup pickup = new HealPickup(mockedWorld, spawnPos);
        Player player = new Player(mockedWorld, spawnPos, 3);

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

    @Test
    void applyEffectPostsHealthPickupEventWhenBelowMaxHp() {
        boolean[] received = {false};
        GameEventBus.register(e -> { if (e == GameEvent.PICKUP_HEALTH) received[0] = true; });

        HealPickup pickup = new HealPickup(mockedWorld, spawnPos);
        Player player = new Player(mockedWorld, spawnPos, 3);
        player.takeDamage(1); // hp = 2 < maxHp = 3

        pickup.applyEffect(player);

        assertTrue(received[0]);
    }

    @Test
    void applyEffectDoesNotPostEventWhenAtMaxHp() {
        boolean[] received = {false};
        GameEventBus.register(e -> { if (e == GameEvent.PICKUP_HEALTH) received[0] = true; });

        HealPickup pickup = new HealPickup(mockedWorld, spawnPos);
        Player player = new Player(mockedWorld, spawnPos, 3); // hp = maxHp = 3

        pickup.applyEffect(player);

        assertFalse(received[0]);
    }

    @Test
    void collectAppliesEffectOnce() {
        HealPickup pickup = new HealPickup(mockedWorld, spawnPos);
        Player player = new Player(mockedWorld, spawnPos, 3);

        player.takeDamage(2); // hp = 1
        player.update(1.2f);  // reset invulnerability

        pickup.collect(player); // heals: hp = 2
        assertEquals(2, player.getHealth());
        assertTrue(pickup.isCollected());
    }

    @Test
    void collectIsIdempotentAndDoesNotApplyEffectTwice() {
        HealPickup pickup = new HealPickup(mockedWorld, spawnPos);
        Player player = new Player(mockedWorld, spawnPos, 3);

        player.takeDamage(2); // hp = 1
        player.update(1.2f);

        pickup.collect(player); // heals: hp = 2
        assertEquals(2, player.getHealth());

        player.update(1.2f);
        pickup.collect(player); // second collect: no-op, hp stays at 2
        assertEquals(2, player.getHealth());
    }
}

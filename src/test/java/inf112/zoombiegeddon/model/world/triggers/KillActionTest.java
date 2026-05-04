package inf112.zoombiegeddon.model.world.triggers;

import inf112.zoombiegeddon.model.entity.AbstractEntityTest;
import inf112.zoombiegeddon.model.entity.player.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KillActionTest extends AbstractEntityTest {

    @Test
    void testKillActionActuallyKillsPlayer() {
        KillAction killAction = new KillAction();
        Player player = new Player(mockedWorld, spawnPos, 3);
        assertFalse(player.isDead(), "Player should not be dead before entering the killzone");

        assertNull(killAction.onTrigger(player));
        assertTrue(player.isDead(), "Player should be dead after entering the killzone");
    }
}

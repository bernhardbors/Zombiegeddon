package inf112.zoombiegeddon.model.world.triggers;

import inf112.zoombiegeddon.model.entity.AbstractEntityTest;
import inf112.zoombiegeddon.model.entity.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class KillActionTest extends AbstractEntityTest {

    private KillAction killAction;
    private Player player;

    @Test
    public void testGoalActionSetsPlayerInGoalToTrue() {
        killAction = new KillAction();
        player = new Player(mockedWorld, spawnPos, mockedAnims, 3);
        assertFalse(player.isDead(), "Player should not be dead before entering the killzone");

        killAction.onTrigger(player);
        assertTrue(player.isDead(), "Player should be dead after entering the killzone");
    }
}

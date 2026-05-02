package inf112.zoombiegeddon.model.world.triggers;

import inf112.zoombiegeddon.model.entity.AbstractEntityTest;
import inf112.zoombiegeddon.model.entity.player.Player;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GoalActionTest extends AbstractEntityTest {

    private GoalAction goalAction;
    private Player player;

    @Test
    void testGoalActionSetsPlayerInGoalToTrue() {
        goalAction = new GoalAction();
        player = new Player(mockedWorld, spawnPos, mockedAnims, 3);

        assertFalse(player.inGoal(), "Player should not be in goal");

        goalAction.onTrigger(player);

        assertTrue(player.inGoal(), "Player should be in goal after trigger is activated");
    }
}

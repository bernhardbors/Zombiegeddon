package inf112.zoombiegeddon.model.world.triggers;

import inf112.zoombiegeddon.model.entity.AbstractEntityTest;
import inf112.zoombiegeddon.model.entity.player.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GoalActionTest extends AbstractEntityTest {

    @Test
    void testGoalActionSetsPlayerInGoalToTrue() {
        GoalAction goalAction = new GoalAction();
        Player player = new Player(mockedWorld, spawnPos, 3);

        assertFalse(player.inGoal(), "Player should not be in goal");

        assertNull(goalAction.onTrigger(player));

        assertTrue(player.inGoal(), "Player should be in goal after trigger is activated");

    }
}

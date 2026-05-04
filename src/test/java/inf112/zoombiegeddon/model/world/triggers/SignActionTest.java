package inf112.zoombiegeddon.model.world.triggers;

import inf112.zoombiegeddon.model.entity.AbstractEntityTest;
import inf112.zoombiegeddon.model.entity.player.Player;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SignActionTest extends AbstractEntityTest {

    @Test
    void testSignActionReturnsCorrectText() {
        String testText = "Test123";
        SignAction signAction = new SignAction(testText);
        Player player = new Player(mockedWorld, spawnPos, 3);

        String result = signAction.onTrigger(player);

        assertEquals(testText,result, "SignAction should return the text that was sent through the constructor");
    }
}

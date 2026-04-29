package inf112.zoombiegeddon.model.world.triggers;

import inf112.zoombiegeddon.model.entity.AbstractEntityTest;
import inf112.zoombiegeddon.model.entity.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SignActionTest extends AbstractEntityTest {

    private SignAction signAction;
    private Player player;

    @Test
    void testSignActionReturnsCorrectText() {
        String testText = "Test123";
        signAction = new SignAction(testText);
        player = new Player(mockedWorld, spawnPos, mockedAnims, 3);

        String result = signAction.onTrigger(player);

        assertEquals(testText,result, "SignAction should return the text that was sent through the constructor");
    }
}

package inf112.zoombiegeddon.controller;

import com.badlogic.gdx.Input;
import inf112.zoombiegeddon.AbstractLibGDXTest;
import inf112.zoombiegeddon.model.entity.IPlayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class PlayerControllerTest extends AbstractLibGDXTest {

    private IPlayer player;
    private PlayerController controller;

    @BeforeEach
    void beforeEach() {
        player = mock(IPlayer.class);
        controller = new PlayerController(player);
    }

    @Test
    void testKeyDownSpace() {
        boolean keyDownWasPressed = controller.keyDown(Input.Keys.SPACE);
        verify(player).jump();
        assertTrue(keyDownWasPressed);
    }

    @Test
    void testKeyDownUp() {
        boolean keyDownWasPressed = controller.keyDown(Input.Keys.UP);
        verify(player).jump();
        assertTrue(keyDownWasPressed);

    }

    @Test
    void testKeyDownW() {
        boolean keyDownWasPressed = controller.keyDown(Input.Keys.W);
        verify(player).jump();
        assertTrue(keyDownWasPressed);

    }

    @Test
    void testKeyDownNotJumpingKey() {
        boolean keyDownWasPressed = controller.keyDown(Input.Keys.U);
        assertFalse(keyDownWasPressed);

    }

    @Test
    void testKeyUpSpace(){
        boolean keyUpWasPressed = controller.keyUp(Input.Keys.SPACE);
        assertTrue(keyUpWasPressed);
    }

    @Test
    void keyUpW() {
        boolean keyUpWasPressed = controller.keyUp(Input.Keys.W);
        assertTrue(keyUpWasPressed);
    }

    @Test
    void keyUpUp() {
        boolean keyUpWasPressed = controller.keyUp(Input.Keys.UP);
        assertTrue(keyUpWasPressed);
    }

    @Test
    void keyUpInvalidKey() {
        boolean keyUpWasPressed = controller.keyUp(Input.Keys.U);
        assertFalse(keyUpWasPressed);
    }




}

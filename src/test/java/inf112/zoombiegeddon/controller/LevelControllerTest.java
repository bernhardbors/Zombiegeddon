package inf112.zoombiegeddon.controller;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class LevelControllerTest {

    private LevelController controller;

    @BeforeEach
    void beforeEach() {
        controller = new LevelController();
        Gdx.input = mock(Input.class);
        Gdx.app = mock(Application.class);

    }

    @Test
    void testKeyDownP() {
        boolean pressedP = controller.keyDown(Input.Keys.P);
        assertTrue(pressedP);
    }

    @Test
    void testKeyDownEscape() {
        boolean pressedEscape = controller.keyDown(Input.Keys.ESCAPE);
        assertTrue(pressedEscape);
        verify(Gdx.app).exit();
    }

    @Test
    void testKeyDownInvalid() {
        boolean pressedP = controller.keyDown(Input.Keys.H);
        assertFalse(pressedP);
    }
}

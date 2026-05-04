package inf112.zoombiegeddon.controller;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import inf112.zoombiegeddon.events.GameEvent;
import inf112.zoombiegeddon.events.GameEventBus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class LevelControllerTest {

    private LevelController controller;

    @BeforeEach
    void beforeEach() {
        controller = new LevelController();
        Gdx.input = mock(Input.class);
        Gdx.app = mock(Application.class);

    }

    @Test
    void testKeyDownP() {
        GameEventBus.clear();
        int[] count = {0};
        GameEventBus.register(event -> {if (event == GameEvent.TOGGLE_PAUSE) count[0]++;});

        boolean pressedP = controller.keyDown(Input.Keys.P);

        assertTrue(pressedP);
        assertEquals(1, count[0]);
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

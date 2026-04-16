package inf112.zoombiegeddon.app;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameAppStoryStateTest {

    @Test
    void testStoryRunStateCarriesElapsedTime() {
        GameApp app = new GameApp();

        app.startStoryRun("  Alice  ");
        assertTrue(app.storyModeActive);
        assertFalse(app.storyTimerActive);
        assertEquals("Alice", app.getCurrentPlayerName());

        app.startStoryTimer();
        app.carryStoryTime(42.5f);

        assertEquals(57.5f, app.getStoryElapsedSeconds(15f), 0.0001f);

        app.finishStoryRun();

        assertFalse(app.storyModeActive);
        assertFalse(app.storyTimerActive);
        assertEquals(0f, app.storyTimeCarrySeconds, 0.0001f);
    }
}
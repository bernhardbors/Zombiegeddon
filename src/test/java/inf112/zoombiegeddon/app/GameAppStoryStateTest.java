package inf112.zoombiegeddon.app;

import inf112.zoombiegeddon.model.story.StorySession;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameAppStoryStateTest {

    @Test
    void testStoryRunStateCarriesElapsedTime() {
        GameApp app = new GameApp();

        app.startStoryRun("  Alice  ");
        StorySession session = app.getStorySession();

        assertTrue(session.isActive());
        assertFalse(session.isTimerRunning());
        assertEquals("Alice", session.getPlayerName());

        app.startStoryTimer();
        app.carryStoryTime(42.5f);

        assertEquals(57.5f, app.getStoryElapsedSeconds(15f), 0.0001f);

        app.finishStoryRun();

        assertFalse(session.isActive());
        assertFalse(session.isTimerRunning());
    }
}
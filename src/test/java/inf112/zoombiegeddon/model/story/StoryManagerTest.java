package inf112.zoombiegeddon.model.story;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StoryManagerTest {
    @Test
    void testStoryRunStateCarriesElapsedTime() {
        StoryManager manager = new StoryManager();

        manager.startRun("  Alice  ");
        StorySession session = manager.getSession();

        assertTrue(session.isActive());
        assertFalse(session.isTimerRunning());
        assertEquals("Alice", session.getPlayerName());

        manager.startTimer();
        manager.carryTime(42.5f);

        assertEquals(57.5f, manager.getElapsedSeconds(15f), 0.0001f);

        manager.finishRun();

        assertFalse(session.isActive());
        assertFalse(session.isTimerRunning());
    }
}
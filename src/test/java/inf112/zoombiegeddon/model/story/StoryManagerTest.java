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

    @Test
    void testDefaultStateBeforeStart() {
        StoryManager manager = new StoryManager();

        assertFalse(manager.isActive());
        assertTrue(manager.hasNextLevel());
        assertEquals(3, manager.getTotalLevels());
        assertEquals("Player", manager.getCurrentPlayerName());
        assertEquals("tilemaps/level1.tmx", manager.getCurrentMapPath());
        assertEquals("level1", manager.getCurrentLevelId());
        assertEquals("tilemaps/level1.tmx", manager.getMapPathForLevel(0));
        assertEquals("level1", manager.getIdForLevel(0));
    }

    @Test
    void testAdvanceLevel() {
        StoryManager manager = new StoryManager();

        manager.advanceLevel();
        assertEquals("level2", manager.getCurrentLevelId());
    }

    @Test
    void testReturnsWhenInvalidInput() {
        StoryManager manager = new StoryManager();
        StorySession session = new StorySession(null);

        assertNull(manager.getMapPathForLevel(5));
        assertNull(manager.getIdForLevel(5));

        assertEquals(2, manager.getElapsedSeconds(2));
    }

    @Test
    void hasNextLevelReturnsFalseAfterAdvancingPastAllLevels() {
        StoryManager manager = new StoryManager();
        manager.advanceLevel();
        manager.advanceLevel();
        manager.advanceLevel(); // currentLevelIndex = 3 = storyMapPaths.length
        assertFalse(manager.hasNextLevel());
    }

    @Test
    void getMapPathForLevelWithNegativeIndexReturnsNull() {
        StoryManager manager = new StoryManager();
        assertNull(manager.getMapPathForLevel(-1));
    }

    @Test
    void getIdForLevelWithNegativeIndexReturnsNull() {
        StoryManager manager = new StoryManager();
        assertNull(manager.getIdForLevel(-1));
    }

    @Test
    void getMapPathForLevelAtExactUpperBoundaryReturnsNull() {
        StoryManager manager = new StoryManager();
        // index 3 == storyMapPaths.length: out of bounds
        assertNull(manager.getMapPathForLevel(3));
    }

    @Test
    void getIdForLevelAtExactUpperBoundaryReturnsNull() {
        StoryManager manager = new StoryManager();
        assertNull(manager.getIdForLevel(3));
    }
}
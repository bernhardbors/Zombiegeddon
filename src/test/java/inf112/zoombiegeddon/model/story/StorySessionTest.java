package inf112.zoombiegeddon.model.story;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class StorySessionTest {

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "\t", "\n"})
    void constructorWithInvalidNameDefaultsToPlayer(String invalidName) {
        StorySession session = new StorySession(invalidName);
        assertEquals("Player", session.getPlayerName(),
                "Should default to 'Player' when input is: " + invalidName);
    }

    @Test
    void constructorWithEmptyNameDefaultsToPlayer() {
        StorySession session = new StorySession("");
        assertEquals("Player", session.getPlayerName());
    }

    @Test
    void addTimeAccumulatesPositiveValues() {
        StorySession session = new StorySession("Alice");
        session.addTime(10f);
        session.addTime(5f);
        assertEquals(15f, session.getAccumulatedTimeSeconds(), 0.001f);
    }

    @Test
    void addTimeIgnoresNegativeValues() {
        StorySession session = new StorySession("Alice");
        session.addTime(-10f);
        assertEquals(0f, session.getAccumulatedTimeSeconds(), 0.001f);
    }

    @Test
    void addTimeWithExactlyZeroDoesNotAccumulate() {
        StorySession session = new StorySession("Alice");
        session.addTime(5f);
        session.addTime(0f);
        assertEquals(5f, session.getAccumulatedTimeSeconds(), 0.001f);
    }

    @Test
    void getTotalElapsedSecondsIncludesAccumulatedTimeWhenActiveAndRunning() {
        StorySession session = new StorySession("Alice");
        session.startTimer();
        session.addTime(20f);
        assertEquals(30f, session.getTotalElapsedSeconds(10f), 0.001f);
    }

    @Test
    void getTotalElapsedSecondsReturnsOnlyCurrentLevelTimeWhenTimerNotRunning() {
        StorySession session = new StorySession("Alice");
        session.addTime(20f); // timer never started
        assertEquals(10f, session.getTotalElapsedSeconds(10f), 0.001f);
    }

    @Test
    void getTotalElapsedSecondsReturnsOnlyCurrentLevelTimeAfterFinished() {
        StorySession session = new StorySession("Alice");
        session.startTimer();
        session.addTime(20f);
        session.finish();
        assertEquals(10f, session.getTotalElapsedSeconds(10f), 0.001f);
    }

    @Test
    void finishSetsSessionInactiveAndStopsTimer() {
        StorySession session = new StorySession("Alice");
        session.startTimer();
        session.finish();
        assertFalse(session.isActive());
        assertFalse(session.isTimerRunning());
    }

    @Test
    void startTimerAndStopTimerToggleTimerState() {
        StorySession session = new StorySession("Alice");
        assertFalse(session.isTimerRunning());
        session.startTimer();
        assertTrue(session.isTimerRunning());
        session.stopTimer();
        assertFalse(session.isTimerRunning());
    }
}
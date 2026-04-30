package inf112.zoombiegeddon.model.leaderboard;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LeaderboardEntryTest {

    @Test
    void constructorSetsFieldsCorrectly() {
        LeaderboardEntry entry = new LeaderboardEntry("Harry Maguire",
                "level2",
                250,
                0,
                3,
                45.5f);

        assertEquals("Harry Maguire", entry.getPlayerName());
        assertEquals(250, entry.getScore());
        assertEquals(0, entry.getCollectedItems());
        assertEquals(3, entry.getLivesLeft());
        assertEquals(45.5f, entry.getTimeUsed(), 0.001f);
    }

    @Test
    void getBoardIdReturnsCorrectValue() {
        LeaderboardEntry entry = new LeaderboardEntry("Alice", "level3", 500, 2, 1, 30f);
        assertEquals("level3", entry.getBoardId());
    }

    @Test
    void defaultConstructorCreatesEntryWithZeroValues() {
        LeaderboardEntry entry = new LeaderboardEntry();
        assertEquals(0, entry.getScore());
        assertEquals(0, entry.getCollectedItems());
        assertEquals(0, entry.getLivesLeft());
        assertEquals(0f, entry.getTimeUsed(), 0.001f);
    }
}


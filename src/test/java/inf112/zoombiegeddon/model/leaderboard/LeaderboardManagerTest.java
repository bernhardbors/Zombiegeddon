package inf112.zoombiegeddon.model.leaderboard;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class LeaderboardManagerTest {
    @Test
    void getLeaderboardCreatesNewLeaderboardWhenItDoesNotExist() {
        LeaderboardManager manager = new LeaderboardManager();

        Leaderboard leaderboard = manager.getLeaderboard("level1");

        assertNotNull(leaderboard);
        assertTrue(leaderboard.getEntries().isEmpty());
    }

    @Test
    void getLeaderboardReturnsSameLeaderboardForSameBoardId() {
        LeaderboardManager manager = new LeaderboardManager();

        Leaderboard first = manager.getLeaderboard("level1");
        Leaderboard second = manager.getLeaderboard("level1");

        assertSame(first, second);
    }

    @Test
    void addEntryAddsEntryToCorrectLeaderboard() {
        LeaderboardManager manager = new LeaderboardManager();
        LeaderboardEntry entry = new LeaderboardEntry(
                "Harry Maguire",
                "level1",
                250,
                0,
                3,
                45.5f
        );

        manager.addEntry("level1", entry);

        Leaderboard leaderboard = manager.getLeaderboard("level1");
        assertEquals(1, leaderboard.getEntries().size());
        assertEquals("Harry Maguire", leaderboard.getEntries().get(0).getPlayerName());
        assertEquals(250, leaderboard.getEntries().get(0).getScore());
    }

    @Test
    void addEntryAddsEntriesToSeparateLeaderboards() {
        LeaderboardManager manager = new LeaderboardManager();

        LeaderboardEntry entry1 = new LeaderboardEntry("Player1", "level1", 100, 1, 3, 20f);
        LeaderboardEntry entry2 = new LeaderboardEntry("Player2", "level2", 200, 2, 2, 30f);

        manager.addEntry("level1", entry1);
        manager.addEntry("level2", entry2);

        assertEquals(1, manager.getLeaderboard("level1").getEntries().size());
        assertEquals(1, manager.getLeaderboard("level2").getEntries().size());
        assertEquals("Player1", manager.getLeaderboard("level1").getEntries().get(0).getPlayerName());
        assertEquals("Player2", manager.getLeaderboard("level2").getEntries().get(0).getPlayerName());
    }

    @Test
    void getLeaderboardsReturnsCopyNotOriginalMap() {
        LeaderboardManager manager = new LeaderboardManager();
        manager.getLeaderboard("level1");

        Map<String, Leaderboard> leaderboards = manager.getLeaderboards();
        leaderboards.clear();

        assertEquals(1, manager.getLeaderboards().size());
    }
}

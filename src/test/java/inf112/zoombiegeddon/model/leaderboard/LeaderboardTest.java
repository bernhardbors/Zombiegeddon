package inf112.zoombiegeddon.model.leaderboard;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class LeaderboardTest {
    @Test
    void newLeaderboardIsEmpty() {
        Leaderboard leaderboard = new Leaderboard();

        assertTrue(leaderboard.getEntries().isEmpty());
    }

    @Test
    void addEntryAddsOneEntryToLeaderboard() {
        Leaderboard leaderboard = new Leaderboard();
        LeaderboardEntry entry = new LeaderboardEntry(
                "Harry Maguire",
                "level2",
                250,
                0,
                3,
                45.5f
        );

        leaderboard.addEntry(entry);

        assertEquals(1, leaderboard.getEntries().size());
        assertEquals("Harry Maguire", leaderboard.getEntries().get(0).getPlayerName());
    }

    @Test
    void addEntrySortsEntriesByHighestScoreFirst() {
        Leaderboard leaderboard = new Leaderboard();

        LeaderboardEntry lowScore = new LeaderboardEntry("Player1", "level1", 100, 1, 2, 30f);
        LeaderboardEntry highScore = new LeaderboardEntry("Player2", "level1", 300, 2, 3, 20f);
        LeaderboardEntry mediumScore = new LeaderboardEntry("Player3", "level1", 200, 3, 1, 25f);

        leaderboard.addEntry(lowScore);
        leaderboard.addEntry(highScore);
        leaderboard.addEntry(mediumScore);

        List<LeaderboardEntry> entries = leaderboard.getEntries();

        assertEquals(3, entries.size());
        assertEquals("Player2", entries.get(0).getPlayerName());
        assertEquals("Player3", entries.get(1).getPlayerName());
        assertEquals("Player1", entries.get(2).getPlayerName());
    }

    @Test
    void getTopEntriesReturnsOnlyRequestedNumberOfEntries() {
        Leaderboard leaderboard = new Leaderboard();

        leaderboard.addEntry(new LeaderboardEntry("Player1", "level1", 100, 1, 2, 30f));
        leaderboard.addEntry(new LeaderboardEntry("Player2", "level1", 300, 2, 3, 20f));
        leaderboard.addEntry(new LeaderboardEntry("Player3", "level1", 200, 3, 1, 25f));

        List<LeaderboardEntry> topEntries = leaderboard.getTopEntries(2);

        assertEquals(2, topEntries.size());
        assertEquals("Player2", topEntries.get(0).getPlayerName());
        assertEquals("Player3", topEntries.get(1).getPlayerName());
    }

    @Test
    void getEntriesReturnsCopyNotOriginalList() {
        Leaderboard leaderboard = new Leaderboard();
        leaderboard.addEntry(new LeaderboardEntry("Player1", "level1", 100, 1, 2, 30f));

        List<LeaderboardEntry> entries = leaderboard.getEntries();
        entries.clear();

        assertEquals(1, leaderboard.getEntries().size());
    }
}

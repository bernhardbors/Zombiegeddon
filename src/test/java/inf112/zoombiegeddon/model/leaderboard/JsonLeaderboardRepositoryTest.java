package inf112.zoombiegeddon.model.leaderboard;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonLeaderboardRepositoryTest {

    @Test
    void testSaveAndLoad() {
        LeaderboardManager manager = new LeaderboardManager();
        manager.addEntry("level1", new LeaderboardEntry("Theo", "level1", 4200, 8, 2, 43.2f));

        JsonLeaderboardRepository repository = new JsonLeaderboardRepository("leaderboard.json");
        repository.save(manager);

        LeaderboardManager loadedManager = repository.load();

        assertEquals(1, loadedManager.getLeaderboard("level1").getEntries().size());
    }
}
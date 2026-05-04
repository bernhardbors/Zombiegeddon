package inf112.zoombiegeddon.model.leaderboard;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;

class JsonLeaderboardRepositoryTest {

    @Test
    void testSaveAndLoad() {
        LeaderboardManager manager = new LeaderboardManager();
        manager.addEntry("level1", new LeaderboardEntry("Theo", "level1", 4200, 8, 2, 43.2f));

        JsonLeaderboardRepository repository = new JsonLeaderboardRepository("leaderboard.json");
        repository.save(manager);

        ILeaderboardManager loadedManager = repository.load();

        assertEquals(1, loadedManager.getLeaderboard("level1").getEntries().size());
    }

    @Test
    void testSaveAndLoadStoryModeBoard(@TempDir Path tempDir) {
        Path leaderboardFile = tempDir.resolve("leaderboard.json");

        LeaderboardManager manager = new LeaderboardManager();
        manager.addEntry("storymode", new LeaderboardEntry("Theo", "storymode", 4200, 8, 2, 43.2f));

        JsonLeaderboardRepository repository = new JsonLeaderboardRepository(leaderboardFile.toString());
        repository.save(manager);

        ILeaderboardManager loadedManager = repository.load();

        assertEquals(1, loadedManager.getLeaderboard("storymode").getEntries().size());
    }
}
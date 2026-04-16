package inf112.zoombiegeddon.controller;

import inf112.zoombiegeddon.app.GameApp;
import inf112.zoombiegeddon.model.leaderboard.ILeaderboardRepository;
import inf112.zoombiegeddon.model.leaderboard.LeaderboardEntry;
import inf112.zoombiegeddon.model.leaderboard.LeaderboardManager;
import inf112.zoombiegeddon.model.leaderboard.ScoreCalculator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LeaderboardControllerStoryTest {

    @Test
    void testRegisterStoryModeScoreUsesDedicatedBoard() {
        InMemoryRepository repository = new InMemoryRepository();
        LeaderboardController controller = new LeaderboardController(
                new LeaderboardManager(),
                repository,
                new ScoreCalculator()
        );

        controller.registerLevelCompletion(GameApp.STORY_LEADERBOARD_ID, "Alice", 2, 3, 12.3f);

        LeaderboardEntry entry = repository.savedManager.getLeaderboard(GameApp.STORY_LEADERBOARD_ID).getEntries().get(0);

        assertEquals("Alice", entry.getPlayerName());
        assertEquals(GameApp.STORY_LEADERBOARD_ID, entry.getBoardId());
        assertEquals(2, entry.getCollectedItems());
        assertEquals(3, entry.getLivesLeft());
        assertEquals(12.3f, entry.getTimeUsed(), 0.0001f);
        assertEquals(4577, entry.getScore());
    }

    private static final class InMemoryRepository implements ILeaderboardRepository {

        private LeaderboardManager savedManager = new LeaderboardManager();

        @Override
        public void save(LeaderboardManager leaderboardManager) {
            this.savedManager = leaderboardManager;
        }

        @Override
        public LeaderboardManager load() {
            return savedManager;
        }
    }
}
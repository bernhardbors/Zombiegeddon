package inf112.zoombiegeddon.controller;

import inf112.zoombiegeddon.model.leaderboard.ILeaderboardRepository;
import inf112.zoombiegeddon.model.leaderboard.LeaderboardEntry;
import inf112.zoombiegeddon.model.leaderboard.LeaderboardManager;
import inf112.zoombiegeddon.model.leaderboard.ScoreCalculator;
import inf112.zoombiegeddon.model.leaderboard.ScoreData;

public class LeaderboardController {

    private final LeaderboardManager leaderboardManager;
    private final ILeaderboardRepository leaderboardRepository;
    private final ScoreCalculator scoreCalculator;

    public LeaderboardController(LeaderboardManager leaderboardManager, ILeaderboardRepository leaderboardRepository, ScoreCalculator scoreCalculator) {
        this.leaderboardManager = leaderboardManager;
        this.leaderboardRepository = leaderboardRepository;
        this.scoreCalculator = scoreCalculator;
    }

    public void registerLevelCompletion(String levelId, String playerName, int collectedItems, int livesLeft, float timeUsed) {
        ScoreData scoreData = new ScoreData(collectedItems, livesLeft, timeUsed);
        int score = scoreCalculator.calculateLevelScore(scoreData);

        LeaderboardEntry entry = new LeaderboardEntry(
                playerName,
                levelId,
                score,
                collectedItems,
                livesLeft,
                timeUsed
        );

        leaderboardManager.addEntry(levelId, entry);
        leaderboardRepository.save(leaderboardManager);
    }

    public LeaderboardManager getLeaderboardManager() {
        return leaderboardManager;
    }
}
package inf112.zoombiegeddon.controller;

import inf112.zoombiegeddon.model.leaderboard.ILeaderboardRepository;
import inf112.zoombiegeddon.model.leaderboard.LeaderboardEntry;
import inf112.zoombiegeddon.model.leaderboard.LeaderboardManager;
import inf112.zoombiegeddon.model.leaderboard.ScoreCalculator;
import inf112.zoombiegeddon.model.leaderboard.ScoreData;

/**
 * Controller responsible for handling leaderboard interactions,
 * including registering level completions and persisting results.
 */
public class LeaderboardController {

    private final LeaderboardManager leaderboardManager;
    private final ILeaderboardRepository leaderboardRepository;
    private final ScoreCalculator scoreCalculator;

    /**
     * @param leaderboardManager    Manages in-memory leaderboard entries.
     * @param leaderboardRepository Handles persistence of leaderboard data.
     * @param scoreCalculator       Calculates scores based on level performance.
     */
    public LeaderboardController(LeaderboardManager leaderboardManager, ILeaderboardRepository leaderboardRepository, ScoreCalculator scoreCalculator) {
        this.leaderboardManager = leaderboardManager;
        this.leaderboardRepository = leaderboardRepository;
        this.scoreCalculator = scoreCalculator;
    }

    /**
     * Calculates and registers a score entry for a completed level,
     * then persists the updated leaderboard.
     *
     * @param levelId        The ID of the completed level.
     * @param playerName     The name of the player.
     * @param collectedItems Number of items collected during the level.
     * @param livesLeft      Number of lives remaining at level completion.
     * @param timeUsed       Time in seconds used to complete the level.
     */
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

    /** @return The current leaderboard manager instance. */
    public LeaderboardManager getLeaderboardManager() {
        return leaderboardManager;
    }
}
package inf112.zoombiegeddon.controller;

import inf112.zoombiegeddon.model.leaderboard.ILeaderboardManager;

/**
 * Defines the operations for registering level completions and accessing leaderboard state.
 */
public interface ILeaderboardController {

    /**
     * Calculates and registers a score entry for a completed level,
     * then persists the updated leaderboard.
     *
     * @param levelId        the ID of the completed level
     * @param playerName     the name of the player
     * @param collectedItems number of items collected during the level
     * @param livesLeft      number of lives remaining at level completion
     * @param timeUsed       time in seconds used to complete the level
     */
    void registerLevelCompletion(String levelId, String playerName, int collectedItems, int livesLeft, float timeUsed);

    /**
     * Returns the current leaderboard manager instance.
     *
     * @return the current leaderboard manager instance
     */
    ILeaderboardManager getLeaderboardManager();
}

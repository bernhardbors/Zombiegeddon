package inf112.zoombiegeddon.model.leaderboard;

/**
 * Data record carrying the necessary statistics to calculate a level score.
 *
 * @param collectedItems number of items collected during run.
 * @param livesLeft Number of lives left when a player reaches the goal zone.
 * @param timeUsed Amount of time used to complete a level.
 */
public record ScoreData(
        int collectedItems,
        int livesLeft,
        float timeUsed
) {}

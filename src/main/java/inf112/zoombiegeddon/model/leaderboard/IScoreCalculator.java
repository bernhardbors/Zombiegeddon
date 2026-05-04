package inf112.zoombiegeddon.model.leaderboard;

/**
 * Defines score calculation for completed levels.
 */
public interface IScoreCalculator {

    /**
     * Calculates a score based on level performance data.
     *
     * @param data the performance data used for score calculation
     * @return the calculated score
     */
    int calculateLevelScore(ScoreData data);

}

package inf112.zoombiegeddon.model.leaderboard;

/**
 * Calculates the final score for a completed level.
 */
public class ScoreCalculator implements IScoreCalculator {

    /**
     * Calculates the final score for a completed level.
     *
     * @param data the score input data; must not be {@code null}
     * @return the total score, always >= 0
     */
    public int calculateLevelScore(ScoreData data) {
        int itemScore = data.collectedItems() * 100;
        int lifeScore = data.livesLeft() * 500;
        int timeBonus = Math.max(0, 3000 - (int) (data.timeUsed() * 10));

        return itemScore + lifeScore + timeBonus;
    }
}

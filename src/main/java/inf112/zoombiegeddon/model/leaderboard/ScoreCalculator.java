package inf112.zoombiegeddon.model.leaderboard;

public class ScoreCalculator {
    public int calculateLevelScore(ScoreData data) {
        int itemScore = data.collectedItems() * 100;
        int lifeScore = data.livesLeft() * 500;
        int timeBonus = Math.max(0, 3000 - (int) (data.timeUsed() * 10));

        return itemScore + lifeScore + timeBonus;
    }
}

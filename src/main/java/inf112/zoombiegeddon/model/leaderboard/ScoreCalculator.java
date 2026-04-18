package inf112.zoombiegeddon.model.leaderboard;

public class ScoreCalculator {

    public int calculateLevelScore(ScoreData data) {
        int itemScore = data.getCollectedItems() * 100;
        int lifeScore = data.getLivesLeft() * 500;
        int timeBonus = Math.max(0, 3000 - (int) (data.getTimeUsed() * 10));

        return itemScore + lifeScore + timeBonus;
    }
}

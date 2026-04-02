package inf112.zoombiegeddon.model.leaderboard;

public class ScoreData {
    private final int collectedItems;
    private final int livesLeft;
    private final float timeUsed;

    public ScoreData(int collectibles, int livesLeft, float timeUsed) {
        this.collectedItems = collectibles;
        this.livesLeft = livesLeft;
        this.timeUsed = timeUsed;
    }

    public int getCollectedItems() {
        return collectedItems;
    }

    public int getLivesLeft() {
        return livesLeft;
    }

    public float getTimeUsed() {
        return timeUsed;
    }
}


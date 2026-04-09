package inf112.zoombiegeddon.model.leaderboard;

// Har med mer enn bare score for å senere kunne vise tid, antall liv osv på leaderboard skjermen
public class LeaderboardEntry {
    private final String playerName;
    private final String boardId;
    private final int score;
    private final int collectedItems;
    private final int livesLeft;
    private final float timeUsed;

    public LeaderboardEntry(String playerName, String boardId, int score,
                            int collectedItems, int livesLeft, float timeUsed) {
        this.playerName = playerName;
        this.boardId = boardId;
        this.score = score;
        this.collectedItems = collectedItems;
        this.livesLeft = livesLeft;
        this.timeUsed = timeUsed;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getBoardId() {
        return boardId;
    }

    public int getScore() {
        return score;
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

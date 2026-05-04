package inf112.zoombiegeddon.model.leaderboard;


/**
 * Represents a single leaderboard entry capturing a player's end-of-game stats.
 *
 * Stores extra fields beyond score, such as items, lives, and time, so the
 * leaderboard screen can display a richer summary per player.
 */
public class LeaderboardEntry {
    private String playerName;
    private String boardId;
    private int score;
    private int collectedItems;
    private int livesLeft;
    private float timeUsed;

    /** No-arg constructor required for JSON deserialization. */
    public LeaderboardEntry() {
    }

    /**
     * Creates a fully populated leaderboard entry.
     *
     * @param playerName     the player's display name
     * @param boardId        identifier of the leaderboard this entry belongs to
     * @param score          the player's final score
     * @param collectedItems number of items collected during the run
     * @param livesLeft      number of lives remaining at game end
     * @param timeUsed       total time spent in seconds
     */
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

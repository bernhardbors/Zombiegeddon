package inf112.zoombiegeddon.model.leaderboard;

/**
 * Thrown when leaderboard data cannot be read from or written to persistent storage.
 */
public class LeaderboardPersistenceException extends RuntimeException {
    /**
     * @param message description of the failure
     * @param cause   the underlying I/O or parsing exception
     */
    public LeaderboardPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}

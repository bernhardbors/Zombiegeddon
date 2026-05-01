package inf112.zoombiegeddon.model.leaderboard;

import java.util.Map;

/**
 * Manages a collection of named {@link Leaderboard} instances.
 */
public interface ILeaderboardManager {

    /**
     * Returns the {@link Leaderboard} for the given ID, creating it if it does not yet exist.
     *
     * @param boardId the leaderboard identifier; must not be {@code null}
     * @return the existing or newly created {@link Leaderboard}
     */
    Leaderboard getLeaderboard(String boardId);

    /**
     * Adds an entry to the leaderboard with the given ID, creating it if needed.
     *
     * @param boardId the leaderboard identifier; must not be {@code null}
     * @param entry   the entry to add; must not be {@code null}
     */
    void addEntry(String boardId, LeaderboardEntry entry);

    /**
     * Returns a snapshot of all leaderboards, keyed by board ID.
     *
     * @return a new map containing all current leaderboards
     */
    Map<String, Leaderboard> getLeaderboards();
}

package inf112.zoombiegeddon.model.leaderboard;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages a collection of named {@link Leaderboard} instances.
 *
 * Leaderboards are created on demand the first time a given
 * {@code boardId} is referenced.
 */
public class LeaderboardManager implements ILeaderboardManager {
    private final Map<String, Leaderboard> leaderboards;


    public LeaderboardManager() {
        this.leaderboards = new HashMap<>();
    }

    /**
     * Returns the {@link Leaderboard} for the given ID, creating it if it
     * does not yet exist.
     *
     * @param boardId the leaderboard identifier; must not be {@code null}
     * @return the existing or newly created {@link Leaderboard}
     */
    public Leaderboard getLeaderboard(String boardId) {
        return leaderboards.computeIfAbsent(boardId, id -> new Leaderboard());
    }

    /**
     * Adds an entry to the leaderboard with the given ID, creating the
     * leaderboard if it does not yet exist.
     *
     * @param boardId the leaderboard identifier; must not be {@code null}
     * @param entry   the entry to add; must not be {@code null}
     */
    public void addEntry(String boardId, LeaderboardEntry entry) {
        getLeaderboard(boardId).addEntry(entry);
    }

    /**
     * Returns a snapshot of all leaderboards, keyed by board ID.
     *
     * @return a new map containing all current leaderboards
     */
    public Map<String, Leaderboard> getLeaderboards() {
        return new HashMap<>(leaderboards);
    }
}

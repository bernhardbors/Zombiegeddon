package inf112.zoombiegeddon.model.leaderboard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Data transfer object for serializing and deserializing leaderboard state.
 *
 * Used internally by {@link JsonLeaderboardRepository} when reading and
 * writing JSON.
 */
public class LeaderboardData {
    private Map<String, List<LeaderboardEntry>> leaderboards;

    /**
     * Creates an empty {@code LeaderboardData} with no entries.
     */
    public LeaderboardData() {
        this.leaderboards = new HashMap<>();
    }

    /**
     * Creates a {@code LeaderboardData} pre-populated with the given map.
     *
     * @param leaderboards map of board ID to its list of entries; must not be {@code null}
     */
    public LeaderboardData(Map<String, List<LeaderboardEntry>> leaderboards) {
        this.leaderboards = leaderboards;
    }

    /**
     * Returns the map of board IDs to their entries.
     *
     * @return the leaderboards map, never {@code null}
     */
    public Map<String, List<LeaderboardEntry>> getLeaderboards() {
        return leaderboards;
    }

    /**
     * Replaces the leaderboards map.
     *
     * @param leaderboards the new map; must not be {@code null}
     */
    public void setLeaderboards(Map<String, List<LeaderboardEntry>> leaderboards) {
        this.leaderboards = leaderboards;
    }

}

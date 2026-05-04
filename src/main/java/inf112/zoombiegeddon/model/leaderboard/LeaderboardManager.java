package inf112.zoombiegeddon.model.leaderboard;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages a collection of named {@link Leaderboard} instances.
 *
 * <p>Leaderboards are created on demand the first time a given
 * {@code boardId} is referenced.</p>
 */
public class LeaderboardManager implements ILeaderboardManager {
    private final Map<String, Leaderboard> leaderboards;


    public LeaderboardManager() {
        this.leaderboards = new HashMap<>();
    }

    @Override
    public Leaderboard getLeaderboard(String boardId) {
        return leaderboards.computeIfAbsent(boardId, id -> new Leaderboard());
    }

    @Override
    public void addEntry(String boardId, LeaderboardEntry entry) {
        getLeaderboard(boardId).addEntry(entry);
    }

    @Override
    public Map<String, Leaderboard> getLeaderboards() {
        return new HashMap<>(leaderboards);
    }
}

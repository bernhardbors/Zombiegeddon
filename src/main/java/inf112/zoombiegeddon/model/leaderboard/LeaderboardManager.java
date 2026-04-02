package inf112.zoombiegeddon.model.leaderboard;

import java.util.HashMap;
import java.util.Map;

public class LeaderboardManager {
    private final Map<String, Leaderboard> leaderboards;

    public LeaderboardManager() {
        this.leaderboards = new HashMap<>();
    }

    public Leaderboard getLeaderboard(String boardId) {
        return leaderboards.computeIfAbsent(boardId, id -> new Leaderboard());
    }

    public void addEntry(String boardId, LeaderboardEntry entry) {
        getLeaderboard(boardId).addEntry(entry);
    }

    public Map<String, Leaderboard> getLeaderboards() {
        return new HashMap<>(leaderboards);
    }
}

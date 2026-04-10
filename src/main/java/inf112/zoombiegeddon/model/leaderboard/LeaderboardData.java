package inf112.zoombiegeddon.model.leaderboard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeaderboardData {
    private Map<String, List<LeaderboardEntry>> leaderboards;

    public LeaderboardData() {
        this.leaderboards = new HashMap<>();
    }

    public LeaderboardData(Map<String, List<LeaderboardEntry>> leaderboards) {
        this.leaderboards = leaderboards;
    }

    public Map<String, List<LeaderboardEntry>> getLeaderboards() {
        return leaderboards;
    }

    public void setLeaderboards(Map<String, List<LeaderboardEntry>> leaderboards) {
        this.leaderboards = leaderboards;
    }

}

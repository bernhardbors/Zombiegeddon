package inf112.zoombiegeddon.model.leaderboard;

public interface ILeaderboardRepository {

    void save(LeaderboardManager leaderboardManager);

    LeaderboardManager load();
}

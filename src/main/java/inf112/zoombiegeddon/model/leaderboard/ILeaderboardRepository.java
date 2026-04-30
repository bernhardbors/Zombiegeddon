package inf112.zoombiegeddon.model.leaderboard;

public interface ILeaderboardRepository {

    /**
     * Persists the current state of the leaderboard.
     *
     * @param leaderboardManager the manager whose state to save; must not be {@code null}
     */
    void save(LeaderboardManager leaderboardManager);

    /**
     * Loads and returns a {@link LeaderboardManager} from the underlying storage.
     *
     * <p>If no previously saved state exists, implementations should return
     * a default/empty {@link LeaderboardManager} rather than {@code null}.</p>
     *
     * @return the restored {@link LeaderboardManager}, never {@code null}
     */
    LeaderboardManager load();
}

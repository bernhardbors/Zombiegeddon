package inf112.zoombiegeddon.model.leaderboard;

/**
 * Defines persistence operations for leaderboard data.
 */
public interface ILeaderboardRepository {

    /**
     * Persists the current state of the leaderboard.
     *
     * @param leaderboardManager the manager whose state to save; must not be {@code null}
     */
    void save(ILeaderboardManager leaderboardManager);

    /**
     * Loads and returns an {@link ILeaderboardManager} from the underlying storage.
     * If no previously saved state exists, implementations should return
     * a default or empty implementation rather than {@code null}.
     *
     * @return the restored manager, never {@code null}
     */
    ILeaderboardManager load();
}

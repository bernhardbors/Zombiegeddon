package inf112.zoombiegeddon.model.leaderboard;

public interface ILeaderboardRepository {

    /**
     * Persists the current state of the leaderboard.
     *
     * @param leaderboardManager the manager whose state to save; must not be {@code null}
     */
    void save(ILeaderboardManager leaderboardManager);

    /**
     * Loads and returns an {@link ILeaderboardManager} from the underlying storage.
     *
     * <p>If no previously saved state exists, implementations should return
     * a default/empty implementation rather than {@code null}.</p>
     *
     * @return the restored manager, never {@code null}
     */
    ILeaderboardManager load();
}

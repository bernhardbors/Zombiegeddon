package inf112.zoombiegeddon.model.leaderboard;

import com.badlogic.gdx.utils.Json;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * A JSON-backed implementation of {@link ILeaderboardRepository} that reads and
 * writes leaderboard data to a local file.
 *
 * <p>Serialization is handled by libGDX's {@link Json} utility. The file is
 * created or overwritten on every {@link #save} call.</p>
 *
 * @param filePath path to the JSON file used for persistence; must be a valid,
 *                 writable file-system path
 */
public record JsonLeaderboardRepository(String filePath) implements ILeaderboardRepository {

    @Override
    public void save(LeaderboardManager leaderboardManager) {
        LeaderboardData data = new LeaderboardData();

        for (String boardId : leaderboardManager.getLeaderboards().keySet()) {
            Leaderboard leaderboard = leaderboardManager.getLeaderboard(boardId);
            data.getLeaderboards().put(boardId, leaderboard.getEntries());
        }

        Json json = new Json();
        json.setUsePrototypes(false);
        String jsonString = json.prettyPrint(data);

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(jsonString);
        } catch (IOException exception) {
            throw new LeaderboardPersistenceException("Failed to save leaderboard data.", exception);
        }
    }

    @Override
    public LeaderboardManager load() {
        Json json = new Json();

        try (FileReader reader = new FileReader(filePath)) {
            LeaderboardData data = json.fromJson(LeaderboardData.class, reader);
            LeaderboardManager leaderboardManager = new LeaderboardManager();

            if (data != null && data.getLeaderboards() != null) {
                for (String boardId : data.getLeaderboards().keySet()) {
                    for (LeaderboardEntry entry : data.getLeaderboards().get(boardId)) {
                        leaderboardManager.addEntry(boardId, entry);
                    }
                }
            }

            return leaderboardManager;
        } catch (IOException _) {
            return new LeaderboardManager();
        }
    }
}
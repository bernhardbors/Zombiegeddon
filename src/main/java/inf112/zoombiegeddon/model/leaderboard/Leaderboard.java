package inf112.zoombiegeddon.model.leaderboard;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Leaderboard {

    private final List<LeaderboardEntry> entries;

    public Leaderboard() {
        this.entries = new ArrayList<>();
    }

    public void addEntry(LeaderboardEntry entry) {
        entries.add(entry);
        entries.sort(Comparator.comparingInt(LeaderboardEntry::getScore).reversed());
    }

    public List<LeaderboardEntry> getTopEntries(int limit) {
        return new ArrayList<>(entries.stream().limit(limit).toList());
    }

    public List<LeaderboardEntry> getEntries() {
        return new ArrayList<>(entries);
    }
}

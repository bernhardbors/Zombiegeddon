package inf112.zoombiegeddon.model.leaderboard;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Represents a single leaderboard that maintains a score-sorted list of
 * {@link LeaderboardEntry} objects.
 *
 * Entries are kept in descending order by score at all times.
 */
public class Leaderboard {

    private final List<LeaderboardEntry> entries;

    public Leaderboard() {
        this.entries = new ArrayList<>();
    }

    /**
     * Adds an entry to the leaderboard and re-sorts by score descending.
     *
     * @param entry the entry to add; must not be {@code null}
     */
    public void addEntry(LeaderboardEntry entry) {
        entries.add(entry);
        entries.sort(Comparator.comparingInt(LeaderboardEntry::getScore).reversed());
    }

    /**
     * Returns the top {@code limit} entries by score.
     *
     * @param limit maximum number of entries to return; must be &gt; 0
     * @return a new list containing at most {@code limit} entries, in descending score order
     */
    public List<LeaderboardEntry> getTopEntries(int limit) {
        return new ArrayList<>(entries.stream().limit(limit).toList());
    }

    /**
     * Returns a snapshot of all entries in descending score order.
     *
     * @return a new list containing all entries
     */
    public List<LeaderboardEntry> getEntries() {
        return new ArrayList<>(entries);
    }
}

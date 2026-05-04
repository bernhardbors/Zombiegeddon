package inf112.zoombiegeddon.model.story;

/**
 * Represents a single story run session, tracking the player name,
 * timer state, and accumulated time across levels.
 */
public class StorySession {
    private final String playerName;
    private boolean active;
    private boolean timerRunning;
    private float accumulatedTimeSeconds;

    /**
     * Creates a new story session for the given player.
     *
     * @param playerName the player's name. Defaults to "Player" if null or blank
     */
    public StorySession(String playerName) {
        this.playerName = (playerName == null || playerName.isBlank()) ? "Player" : playerName.trim();
        this.active = true;
        this.timerRunning = false;
        this.accumulatedTimeSeconds = 0f;
    }

    public void startTimer() { this.timerRunning = true; }
    public void stopTimer() { this.timerRunning = false; }

    /**
     * Adds time to the accumulated total. Negative values are ignored.
     *
     * @param seconds Seconds to add.
     */
    public void addTime(float seconds) {
        this.accumulatedTimeSeconds += Math.max(0f, seconds);
    }

    /**
     * Returns total elapsed time including the current level's time.
     *
     * @param currentLevelTime Time elapsed in the current level.
     * @return Total seconds elapsed, or just currentLevelTime if session is inactive.
     */
    public float getTotalElapsedSeconds(float currentLevelTime) {
        if (active && timerRunning) {
            return accumulatedTimeSeconds + currentLevelTime;
        }
        return currentLevelTime;
    }

    /** Marks the session as finished and stops the timer. */
    public void finish() {
        this.active = false;
        this.timerRunning = false;
    }

    public boolean isActive() { return active; }
    public boolean isTimerRunning() { return timerRunning; }
    public float getAccumulatedTimeSeconds() { return accumulatedTimeSeconds; }
    public String getPlayerName() { return playerName; }
}

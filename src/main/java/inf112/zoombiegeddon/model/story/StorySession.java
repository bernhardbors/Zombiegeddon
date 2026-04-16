package inf112.zoombiegeddon.model.story;

public class StorySession {
    private final String playerName;
    private boolean active;
    private boolean timerRunning;
    private float accumulatedTimeSeconds;

    public StorySession(String playerName) {
        this.playerName = (playerName == null || playerName.isBlank()) ? "Player" : playerName.trim();
        this.active = true;
        this.timerRunning = false;
        this.accumulatedTimeSeconds = 0f;
    }

    public void startTimer() { this.timerRunning = true; }
    public void stopTimer() { this.timerRunning = false; }

    public void addTime(float seconds) {
        this.accumulatedTimeSeconds += Math.max(0f, seconds);
    }

    public float getTotalElapsedSeconds(float currentLevelTime) {
        if (active && timerRunning) {
            return accumulatedTimeSeconds + currentLevelTime;
        }
        return currentLevelTime;
    }

    public void finish() {
        this.active = false;
        this.timerRunning = false;
    }

    //Getters for testing
    public boolean isActive() { return active; }
    public boolean isTimerRunning() { return timerRunning; }
    public float getAccumulatedTimeSeconds() { return accumulatedTimeSeconds; }
    public String getPlayerName() { return playerName; }
}

package inf112.zoombiegeddon.model.story;

public class StoryManager implements IStoryManager{
    private StorySession storySession;

    @Override
    public void startRun(String playerName) {
        this.storySession = new StorySession(playerName);
    }

    @Override
    public void startTimer() {
        if (storySession != null) storySession.startTimer();
    }

    @Override
    public void carryTime(float elapsedSeconds) {
        if (storySession != null) storySession.addTime(elapsedSeconds);
    }

    @Override
    public float getElapsedSeconds(float currentLevelTime) {
        if (storySession != null) {
            return storySession.getTotalElapsedSeconds(currentLevelTime);
        }
        return currentLevelTime;
    }

    @Override
    public void finishRun() {
        if (storySession != null) storySession.finish();
    }

    @Override
    public boolean isActive() {
        return storySession != null && storySession.isActive();
    }

    @Override
    public StorySession getSession() {
        return storySession;
    }

    @Override
    public String getCurrentPlayerName() {
        return storySession != null ? storySession.getPlayerName() : "Player";
    }
}

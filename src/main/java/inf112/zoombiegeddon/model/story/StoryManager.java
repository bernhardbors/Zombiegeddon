package inf112.zoombiegeddon.model.story;

public class StoryManager implements IStoryManager{
    private StorySession storySession;

    private final String[] storyMapPaths = {"tilemaps/level1.tmx", "tilemaps/level3.tmx", "tilemaps/level5.tmx"};
    private final String[] storyLevelIds = {"level1", "level2", "level3"};
    private int currentLevelIndex = 0;

    @Override
    public void startRun(String playerName) {
        this.storySession = new StorySession(playerName);
        this.currentLevelIndex = 0;
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
    public boolean hasNextLevel() {
        return currentLevelIndex < storyMapPaths.length;
    }

    @Override
    public void advanceLevel() {
        currentLevelIndex++;
    }

    @Override
    public int getTotalLevels() {
        return storyMapPaths.length;
    }

    @Override
    public String getMapPathForLevel(int index) {
        if (index >= 0 && index < storyMapPaths.length) {
            return storyMapPaths[index];
        }
        return null;
    }

    @Override
    public String getIdForLevel(int index) {
        if (index >= 0 && index < storyLevelIds.length) {
            return storyLevelIds[index];
        }
        return null;
    }

    @Override
    public String getCurrentMapPath() {
        return storyMapPaths[currentLevelIndex];
    }

    @Override
    public String getCurrentLevelId() {
        return storyLevelIds[currentLevelIndex];
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

package inf112.zoombiegeddon.model.story;

public interface IStoryManager {

    public void startRun(String playerName);

    public void startTimer();

    public void carryTime(float elapsedSeconds);

    public float getElapsedSeconds(float currentLevelTime);

    public void finishRun();

    public boolean isActive();

    public boolean hasNextLevel();

    public void advanceLevel();

    public String getCurrentMapPath();

    public String getCurrentLevelId();

    public StorySession getSession();

    public String getCurrentPlayerName();

    public int getTotalLevels();

    public String getMapPathForLevel(int index);

    public String getIdForLevel(int index);
}

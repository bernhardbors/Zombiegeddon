package inf112.zoombiegeddon.model.story;

public interface IStoryManager {

    public void startRun(String playerName);

    public void startTimer();

    public void carryTime(float elapsedSeconds);

    public float getElapsedSeconds(float currentLevelTime);

    public void finishRun();

    public boolean isActive();

    public StorySession getSession();

    public String getCurrentPlayerName();
}

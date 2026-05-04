package inf112.zoombiegeddon.model.story;
/**
 * Manages the story progression and run state for a game session.
 */
public interface IStoryManager {

    /**
     * Starts a new run with the given player name.
     * @param playerName The name of the player.
     */
    void startRun(String playerName);

    /** Starts the timer for the current level. */
    void startTimer();

    /**
     * Carries over elapsed time from a previous level.
     * @param elapsedSeconds Seconds to add to the total.
     */
    void carryTime(float elapsedSeconds);

    /**
     * Returns total elapsed seconds including current level time.
     *
     * @param currentLevelTime the time elapsed in the current level
     * @return total elapsed seconds for the current run
     */
    float getElapsedSeconds(float currentLevelTime);

    /** Marks the current run as finished. */
    void finishRun();

    /** @return true if a run is currently active. */
    boolean isActive();

    /** @return true if there is another level after the current one. */
    boolean hasNextLevel();

    /** Advances to the next level in the story. */
    void advanceLevel();

    /** @return The file path of the current level's map. */
    String getCurrentMapPath();

    /** @return The ID of the current level. */
    String getCurrentLevelId();

    /** @return The name of the current player. */
    String getCurrentPlayerName();

    /** @return The total number of levels in the story. */
    int getTotalLevels();

    /**
     * @param index The level index.
     * @return The map file path for the given level.
     */
    String getMapPathForLevel(int index);

    /**
     * @param index The level index.
     * @return The ID for the given level.
     */
    String getIdForLevel(int index);
}
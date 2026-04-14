package inf112.zoombiegeddon.app;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import inf112.zoombiegeddon.audio.AudioManager;
import inf112.zoombiegeddon.audio.IAudioService;
import inf112.zoombiegeddon.controller.LeaderboardController;
import inf112.zoombiegeddon.model.leaderboard.ILeaderboardRepository;
import inf112.zoombiegeddon.model.leaderboard.JsonLeaderboardRepository;
import inf112.zoombiegeddon.model.leaderboard.LeaderboardManager;
import inf112.zoombiegeddon.model.leaderboard.ScoreCalculator;
import inf112.zoombiegeddon.model.world.background.ParallaxBackground;
import inf112.zoombiegeddon.view.screens.LoadingScreen;

/**
 * The main entry point and orchestrator for the Zoombiegeddon application.
 * This class manages global resources such as the AssetManager, SpriteBatch,
 * preferences, and shared UI/animation components. It handles the initial
 * setup of the game window and transitions to the loading screen.
 */
public class GameApp extends Game {
    public static final String STORY_LEADERBOARD_ID = "storymode";

    private SpriteBatch batch;
    private Assets assets;
    private IAudioService audio;
    private Preferences prefs;
    private ShapeRenderer shapeRenderer;
    private LeaderboardController leaderboardController;
    private ParallaxBackground background;

    private ILeaderboardRepository leaderboardRepository;
    private LeaderboardManager leaderboardManager;
    private ScoreCalculator scoreCalculator;
    private String currentPlayerName = "Player";
    public boolean storyModeActive;
    public boolean storyTimerActive;
    public float storyTimeCarrySeconds;
    /**
     * Initializes the core components of the application.
     * Sets up the rendering batch, asset management, user preferences.
     * It also configures the window resolution and starts the loading screen.
     */
    @Override
    public void create() {
        this.batch = new SpriteBatch();
        this.prefs = Gdx.app.getPreferences(AssetConstants.PREFS_NAME);
        this.shapeRenderer = new ShapeRenderer();
        this.background = new ParallaxBackground();

        this.assets = new Assets();
        this.assets.load();

        this.audio = new AudioManager(assets.getManager(), prefs);

        initializeLeaderboardSystem();
        setResolutionOnStartup();

        this.setScreen(new LoadingScreen(this));
    }

    private void initializeLeaderboardSystem() {
        leaderboardRepository = new JsonLeaderboardRepository("leaderboard.json");
        leaderboardManager = leaderboardRepository.load();
        scoreCalculator = new ScoreCalculator();
        leaderboardController = new LeaderboardController(
                leaderboardManager,
                leaderboardRepository,
                scoreCalculator
        );
    }

    /**
     * Configures the window resolution based on saved user preferences.
     * Defaults to 1280x720 if no preferences are found.
     */
    private void setResolutionOnStartup() {
        int width = prefs.getInteger(AssetConstants.PREF_WIDTH, 1280);
        int height = prefs.getInteger(AssetConstants.PREF_HEIGHT, 720);
        Gdx.graphics.setWindowedMode(width, height);
    }

    /**
     * Cleans up and releases all managed resources.
     * Disposes of the AssetManager, SpriteBatch, and ensures the current
     * screen is properly hidden.
     */
    @Override
    public void dispose() {
        if (getScreen() != null) getScreen().hide();
        if (audio != null) audio.dispose();
        if (assets != null) assets.dispose();
        if (batch != null) batch.dispose();
        if (shapeRenderer != null) shapeRenderer.dispose();
    }

    public SpriteBatch getBatch() { return batch; }
    public Assets getAssets() { return assets; }
    public IAudioService getAudio() { return audio; }
    public LeaderboardController getLeaderboardController() { return leaderboardController; }
    public LeaderboardManager getLeaderboardManager() { return leaderboardManager; }
    public ParallaxBackground getBackground() { return background; }
    public String getCurrentPlayerName() { return currentPlayerName; }
    public Preferences getPrefs() { return prefs; }
    public ShapeRenderer getShapeRenderer() { return shapeRenderer; }

    public void setCurrentPlayerName(String currentPlayerName) {
        if (currentPlayerName == null || currentPlayerName.isBlank()) {
            this.currentPlayerName = "Player";
        } else {
            this.currentPlayerName = currentPlayerName.trim();
        }
    }

    public void startStoryRun(String playerName) {
        setCurrentPlayerName(playerName);
        storyModeActive = true;
        storyTimerActive = false;
        storyTimeCarrySeconds = 0f;
    }

    public void startStoryTimer() {
        storyModeActive = true;
        storyTimerActive = true;
    }

    public void carryStoryTime(float elapsedSeconds) {
        storyTimeCarrySeconds = Math.max(0f, elapsedSeconds);
    }

    public float getStoryElapsedSeconds(float currentLevelTime) {
        if (storyModeActive && storyTimerActive) {
            return storyTimeCarrySeconds + currentLevelTime;
        }

        return currentLevelTime;
    }

    public void finishStoryRun() {
        storyModeActive = false;
        storyTimerActive = false;
        storyTimeCarrySeconds = 0f;
    }

}
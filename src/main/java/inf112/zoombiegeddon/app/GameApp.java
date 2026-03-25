package inf112.zoombiegeddon.app;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import inf112.zoombiegeddon.audio.AudioManager;
import inf112.zoombiegeddon.audio.IAudioService;
import inf112.zoombiegeddon.model.animations.AnimationHandler;
import inf112.zoombiegeddon.model.world.background.ParallaxBackground;
import inf112.zoombiegeddon.view.screens.LoadingScreen;

/**
 * The main entry point and orchestrator for the Zoombiegeddon application.
 * This class manages global resources such as the AssetManager, SpriteBatch,
 * preferences, and shared UI/animation components. It handles the initial
 * setup of the game window and transitions to the loading screen.
 */
public class GameApp extends Game {

    // Public shared resources
    public SpriteBatch batch;
    public AssetManager manager;
    public IAudioService audio;
    public Preferences prefs;
    public Skin skin;
    public BitmapFont font;
    public AnimationHandler playerMaleAnimations;
    public AnimationHandler playerFemaleAnimations;
    public AnimationHandler zombieAnimations;
    public AnimationHandler chonkerAnimations;
    public ParallaxBackground background;
    public ShapeRenderer shapeRenderer;

    /**
     * Initializes the core components of the application.
     * Sets up the rendering batch, asset management, and user preferences.
     * It also configures the window resolution and starts the loading screen.
     */
    @Override
    public void create() {
        batch = new SpriteBatch();
        manager = new AssetManager();
        prefs = Gdx.app.getPreferences(AssetConstants.PREFS_NAME);
        audio = new AudioManager(manager, prefs);

        setResolutionOnStartup();

        background = new ParallaxBackground(new OrthographicCamera());

        this.setScreen(new LoadingScreen(this));
        Gdx.graphics.setForegroundFPS(60);
        this.shapeRenderer = new ShapeRenderer();
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
        if (manager != null) manager.dispose();
        if (batch != null) batch.dispose();
    }

    /**
     * Configures the window resolution based on saved user preferences.
     * Defaults to 1280x720 if no preferences are found.
     */
    private void setResolutionOnStartup() {
        int width = prefs.getInteger("width", 1280);
        int height = prefs.getInteger("height", 720);
        Gdx.graphics.setWindowedMode(width, height);
    }
}
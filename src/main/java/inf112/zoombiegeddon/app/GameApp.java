package inf112.zoombiegeddon.app;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import inf112.zoombiegeddon.audio.AudioManager;
import inf112.zoombiegeddon.audio.GameSound;
import inf112.zoombiegeddon.view.FontGenerator;
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
    public AudioManager audioManager;
    public Preferences prefs;
    public Skin skin;
    public BitmapFont font;
    public AnimationHandler playerAnimations;
    public AnimationHandler enemyAnimations;
    public ParallaxBackground background;

    // Asset paths and constants
    public static final String UI_SKIN = "ui/skin/uiskin.json";
    public static final String FONT_PATH = "fonts/WhitePeaberryOutline.fnt";
    public static final String CHARACTER_SHEET = "assets/characterSprite32x32/MaleSkin1-Sheet.png";
    public static final String ENEMY_SHEET = "assets/characterSprite32x32/Enemy-Sheet.png";
    public static final String BG_SKY = "assets/backgrounds/CityBackgroundSky.png";
    public static final String BG_CITY = "assets/backgrounds/CityBackground.png";
    public static final String BG_L1 = "assets/backgrounds/CityBackgroundLayer1.png";
    public static final String BG_L2 = "assets/backgrounds/CityBackgroundLayer2.png";

    // Preference keys
    public static final String PREFS_NAME = "Prefs";
    public static final String PREF_WIDTH = "width";
    public static final String PREF_HEIGHT = "height";
    public static final String PREF_RES_STRING = "resString";

    /**
     * Initializes the core components of the application.
     * Sets up the rendering batch, asset management, and user preferences.
     * It also configures the window resolution and starts the loading screen.
     */
    @Override
    public void create() {
        batch = new SpriteBatch();
        manager = new AssetManager();
        audioManager = new AudioManager(manager);
        prefs = Gdx.app.getPreferences(PREFS_NAME);

        setResolutionOnStartup();

        background = new ParallaxBackground(new OrthographicCamera());

        this.setScreen(new LoadingScreen(this));
        Gdx.graphics.setForegroundFPS(60);
    }

    /**
     * Cleans up and releases all managed resources.
     * Disposes of the AssetManager, SpriteBatch, and ensures the current
     * screen is properly hidden.
     */
    @Override
    public void dispose() {
        if (getScreen() != null) getScreen().hide();
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
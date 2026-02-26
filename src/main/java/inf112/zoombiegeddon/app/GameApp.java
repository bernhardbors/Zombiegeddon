package inf112.zoombiegeddon.app;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import inf112.zoombiegeddon.model.animations.AnimationHandler;
import inf112.zoombiegeddon.model.world.background.ParallaxBackground;
import inf112.zoombiegeddon.view.screens.LoadingScreen;

public class GameApp extends Game {

    public Skin skin;
    public SpriteBatch batch;
    public Preferences prefs;
    public BitmapFont font;
    public AssetManager manager;
    public Texture characterSpriteSheet;
    public ParallaxBackground background;
    public AnimationHandler playerAnimations;

    public static final String UI_SKIN = "ui/skin/uiskin.json";
    public static final String SPLASH_LOGO = "background/background1.png";
    public static final String CHARACTER_SPRITE_SHEET = "assets/characterSprite32x32/MaleSkin1-Sheet.png";

    public static final String FONT_PATH = "fonts/WhitePeaberryOutline.fnt";

    public static final String BACKGROUND_LAYER0_PATH = "assets/backgrounds/CityBackgroundSky.png";
    public static final String BACKGROUND_LAYER1_PATH = "assets/backgrounds/CityBackground.png";
    public static final String BACKGROUND_LAYER2_PATH = "assets/backgrounds/CityBackgroundLayer1.png";
    public static final String BACKGROUND_LAYER3_PATH = "assets/backgrounds/CityBackgroundLayer2.png";
    
    // Preferences Constants
    public static final String PREFS_NAME = "Preferences";
    public static final String PREF_WIDTH = "width";
    public static final String PREF_HEIGHT = "height";
    public static final String PREF_RES_STRING = "resString";

    @Override
    public void create() {
        batch = new SpriteBatch();
        manager = new AssetManager();

        characterSpriteSheet = new Texture(CHARACTER_SPRITE_SHEET);

        playerAnimations = new AnimationHandler(characterSpriteSheet);

        this.setScreen(new LoadingScreen(this));

        background = new ParallaxBackground(new OrthographicCamera());

        loadPreferences();
        setResolutionOnStartup();
        Gdx.graphics.setForegroundFPS(60);
    }

    private void loadPreferences() {
        this.prefs = Gdx.app.getPreferences(PREFS_NAME);
    }

    private void setResolutionOnStartup() {
        int width = prefs.getInteger(PREF_WIDTH, 1280);
        int height = prefs.getInteger(PREF_HEIGHT, 720);

        Gdx.graphics.setWindowedMode(width, height);
    }

    @Override
    public void dispose() {
        if (batch != null) batch.dispose();
        if (manager != null) manager.dispose();
        if (font != null) font.dispose();
        if (skin != null) skin.dispose();
    }
}
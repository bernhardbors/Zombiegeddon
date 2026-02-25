package inf112.zoombiegeddon.app;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import inf112.zoombiegeddon.view.FontGenerator;
import inf112.zoombiegeddon.view.screens.LoadingScreen;
import inf112.zoombiegeddon.view.screens.MainMenuScreen;
import inf112.zoombiegeddon.view.screens.SplashScreen;

public class GameApp extends Game {

    public Skin skin;
    public SpriteBatch batch;
    public Preferences prefs;
    public FontGenerator font;
    public AssetManager manager;
    public Texture characterSpriteSheet;

    public static final String UI_SKIN = "ui/skin/uiskin.json";
    public static final String SPLASH_LOGO = "background/background1.png";
    public static final String CHAR_SHEET = "src/main/resources/assets/characterSprite32x32/MaleSkin1.png";
    public static final String FONT_PATH = "fonts/pixbobMiniLiteFont.ttf";

    @Override
    public void create() {
        batch = new SpriteBatch();
        manager = new AssetManager();

        this.setScreen(new LoadingScreen(this));

        loadPreferences();
        setResolutionOnStartup();
        Gdx.graphics.setForegroundFPS(60);
    }

    private void loadPreferences() {
        this.prefs = Gdx.app.getPreferences("Preferences");
    }

    private void setResolutionOnStartup() {
        int width = prefs.getInteger("width", 1280);
        int height = prefs.getInteger("height", 720);

        Gdx.graphics.setWindowedMode(width, height);
    }

    @Override
    public void dispose() {
        if (batch != null) batch.dispose();
        if (batch != null) manager.dispose();
    }
}
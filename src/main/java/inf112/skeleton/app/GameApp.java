package inf112.skeleton.app;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import inf112.skeleton.view.screens.MainMenuScreen;

public class GameApp extends Game {
    public SpriteBatch batch;
    public Skin skin;
    public Preferences prefs;

    public AssetManager manager;

    @Override
    public void create() {

        batch = new SpriteBatch();
        manager = new AssetManager();

        manager.load("ui/skin/uiskin.json", Skin.class);
        manager.finishLoading();

        this.skin = manager.get("ui/skin/uiskin.json", Skin.class);

        loadPreferences();
        setResolutionOnStartup();
        Gdx.graphics.setForegroundFPS(60);

        this.setScreen(new MainMenuScreen(this));
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
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        skin.dispose();
    }
}
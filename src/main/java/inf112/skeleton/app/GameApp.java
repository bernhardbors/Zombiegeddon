package inf112.skeleton.app;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import inf112.skeleton.view.MainMenuView;

public class GameApp extends Game {
    public SpriteBatch batch;
    public Skin skin;
    public Preferences prefs;

    @Override
    public void create() {

        skin = new Skin(Gdx.files.internal("ui/skin/uiskin.json"));
        batch = new SpriteBatch();

        loadPreferences();
        setResolutionOnStartup();

        Gdx.graphics.setForegroundFPS(60);

        this.setScreen(new MainMenuView(this));
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
package inf112.zoombiegeddon.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import inf112.zoombiegeddon.app.AssetConstants;
import inf112.zoombiegeddon.app.GameApp;
import inf112.zoombiegeddon.view.background.ParallaxBackground;

/**
 * Abstract base class for UI-based screens in the game.
 * Provides shared setup for stage, table layout, screen resizing,
 * and common text formatting utilities.
 */
public abstract class AbstractScreen implements IScreen {

    protected final Stage stage;
    protected final Skin skin;
    protected final GameApp app;
    protected final Table table;
    protected Preferences prefs;

    protected float buttonWidth;
    protected float buttonHeight;

    protected AssetManager assetManager;

    protected ParallaxBackground parallaxBg = new ParallaxBackground();

    /**
     * Constructs a new abstract screen with shared UI setup.
     *
     * @param app The game application instance.
     */
    protected AbstractScreen(final GameApp app) {
        this.app = app;

        this.skin = app.getAssets().getSkin();
        this.prefs = app.getPrefs();

        this.table = new Table();
        this.stage = new Stage(new ScreenViewport());

        this.assetManager = app.getAssets().getManager();

        table.setFillParent(true);
        stage.addActor(table);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        calculateButtonSize();
        buildScreen();
    }

    /**
     * Clears all actors from the main table.
     */
    protected void clearTable() {
        table.clear();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.DARK_GRAY);
        stage.act(Math.min(delta, 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);

        calculateButtonSize();

        table.invalidateHierarchy();
        buildScreen();
    }

    @Override
    public void pause() {
        Gdx.app.log("AbstractScreen", "pause() called");
    }

    @Override
    public void resume() {
        Gdx.app.log("AbstractScreen", "resume() called");
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    /**
     * Releases resources used by this screen.
     */
    public void dispose() {
        if (stage != null) {
            stage.dispose();
        }
    }

    private void calculateButtonSize(){
        this.buttonWidth = (float) Gdx.graphics.getWidth() / 7;
        this.buttonHeight = (float) Gdx.graphics.getHeight() / 18;
    }

    /**
     * Creates and adds formatted text to the screen table.
     *
     * @param text The text to display.
     * @param color The color of the text.
     * @param scale The base scale of the text.
     * @return The created label.
     */
    protected Label formatText(String text, Color color, float scale) {
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = app.getAssets().getFont();
        style.fontColor = color;

        float screenScale = Gdx.graphics.getHeight() / 1080f;

        Label textBody = new Label(text, style);
        textBody.setFontScale(scale * screenScale);
        table.add(textBody).row();

        return textBody;
    }

    /**
     * Creates a default parallax background for menu-style screens.
     *
     * @param bgCamera the camera used to render the background
     * @return a parallax background with the default menu layers
     */
    protected ParallaxBackground addParallaxBg(OrthographicCamera bgCamera) {
        final ParallaxBackground parallaxBg;
        parallaxBg = new ParallaxBackground(bgCamera);
        parallaxBg.addLayer(assetManager.get(AssetConstants.BG_SKY, Texture.class), 0.3f);
        parallaxBg.addLayer(assetManager.get(AssetConstants.BG_CITY, Texture.class), 0.6f);
        parallaxBg.addLayer(assetManager.get(AssetConstants.BG_L1, Texture.class), 1.0f);
        parallaxBg.addLayer(assetManager.get(AssetConstants.BG_L2, Texture.class), 1.5f);
        return parallaxBg;
    }
}

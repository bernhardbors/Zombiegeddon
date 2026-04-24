package inf112.zoombiegeddon.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import inf112.zoombiegeddon.app.GameApp;
import inf112.zoombiegeddon.audio.MusicTrack;
import inf112.zoombiegeddon.controller.LevelController;
import inf112.zoombiegeddon.controller.PlayerController;
import inf112.zoombiegeddon.events.GameEvent;
import inf112.zoombiegeddon.events.GameEventBus;
import inf112.zoombiegeddon.events.IEventListener;
import inf112.zoombiegeddon.model.levels.LevelModel;
import inf112.zoombiegeddon.view.renderer.EntityRenderer;
import inf112.zoombiegeddon.view.renderer.LevelRenderer;

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

    /**
     * Constructs a new abstract screen with shared UI setup.
     *
     * @param app The game application instance.
     */
    AbstractScreen(final GameApp app) {

        this.app = app;

        this.skin = app.getAssets().getSkin();
        this.prefs = app.getPrefs();

        this.table = new Table();
        this.stage = new Stage(new ScreenViewport());


        table.setFillParent(true);
        stage.addActor(table);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        calculateButtonSize();
        buildScreen();
    }


    public abstract void buildScreen();

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
        style.font = app.getAssets().getFont();;
        style.fontColor = color;

        float screenScale = Gdx.graphics.getHeight() / 1080f;

        Label textBody = new Label(text, style);
        textBody.setFontScale(scale * screenScale);
        table.add(textBody).row();

        return textBody;
    }
}

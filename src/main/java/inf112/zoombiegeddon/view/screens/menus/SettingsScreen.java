package inf112.zoombiegeddon.view.screens.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import inf112.zoombiegeddon.app.AssetConstants;
import inf112.zoombiegeddon.app.GameApp;
import inf112.zoombiegeddon.controller.ISettingsController;
import inf112.zoombiegeddon.controller.SettingsController;
import inf112.zoombiegeddon.events.GameEvent;
import inf112.zoombiegeddon.events.GameEventBus;
import inf112.zoombiegeddon.view.utils.UIUtils;
import inf112.zoombiegeddon.view.screens.AbstractScreen;

/**
 * Displays the settings screen where the player can adjust game configuration.
 * Supports resolution changes and audio volume settings.
 */
public class SettingsScreen extends AbstractScreen {

    private static final float SCROLL_SPEED = 2f;

    private final SpriteBatch bgBatch;
    private final OrthographicCamera bgCamera;
    private final Screen returnScreen;
    private final ISettingsController controller;

    /**
     * Constructs the settings screen.
     *
     * @param app          the game application instance
     * @param returnScreen the screen to return to when pressing back
     */
    public SettingsScreen(final GameApp app, Screen returnScreen) {
        super(app);
        this.returnScreen = returnScreen;
        this.controller = new SettingsController(app);
        bgBatch = new SpriteBatch();
        bgCamera = new OrthographicCamera();
        bgCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        bgCamera.position.x += SCROLL_SPEED * delta;
        bgCamera.update();
        bgBatch.setProjectionMatrix(bgCamera.combined);
        bgBatch.begin();
        parallaxBg.render(bgBatch);
        bgBatch.end();
        stage.act(Math.min(delta, 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        bgCamera.setToOrtho(false, width, height);
        bgCamera.update();
        super.resize(width, height);
    }

    @Override
    public void dispose() {
        super.dispose();
        bgBatch.dispose();
    }

    @Override
    public void buildScreen() {
        clearTable();
        parallaxBg = addParallaxBg(bgCamera);
        table.top();

        GameEventBus.post(GameEvent.MENU_STARTED);

        formatText("SETTINGS", Color.GOLD, 3.0f);

        float contentWidth = Math.max(buttonWidth * 1.5f, Gdx.graphics.getWidth() * 0.45f);
        float screenScale = Gdx.graphics.getHeight() / 1080f;

        Table contentTable = new Table();
        contentTable.defaults().left().pad(4f * screenScale);

        Table displaySection = createSectionTable();
        addSectionTitle(displaySection, "VIDEO SETTINGS", Color.CYAN);

        String[] resolutions = {"1280 x 720", "1366 x 768", "1600 x 900", "1920 x 1080"};

        UIUtils.selectBox("Resolution", resolutions, app.getAssets().getSkin()).size(buttonWidth, buttonHeight)
                .action(controller::applyResolution).addTo(displaySection);

        UIUtils.checkBox("Fullscreen", app.getAssets().getSkin())
                .checked(app.getPrefs().getBoolean(AssetConstants.PREF_FULLSCREEN, false))
                .action(controller::toggleFullscreen).addTo(displaySection);

        Table audioSection = createSectionTable();
        addSectionTitle(audioSection, "AUDIO SETTINGS", Color.ORANGE);

        UIUtils.slider("Music Volume", app.getAssets().getSkin()).value(app.getAudio().getMusicVolume())
                .range(0f, 0.3f, 0.01f).action(controller::updateMusicVolume).addTo(audioSection);

        UIUtils.slider("SFX Volume", app.getAssets().getSkin()).value(app.getAudio().getSfxVolume())
                .range(0f, 1f, 0.01f).action(controller::updateSfxVolume).addTo(audioSection);

        contentTable.add(displaySection).growX().row();
        contentTable.add(audioSection).growX().row();

        table.add(contentTable).width(contentWidth).expand().center().row();

        UIUtils.button("BACK", app.getAssets().getSkin()).size(buttonWidth, buttonHeight)
                .action(() -> app.setScreen(returnScreen)).addTo(table);
    }

    private Table createSectionTable() {
        float screenScale = Gdx.graphics.getHeight() / 1080f;
        Table section = new Table();
        section.defaults().pad(2f * screenScale);
        return section;
    }

    private void addSectionTitle(Table section, String title, Color color) {
        Label.LabelStyle style = new Label.LabelStyle(app.getAssets().getFont(), color);
        Label sectionTitle = new Label(title, style);

        float screenScale = Gdx.graphics.getHeight() / 1080f;
        sectionTitle.setFontScale(1.6f * screenScale);

        section.add(sectionTitle).colspan(2).left().padTop(5f * screenScale).padBottom(15f * screenScale).row();
    }
}
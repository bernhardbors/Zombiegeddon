package inf112.zoombiegeddon.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import inf112.zoombiegeddon.app.AssetConstants;
import inf112.zoombiegeddon.app.GameApp;
import inf112.zoombiegeddon.audio.MusicTrack;
import inf112.zoombiegeddon.utils.UIUtils;

/**
 * Displays the settings screen where the player can adjust game configuration.
 * Supports resolution changes and audio volume settings.
 */
public class SettingsScreen extends AbstractScreen {

    private final Screen returnScreen;

    /**
     * Constructs the settings screen.
     *
     * @param app The game application instance.
     */
    public SettingsScreen(final GameApp app, Screen returnScreen) {
        super(app);
        this.returnScreen = returnScreen;
    }

    @Override
    public void buildScreen() {
        clearTable();
        app.audio.playMusic(MusicTrack.MENU, true);

        table.defaults().pad(4f);
        formatText("SETTINGS", Color.WHITE, 2f);
        table.row().padTop(18f);

        float contentWidth = Math.max(buttonWidth * 1.5f, Gdx.graphics.getWidth() * 0.45f);

        Table contentTable = new Table();
        contentTable.defaults().left().pad(4f);


        // --- DISPLAY SECTION ---
        Table displaySection = createSectionTable();
        addSectionTitle(displaySection, "Display");

        String[] resolutions = {"1280 x 720", "1366 x 768", "1600 x 900", "1920 x 1080", "2560 x 1440", "3840 x 2160"};
        String savedRes = app.prefs.getString(AssetConstants.PREF_RES_STRING, resolutions[0]);

        UIUtils.addSelectBox("Resolution", resolutions, app.skin, displaySection,
                buttonWidth, buttonHeight, this::changeResolution);

        UIUtils.addCheckBox("Fullscreen", app.skin, displaySection,
                app.prefs.getBoolean(AssetConstants.PREF_FULLSCREEN, false), this::setFullscreen);

        // --- AUDIO SECTION ---
        Table audioSection = createSectionTable();
        addSectionTitle(audioSection, "Audio");

        UIUtils.addSlider("Music Volume", app.skin, audioSection,
                app.audio.getMusicVolume(), 0f, 1f, 0.01f, app.audio::setMusicVolume);

        UIUtils.addSlider("SFX Volume", app.skin, audioSection,
                app.audio.getSfxVolume(), 0f, 1f, 0.01f, app.audio::setSfxVolume);

        // --- ASSEMBLE ---
        contentTable.add(displaySection).growX().row();
        contentTable.row().padTop(20f);
        contentTable.add(audioSection).growX().row();

        table.add(contentTable).width(contentWidth).expand().center().row();

        UIUtils.addButton("BACK", app.skin, table, buttonWidth, buttonHeight,
                () -> app.setScreen(returnScreen));
    }

    private Table createSectionTable() {
        Table section = new Table();
        section.defaults().left().fillX().pad(4f);
        return section;
    }

    private void addSectionTitle(Table section, String title) {
        Label.LabelStyle style = new Label.LabelStyle(app.font, Color.WHITE);
        Label sectionTitle = new Label(title, style);
        sectionTitle.setFontScale(1.6f * (Gdx.graphics.getHeight() / 1080f));
        section.add(sectionTitle).colspan(2).left().padTop(2f).padBottom(15f).row();
    }

    /**
     * Changes the game resolution and stores the selected value in preferences.
     *
     * @param resolution The resolution string to apply, for example {@code "1920 x 1080"}.
     */
    public void changeResolution(String resolution) {
        try {
            String[] parts = resolution.split("x");
            int width = Integer.parseInt(parts[0].trim());
            int height = Integer.parseInt(parts[1].trim());

            app.prefs.putInteger(AssetConstants.PREF_WIDTH, width);
            app.prefs.putInteger(AssetConstants.PREF_HEIGHT, height);
            app.prefs.putString(AssetConstants.PREF_RES_STRING, resolution);
            app.prefs.flush();

            if (!app.prefs.getBoolean(AssetConstants.PREF_FULLSCREEN, false)) {
                Gdx.graphics.setWindowedMode(width, height);

                stage.getViewport().update(width, height, true);

                table.setFillParent(true);
                table.invalidateHierarchy();
            }
        } catch (Exception e) {
            Gdx.app.error("Settings", "Resolution change failed: " + resolution);
        }
    }

    private void setFullscreen(boolean fullscreen) {
        app.prefs.putBoolean(AssetConstants.PREF_FULLSCREEN, fullscreen);
        app.prefs.flush();

        if (fullscreen) {
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        } else {
            int width = app.prefs.getInteger(AssetConstants.PREF_WIDTH, 1280);
            int height = app.prefs.getInteger(AssetConstants.PREF_HEIGHT, 720);
            Gdx.graphics.setWindowedMode(width, height);
        }

        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        table.invalidateHierarchy();
    }


}
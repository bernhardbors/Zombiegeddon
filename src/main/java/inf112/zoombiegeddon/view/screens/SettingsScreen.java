package inf112.zoombiegeddon.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import inf112.zoombiegeddon.app.AssetConstants;
import inf112.zoombiegeddon.app.GameApp;
import inf112.zoombiegeddon.audio.MusicTrack;
import inf112.zoombiegeddon.controller.SettingsController;
import inf112.zoombiegeddon.utils.UIUtils;

/**
 * Displays the settings screen where the player can adjust game configuration.
 * Supports resolution changes and audio volume settings.
 */
public class SettingsScreen extends AbstractScreen {

    private final Screen returnScreen;
    private final SettingsController controller;

    /**
     * Constructs the settings screen.
     *
     * @param app The game application instance.
     */
    public SettingsScreen(final GameApp app, Screen returnScreen) {
        super(app);
        this.returnScreen = returnScreen;
        this.controller = new SettingsController(app);
    }

    @Override
    public void buildScreen() {
        if (app.getAssets().getManager().isLoaded(AssetConstants.BG_SKY)) {
            Texture background = app.getAssets().getManager().get(AssetConstants.BG_SKY, Texture.class);
            table.setBackground(new TextureRegionDrawable(background));
        }

        clearTable();
        table.top();

        app.getAudio().playMusic(MusicTrack.MENU, true);

        formatText("SETTINGS", Color.GOLD, 3.0f);

        float contentWidth = Math.max(buttonWidth * 1.5f, Gdx.graphics.getWidth() * 0.45f);

        Table contentTable = new Table();
        contentTable.defaults().left().pad(4f);

        Table displaySection = createSectionTable();
        addSectionTitle(displaySection, "VIDEO SETTINGS", Color.CYAN);

        String[] resolutions = {"1280 x 720", "1366 x 768", "1600 x 900", "1920 x 1080", "2560 x 1440", "3840 x 2160"};

        UIUtils.addSelectBox("Resolution", resolutions, app.getAssets().getSkin(), displaySection,
                buttonWidth, buttonHeight, controller::applyResolution);

        UIUtils.addCheckBox("Fullscreen", app.getAssets().getSkin(), displaySection,
                app.getPrefs().getBoolean(AssetConstants.PREF_FULLSCREEN, false), controller::toggleFullscreen);

        Table audioSection = createSectionTable();
        addSectionTitle(audioSection, "AUDIO SETTINGS", Color.ORANGE);

        UIUtils.addSlider("Music Volume", app.getAssets().getSkin(), audioSection,
                app.getAudio().getMusicVolume(), 0f, 1f, 0.01f, controller::updateMusicVolume);

        UIUtils.addSlider("SFX Volume", app.getAssets().getSkin(), audioSection,
                app.getAudio().getSfxVolume(), 0f, 1f, 0.01f, controller::updateSfxVolume);

        contentTable.add(displaySection).growX().row();
        contentTable.add(audioSection).growX().row();

        table.add(contentTable).width(contentWidth).expand().center().row();

        UIUtils.addButton("BACK", app.getAssets().getSkin(), table, buttonWidth, buttonHeight,
                () -> app.setScreen(returnScreen));
    }

    private Table createSectionTable() {
        Table section = new Table();
        section.defaults().pad(2f);
        return section;
    }

    private void addSectionTitle(Table section, String title, Color color) {
        Label.LabelStyle style = new Label.LabelStyle(app.getAssets().getFont(), color);
        Label sectionTitle = new Label(title, style);

        float fontScale = 1.6f * (Gdx.graphics.getHeight() / 1080f);
        sectionTitle.setFontScale(fontScale);

        section.add(sectionTitle).colspan(2).left().padTop(5f).padBottom(15f).row();
    }
}
package inf112.zoombiegeddon.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
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

        app.getAudio().playMusic(MusicTrack.MENU, true);

        if (app.getAssets().getManager().isLoaded(AssetConstants.BG_GAME)) {
            Texture background = app.getAssets().getManager().get(AssetConstants.BG_GAME, Texture.class);
            table.setBackground(new TextureRegionDrawable(background));
        }
        formatText("SETTINGS", Color.WHITE, 2f);
        table.row().pad(20);


        String[] resolutions = {"1280 x 720", "1366 x 768", "1600 x 900", "1920 x 1080", "2560 x 1440", "3840 x 2160"};


        String savedRes = app.getPrefs().getString(AssetConstants.PREF_RES_STRING, resolutions[0]);


        UIUtils.addSelectBox("Resolutions:", resolutions, app.getAssets().getSkin(), table, buttonWidth, buttonHeight, this::changeResolution)
                .setSelected(savedRes);

        table.row().padTop(50);

        formatText("Music Volume", Color.WHITE, 1.2f);

        Slider musicSlider = new Slider(0f, 1f, 0.01f, false, app.getAssets().getSkin());
        musicSlider.setValue(app.getAudio().getMusicVolume());

        musicSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                app.getAudio().setMusicVolume(musicSlider.getValue());
            }
        });

        table.add(musicSlider).width(buttonWidth).row();
        table.row().padTop(30);

        table.row().padTop(30);

        formatText("SFX Volume", Color.WHITE, 1.2f);

        Slider sfxSlider = new Slider(0f, 1f, 0.01f, false, app.getAssets().getSkin());
        sfxSlider.setValue(app.getAudio().getSfxVolume());

        sfxSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                app.getAudio().setSfxVolume(sfxSlider.getValue());
            }
        });

        table.add(sfxSlider).width(buttonWidth).row();
        table.row().padTop(30);

        UIUtils.addButton("Back", app.getAssets().getSkin(), table, buttonWidth, buttonHeight,
                () -> app.setScreen(returnScreen));
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

            Gdx.graphics.setWindowedMode(width, height);

            app.getPrefs().putInteger(AssetConstants.PREF_WIDTH, width);
            app.getPrefs().putInteger(AssetConstants.PREF_HEIGHT, height);
            app.getPrefs().putString(AssetConstants.PREF_RES_STRING, resolution);
            app.getPrefs().flush();

            stage.getViewport().update(width, height, true);

            table.invalidateHierarchy();

        } catch (Exception e) {
            Gdx.app.error("Settings", "Kunne ikke endre oppløsning: " + resolution);
        }
    }


}
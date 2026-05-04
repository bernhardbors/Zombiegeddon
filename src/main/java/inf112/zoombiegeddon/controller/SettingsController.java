package inf112.zoombiegeddon.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import inf112.zoombiegeddon.app.AssetConstants;
import inf112.zoombiegeddon.app.GameApp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller responsible for applying and persisting game settings,
 * including resolution, fullscreen mode, and audio volumes.
 */
public class SettingsController implements ISettingsController {
    private final GameApp app;
    private final Preferences prefs;

    private static final Logger logger = LoggerFactory.getLogger(SettingsController.class);

    /**
     * Creates a settings controller for the given game application.
     *
     * @param app the game application whose settings should be controlled
     */
    public SettingsController(GameApp app) {
        this.app = app;
        this.prefs = app.getPrefs();
    }

    @Override
    public void applyResolution(String resolution) {
        try {
            String[] parts = resolution.split("x");
            int width = Integer.parseInt(parts[0].trim());
            int height = Integer.parseInt(parts[1].trim());

            prefs.putInteger(AssetConstants.PREF_WIDTH, width);
            prefs.putInteger(AssetConstants.PREF_HEIGHT, height);
            prefs.putString(AssetConstants.PREF_RES_STRING, resolution);
            prefs.flush();

            if (!prefs.getBoolean(AssetConstants.PREF_FULLSCREEN, false)) {
                Gdx.graphics.setWindowedMode(width, height);
            }
        } catch (Exception e){
            logger.error("Failed to apply resolution", e);
        }
    }

    @Override
    public void toggleFullscreen(boolean enabled) {
        prefs.putBoolean(AssetConstants.PREF_FULLSCREEN, enabled);
        prefs.flush();

        if (enabled) {
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        } else {
            int width = prefs.getInteger(AssetConstants.PREF_WIDTH, 1280);
            int height = prefs.getInteger(AssetConstants.PREF_HEIGHT, 720);
            Gdx.graphics.setWindowedMode(width, height);
        }
    }

    @Override
    public void updateMusicVolume(float volume) {
        app.getAudio().setMusicVolume(volume);
    }

    @Override
    public void updateSfxVolume(float volume) {
        app.getAudio().setSfxVolume(volume);
    }
}

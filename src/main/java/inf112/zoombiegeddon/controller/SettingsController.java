package inf112.zoombiegeddon.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import inf112.zoombiegeddon.app.AssetConstants;
import inf112.zoombiegeddon.app.GameApp;

public class SettingsController {
    private final GameApp app;
    private final Preferences prefs;

    public SettingsController(GameApp app) {
        this.app = app;
        this.prefs = app.getPrefs();
    }

    /**
     * Changes the game resolution and stores the selected value in preferences.
     *
     * @param resolution The resolution string to apply, for example {@code "1920 x 1080"}.
     */
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
        } catch (Exception e) {
            Gdx.app.error("Settings", "Resolution change failed: " + resolution);
        }
    }

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

    public void updateMusicVolume(float volume) {
        app.getAudio().setMusicVolume(volume);
    }

    public void updateSfxVolume(float volume) {
        app.getAudio().setSfxVolume(volume);
    }
}

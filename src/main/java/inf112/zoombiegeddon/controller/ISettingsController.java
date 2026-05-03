package inf112.zoombiegeddon.controller;

/**
 * Defines operations for applying and persisting game settings.
 */
public interface ISettingsController {

    /**
     * Changes the game resolution and stores the selected value in preferences.
     *
     * @param resolution the resolution string to apply, e.g. {@code "1920 x 1080"}
     */
    void applyResolution(String resolution);

    /**
     * Toggles fullscreen mode and persists the setting.
     *
     * @param enabled {@code true} to enable fullscreen, {@code false} for windowed mode
     */
    void toggleFullscreen(boolean enabled);

    /**
     * Updates the background music volume.
     *
     * @param volume the desired volume level
     */
    void updateMusicVolume(float volume);

    /**
     * Updates the sound effects volume.
     *
     * @param volume the desired volume level
     */
    void updateSfxVolume(float volume);
}

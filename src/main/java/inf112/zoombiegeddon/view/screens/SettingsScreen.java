package inf112.zoombiegeddon.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import inf112.zoombiegeddon.app.GameApp;
import inf112.zoombiegeddon.utils.UIUtils;

/**
 * The SettingsScreen allows the player to adjust game configurations
 * such as volume, resolution, and difficulty.
 */
public class SettingsScreen extends AbstractScreen {

    /**
     * Constructor for the SettingsScreen.
     *
     * @param app The main application instance used to switch screens and access assets.
     */
    public SettingsScreen(final GameApp app) {
        super(app);
    }

    /**
     * Builds the settings UI layout.
     * Includes a title, a resolution selection box, and a return button.
     */
    @Override
    public void buildScreen() {
        super.buildScreen();

        // Title
        formatText("SETTINGS", Color.WHITE, 2f);
        table.row().pad(20);

        // Resolution options
        String[] resolutions = {"1280 x 720", "1366 x 768", "1600 x 900", "1920 x 1080", "2560 x 1440", "3840 x 2160"};
        String savedRes = prefs.getString(GameApp.PREF_RES_STRING, resolutions[0]);

        // Selection Box for resolutions
        UIUtils.addSelectBox("Resolutions:", resolutions, skin, table, buttonWidth, buttonHeight, this::changeResolution)
                .setSelected(savedRes);

        // Spacing and Back Button
        table.row().padTop(50);
        UIUtils.addButton("Back", skin, table, buttonWidth, buttonHeight,
                () -> app.setScreen(new MainMenuScreen(app)));
    }

    public void changeResolution(String resolution) {
        String[] parts = resolution.split("x");
        int width = Integer.parseInt(parts[0].trim());
        int height = Integer.parseInt(parts[1].trim());

        Gdx.graphics.setWindowedMode(width, height);

        prefs.putInteger(GameApp.PREF_WIDTH, width);
        prefs.putInteger(GameApp.PREF_HEIGHT, height);
        prefs.putString(GameApp.PREF_RES_STRING, resolution);
        prefs.flush();

        stage.getViewport().update(width, height, true);
        table.invalidateHierarchy();

    }
}
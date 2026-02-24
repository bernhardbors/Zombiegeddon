package inf112.zoombiegeddon.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import inf112.zoombiegeddon.app.GameApp;
import inf112.zoombiegeddon.utils.UIUtils;

public class SettingsScreen extends AbstractScreen {

    public SettingsScreen(final GameApp app) {
        super(app);
    }

    @Override
    public void buildScreen() {
        super.buildScreen();
        table.add();
        DrawHeading("SETTINGS", Color.WHITE, 4);

        String[] resolutions = {
                "1280 x 720", "1366 x 768", "1600 x 900",
                "1920 x 1080", "2560 x 1440", "3840 x 2160"
        };

        String savedRes = prefs.getString("resString", resolutions[0]);

        UIUtils.addSelectBox("Resolutions:", resolutions, skin, table, buttonWidth, buttonHeight, this::changeResolution)
                .setSelected(savedRes);

        table.add();

        UIUtils.addButton("Back", skin, table, buttonWidth, buttonHeight, () -> app.setScreen(new MainMenuScreen(app))).row();
    }

    public void changeResolution(String resolution) {
        String[] parts = resolution.split("x");
        int width = Integer.parseInt(parts[0].trim());
        int height = Integer.parseInt(parts[1].trim());

        Gdx.graphics.setWindowedMode(width, height);

        prefs.putInteger("width", width);
        prefs.putInteger("height", height);
        prefs.putString("resString", resolution);
        prefs.flush();

        stage.getViewport().update(width, height, true);
    }
}
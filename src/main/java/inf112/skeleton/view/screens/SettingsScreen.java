package inf112.skeleton.view.screens;

import com.badlogic.gdx.Gdx;
import inf112.skeleton.app.GameApp;
import inf112.skeleton.utils.UIUtils;

public class SettingsScreen extends AbstractMenuScreen {

    public SettingsScreen(final GameApp app) {
        super(app);

        float buttonWidth = (float) Gdx.graphics.getWidth() / 5;
        float buttonHeight = (float) Gdx.graphics.getHeight() / 15;

        String[] resolutions = {
                "1280 x 720", "1366 x 768", "1600 x 900",
                "1920 x 1080", "2560 x 1440", "3840 x 2160"
        };

        String savedRes = prefs.getString("resString", resolutions[0]);

        UIUtils.addSelectBox("Resolutions:", resolutions, skin, table, buttonWidth, buttonHeight, this::changeResolution)
                .setSelected(savedRes);

        UIUtils.addButton("Back", skin, table, buttonWidth, buttonHeight, () ->
                app.setScreen(new MainMenuScreen(app))
        );
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
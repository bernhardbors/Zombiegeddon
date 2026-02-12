package inf112.skeleton.view.screens;

import com.badlogic.gdx.Gdx;
import inf112.skeleton.app.GameApp;
import inf112.skeleton.utils.UIUtils;

public class MainMenuScreen extends AbstractMenuScreen {

    public MainMenuScreen(final GameApp app) {
        super(app);

        float buttonWidth = (float) Gdx.graphics.getWidth() / 5;
        float buttonHeight = (float) Gdx.graphics.getHeight() / 15;

        UIUtils.addButton("Start", skin, table, buttonWidth, buttonHeight, () -> {});

        UIUtils.addButton("How To Play", skin, table, buttonWidth, buttonHeight, () -> {});

        UIUtils.addButton("Settings", skin, table, buttonWidth, buttonHeight, () -> app.setScreen(new SettingsScreen(app)));

        UIUtils.addButton("Exit", skin, table, buttonWidth, buttonHeight, () -> Gdx.app.exit());
    }
}
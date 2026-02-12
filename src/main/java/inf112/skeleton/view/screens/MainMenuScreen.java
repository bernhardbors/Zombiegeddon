package inf112.skeleton.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import inf112.skeleton.app.GameApp;
import inf112.skeleton.utils.UIUtils;

public class MainMenuScreen extends AbstractMenuScreen {

    public MainMenuScreen(final GameApp app) {
        super(app);

    }

    @Override
    public void buildScreen() {
        super.buildScreen();

        DrawHeading("MAIN MENU", Color.WHITE, 4);

        UIUtils.addButton("Start", skin, table, buttonWidth, buttonHeight, () -> {});

        UIUtils.addButton("How To Play", skin, table, buttonWidth, buttonHeight, () -> app.setScreen(new HowToScreen(app)));

        UIUtils.addButton("Settings", skin, table, buttonWidth, buttonHeight, () -> app.setScreen(new SettingsScreen(app)));

        UIUtils.addButton("Exit", skin, table, buttonWidth, buttonHeight, () -> Gdx.app.exit());

    }
}
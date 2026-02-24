package inf112.zoombiegeddon.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import inf112.zoombiegeddon.app.GameApp;
import inf112.zoombiegeddon.utils.UIUtils;
import inf112.zoombiegeddon.view.levels.TestLevel01;

public class MainMenuScreen extends AbstractMenuScreen {

    private Texture background;

    public MainMenuScreen(final GameApp app) {
        super(app);
        this.background = new Texture("background/background1.png");
    }

    @Override
    public void buildScreen() {
        super.buildScreen();

        table.setBackground(new TextureRegionDrawable(background));

        DrawHeading("MAIN MENU", Color.WHITE, 4);

        UIUtils.addButton("Start", skin, table, buttonWidth, buttonHeight, () -> app.setScreen(new TestLevel01(app, new Vector2(0,-9.81f))));

        UIUtils.addButton("How To Play", skin, table, buttonWidth, buttonHeight, () -> app.setScreen(new HowToScreen(app)));

        UIUtils.addButton("Settings", skin, table, buttonWidth, buttonHeight, () -> app.setScreen(new SettingsScreen(app)));

        UIUtils.addButton("Exit", skin, table, buttonWidth, buttonHeight, () -> Gdx.app.exit());

    }
    @Override
    public void dispose() {
        super.dispose();
        if (background != null) {
            background.dispose();
        }
    }
}
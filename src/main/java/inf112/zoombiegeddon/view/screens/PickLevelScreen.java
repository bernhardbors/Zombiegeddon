package inf112.zoombiegeddon.view.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import inf112.zoombiegeddon.app.AssetConstants;
import inf112.zoombiegeddon.app.GameApp;
import inf112.zoombiegeddon.utils.UIUtils;
import inf112.zoombiegeddon.view.levels.Level1;
import inf112.zoombiegeddon.view.levels.Level2;

/**
 * Screen that lets you pick level when you press start.
 */
public class PickLevelScreen extends AbstractScreen{


    public PickLevelScreen(GameApp app) {super(app);}

    @Override
    public void buildScreen() {
        clearTable();

        if (app.manager.isLoaded(AssetConstants.BG_SKY)) {
            Texture background = app.manager.get(AssetConstants.BG_SKY, Texture.class);
            table.setBackground(new TextureRegionDrawable(background));
        }

        formatText("PICK LEVEL", Color.WHITE, 6f);
        UIUtils.addButton("Level 1", skin, table, buttonWidth, buttonHeight, () -> app.setScreen(new Level1(app)));
        UIUtils.addButton("Level 2", skin, table, buttonWidth, buttonHeight, () -> app.setScreen(new Level2(app)));
        UIUtils.addButton("Back", skin, table, buttonWidth, buttonHeight, () -> app.setScreen(new MainMenuScreen(app)));




    }
}

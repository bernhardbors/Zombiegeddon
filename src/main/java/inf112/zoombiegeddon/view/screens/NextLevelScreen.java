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
 * Displays the screen shown when the player reaches the goal.
 * Allows the player to choose the next level or return to the main menu.
 */
public class NextLevelScreen extends AbstractScreen{


    /**
     * Constructs the next level screen.
     *
     * @param app The game application instance.
     */
    public NextLevelScreen(GameApp app) {super(app); }

    @Override
    public void buildScreen() {
        clearTable();

        if (app.getAssets().getManager().isLoaded(AssetConstants.BG_SKY)) {
            Texture background = app.getAssets().getManager().get(AssetConstants.BG_SKY, Texture.class);
            table.setBackground(new TextureRegionDrawable(background));


        }

        formatText("PICK NEXT LEVEL", Color.WHITE, 6f);
        UIUtils.addButton("Level 1", skin, table, buttonWidth, buttonHeight, () -> app.setScreen(new Level1(app)));
        UIUtils.addButton("Level 2", skin, table, buttonWidth, buttonHeight, () -> app.setScreen(new Level2(app)));
        UIUtils.addButton("Main menu", skin, table, buttonWidth, buttonHeight, () -> app.setScreen(new MainMenuScreen(app)));

    }
}

package inf112.zoombiegeddon.view.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import inf112.zoombiegeddon.app.AssetConstants;
import inf112.zoombiegeddon.app.GameApp;
import inf112.zoombiegeddon.utils.UIUtils;
import inf112.zoombiegeddon.view.levels.Level0;

/**
 * Creates the gameoverscreen in the game when the player dies.
 * Either you can respawn or go back to the main menu.
 */
public class GameOverScreen extends AbstractScreen {

    public GameOverScreen(GameApp app) {
        super(app);
    }

    @Override
    public void buildScreen() {
        clearTable();

        if (app.manager.isLoaded(AssetConstants.BG_SKY)) {
            Texture background = app.manager.get(AssetConstants.BG_SKY, Texture.class);
            table.setBackground(new TextureRegionDrawable(background));
        }

        formatText("GAME OVER", Color.WHITE, 6f);

        UIUtils.addButton("Respawn", skin, table, buttonWidth, buttonHeight, () -> app.setScreen(new Level0(app)));
        UIUtils.addButton("MainScreen", skin, table, buttonWidth, buttonHeight, () -> app.setScreen(new MainMenuScreen(app)));
    }
}


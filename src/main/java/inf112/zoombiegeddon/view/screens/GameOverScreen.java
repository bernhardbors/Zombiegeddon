package inf112.zoombiegeddon.view.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import inf112.zoombiegeddon.app.AssetConstants;
import inf112.zoombiegeddon.app.GameApp;
import inf112.zoombiegeddon.audio.MusicTrack;
import inf112.zoombiegeddon.utils.UIUtils;
import inf112.zoombiegeddon.view.levels.Level1;
import inf112.zoombiegeddon.view.levels.Level2;

/**
 * Displays the game over screen when the player dies.
 * Allows the player to respawn or return to the main menu.
 */
public class GameOverScreen extends AbstractScreen {

    /**
     * Constructs the game over screen.
     *
     * @param app The game application instance.
     */
    public GameOverScreen(GameApp app) {
        super(app);
    }

    @Override
    public void buildScreen() {
        clearTable();

        app.audio.playMusic(MusicTrack.MENU, true);

        if (app.manager.isLoaded(AssetConstants.BG_SKY)) {
            Texture background = app.manager.get(AssetConstants.BG_SKY, Texture.class);
            table.setBackground(new TextureRegionDrawable(background));
        }

        formatText("GAME OVER", Color.WHITE, 6f);



        UIUtils.addButton("Respawn level 1", skin, table, buttonWidth, buttonHeight, () -> app.setScreen(new Level1(app)));
        UIUtils.addButton("Respawn level 2", skin, table, buttonWidth, buttonHeight, () -> app.setScreen(new Level2(app)));

        UIUtils.addButton("MainScreen", skin, table, buttonWidth, buttonHeight, () -> app.setScreen(new MainMenuScreen(app)));
    }
}


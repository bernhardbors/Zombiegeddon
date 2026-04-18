package inf112.zoombiegeddon.view.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import inf112.zoombiegeddon.app.AssetConstants;
import inf112.zoombiegeddon.app.GameApp;
import inf112.zoombiegeddon.audio.MusicTrack;
import inf112.zoombiegeddon.utils.UIUtils;

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

        app.getAudio().playMusic(MusicTrack.MENU, true);

        if (app.getAssets().getManager().isLoaded(AssetConstants.BG_SKY)) {
            Texture background = app.getAssets().getManager().get(AssetConstants.BG_SKY, Texture.class);
            table.setBackground(new TextureRegionDrawable(background));
        }

        formatText("GAME OVER", Color.WHITE, 6f);

        UIUtils.addButton("MainScreen", skin, table, buttonWidth, buttonHeight, () -> {
            app.getStoryManager().finishRun();
            app.setScreen(new MainMenuScreen(app));
        });
    }
}


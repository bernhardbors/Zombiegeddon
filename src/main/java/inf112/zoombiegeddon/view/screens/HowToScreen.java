package inf112.zoombiegeddon.view.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import inf112.zoombiegeddon.app.AssetConstants;
import inf112.zoombiegeddon.app.GameApp;
import inf112.zoombiegeddon.audio.MusicTrack;
import inf112.zoombiegeddon.utils.UIUtils;

/**
 * Displays instructions for how to play the game.
 */
public class HowToScreen extends AbstractScreen {

    /**
     * Constructs the how-to-play screen.
     *
     * @param app The game application instance.
     */
    public HowToScreen(GameApp app) {
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

        formatText("HOW TO PLAY", Color.WHITE, 2f);

        formatText(
                "Move: W, A, S, D or Arrow Keys\n" +
                "Jump: Space or Up\n" +
                "Sprint: Shift\n" +
                        "Pause: P\n" +
                "Quit: Escape",
                Color.WHITE, 1.5f);

        UIUtils.addButton("Back", skin, table, buttonWidth, buttonHeight, () -> app.setScreen(new MainMenuScreen(app))).row();
    }
}

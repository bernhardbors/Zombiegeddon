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

        app.getAudio().playMusic(MusicTrack.MENU, true);

        if (app.getAssets().getManager().isLoaded(AssetConstants.BG_SKY)) {
            Texture background = app.getAssets().getManager().get(AssetConstants.BG_SKY, Texture.class);
            table.setBackground(new TextureRegionDrawable(background));
        }

        // Title
        formatText("HOW TO PLAY", Color.GOLD, 2.5f);
        table.row().padTop(20);

        // --- Controls Section ---
        formatText("CONTROLS", Color.ORANGE, 1.5f);
        formatText(
                "Move: W, A, S, D / Arrows\n" +
                        "Jump: Space / Up\n" +
                        "Sprint: Left Shift\n" +
                        "Pause: P  |  Quit: ESC",
                Color.WHITE, 1.2f);

        table.row().padTop(30);

        // --- Game Modes Section ---
        formatText("GAME MODES", Color.ORANGE, 1.5f);

        // Story Mode Description
        formatText("STORY MODE", Color.CYAN, 1.2f);
        formatText("Experience the apocalypse in order. \n" +
                        "Your final score will be submitted to the Leaderboard!",
                Color.WHITE, 1.0f);

        table.row().padTop(10);

        // Level Select Description
        formatText("LEVEL SELECT", Color.CYAN, 1.2f);
        formatText("Practice any level in any order. \n" +
                        "Note: Highscores are disabled in this mode.",
                Color.WHITE, 1.0f);

        // --- Back Button ---
        table.row().padTop(40);
        UIUtils.addButton("Back", skin, table, buttonWidth, buttonHeight,
                () -> app.setScreen(new MainMenuScreen(app))).row();
    }
}
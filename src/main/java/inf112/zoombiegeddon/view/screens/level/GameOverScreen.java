package inf112.zoombiegeddon.view.screens.level;

import com.badlogic.gdx.graphics.Color;
import inf112.zoombiegeddon.app.GameApp;
import inf112.zoombiegeddon.events.GameEvent;
import inf112.zoombiegeddon.events.GameEventBus;
import inf112.zoombiegeddon.view.screens.AbstractScreen;
import inf112.zoombiegeddon.view.screens.menus.MainMenuScreen;
import inf112.zoombiegeddon.view.screens.menus.PickLevelScreen;
import inf112.zoombiegeddon.view.screens.story.StoryNameScreen;
import inf112.zoombiegeddon.view.utils.UIUtils;

/**
 * Displays the game over screen when the player dies.
 * Allows the player to retry, pick a new level, or return to the main menu.
 */
public class GameOverScreen extends AbstractScreen {

    /**
     * Defines how the retry button should behave on the game over screen.
     */
    public enum RetryMode {
        NONE,
        STORY,
        LEVEL
    }

    private final RetryMode retryMode;
    private final String retryMapPath;
    private final String retryBoardId;

    /**
     * Creates a game over screen without retry options.
     *
     * @param app the game application
     */
    public GameOverScreen(GameApp app) {
        this(app, RetryMode.NONE, null, null);}

    /**
     * Creates a game over screen with optional retry behavior.
     *
     * @param app          the game application
     * @param retryMode    the retry mode to use
     * @param retryMapPath the map path to retry, or {@code null} if not used
     * @param retryBoardId the leaderboard ID to use when retrying, or {@code null} if not used
     */
    public GameOverScreen(GameApp app, RetryMode retryMode, String retryMapPath, String retryBoardId) {
        super(app);
        this.retryMode = retryMode;
        this.retryMapPath = retryMapPath;
        this.retryBoardId = retryBoardId;
    }

    @Override
    public void buildScreen() {
        clearTable();

        GameEventBus.post(GameEvent.PLAYER_DEATH);
        GameEventBus.post(GameEvent.MENU_STARTED);

        formatText("GAME OVER", Color.RED, 6f);
        table.row().padTop(25);

        if (retryMode == RetryMode.STORY) {
            UIUtils.button("TRY AGAIN", skin).size(buttonWidth, buttonHeight)
                    .action(() -> app.setScreen(new StoryNameScreen(app)))
                    .addTo(table);


            table.row().padTop(10);
        }

        if (retryMode == RetryMode.LEVEL) {
            UIUtils.button("TRY AGAIN", skin).size(buttonWidth, buttonHeight)
                    .action(() -> app.setScreen(new PlayableLevel(app, retryMapPath, retryBoardId)))
                    .addTo(table);

            table.row().padTop(10);

            UIUtils.button("PICK NEW LEVEL", skin).size(buttonWidth, buttonHeight)
                    .action(() -> app.setScreen(new PickLevelScreen(app)))
                    .addTo(table);

            table.row().padTop(10);
        }

        UIUtils.button("MAIN MENU", skin).size(buttonWidth, buttonHeight)
                .action(() -> {
                    app.getStoryManager().finishRun();
                    app.setScreen(new MainMenuScreen(app));
                })
                .addTo(table);
    }
}
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

    public enum RetryMode {
        NONE,
        STORY,
        LEVEL
    }

    private final RetryMode retryMode;
    private final String retryMapPath;
    private final String retryBoardId;

    public GameOverScreen(GameApp app) {
        this(app, RetryMode.NONE, null, null);
    }

    public GameOverScreen(GameApp app, RetryMode retryMode, String retryMapPath, String retryBoardId) {
        super(app);
        this.retryMode = retryMode;
        this.retryMapPath = retryMapPath;
        this.retryBoardId = retryBoardId;
    }

    @Override
    public void buildScreen() {
        clearTable();

        formatText("GAME OVER", Color.RED, 6f);
        table.row().padTop(25);

        if (retryMode == RetryMode.STORY) {
            UIUtils.button("TRY STORYMODE AGAIN", skin).size(buttonWidth, buttonHeight)
                    .action(() -> app.setScreen(new StoryNameScreen(app)))
                    .addTo(table);
        GameEventBus.post(GameEvent.MENU_STARTED);

            table.row().padTop(10);
        }

        if (retryMode == RetryMode.LEVEL) {
            UIUtils.button("TRY LEVEL AGAIN", skin).size(buttonWidth, buttonHeight)
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
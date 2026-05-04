package inf112.zoombiegeddon.view.screens.level;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import inf112.zoombiegeddon.app.AssetConstants;
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

    private Texture background;

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
        background = app.getAssets().getManager().get(AssetConstants.GAME_OVER, Texture.class);
    }

    @Override
    public void buildScreen() {
        clearTable();

        GameEventBus.post(GameEvent.PLAYER_DEATH);
        GameEventBus.post(GameEvent.MENU_STARTED);

        float bw = buttonWidth * 1.3f;
        float bh = buttonHeight * 1.3f;

        table.bottom();
        table.padBottom(80);

        if (retryMode == RetryMode.STORY) {
            UIUtils.button("TRY STORYMODE AGAIN", skin).size(bw,bh)
                    .action(() -> app.setScreen(new StoryNameScreen(app)))
                    .addTo(table);

            table.row().padTop(15);
        }

        if (retryMode == RetryMode.LEVEL) {
            UIUtils.button("TRY LEVEL AGAIN", skin).size(bw,bh)
                    .action(() -> app.setScreen(new PlayableLevel(app, retryMapPath, retryBoardId)))
                    .addTo(table);

            table.row().padTop(15);

            UIUtils.button("PICK NEW LEVEL", skin).size(bw,bh)
                    .action(() -> app.setScreen(new PickLevelScreen(app)))
                    .addTo(table);

            table.row().padTop(15);
        }

        UIUtils.button("BACK TO MAIN MENU", skin).size(bw,bh)
                .action(() -> {
                    app.getStoryManager().finishRun();
                    app.setScreen(new MainMenuScreen(app));
                })
                .addTo(table);
    }
    @Override
    public void show() {
        super.show();

        stage.clear();

        Image bg = new Image(new TextureRegionDrawable(background));
        bg.setFillParent(true);

        stage.addActor(bg);
        stage.addActor(table);
    }
}
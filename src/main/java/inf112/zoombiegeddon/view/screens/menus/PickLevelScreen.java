package inf112.zoombiegeddon.view.screens.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import inf112.zoombiegeddon.app.GameApp;
import inf112.zoombiegeddon.view.screens.AbstractScreen;
import inf112.zoombiegeddon.view.screens.level.PlayableLevel;
import inf112.zoombiegeddon.view.utils.UIUtils;

/**
 * Displays the level selection screen.
 * Allows the player to choose a level before starting the game.
 */
public class PickLevelScreen extends AbstractScreen {

    private static final float SCROLL_SPEED = 2f;

    private final SpriteBatch bgBatch;
    private final OrthographicCamera bgCamera;

    /**
     * Constructs the level selection screen.
     *
     * @param app The game application instance.
     */
    public PickLevelScreen(GameApp app) {
        super(app);
        bgBatch = new SpriteBatch();
        bgCamera = new OrthographicCamera();
        bgCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        bgCamera.position.x += SCROLL_SPEED * delta;
        bgCamera.update();
        bgBatch.setProjectionMatrix(bgCamera.combined);
        bgBatch.begin();
        parallaxBg.render(bgBatch);
        bgBatch.end();
        stage.act(Math.min(delta, 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        bgCamera.setToOrtho(false, width, height);
        bgCamera.update();
        super.resize(width, height);
    }

    @Override
    public void dispose() {
        super.dispose();
        bgBatch.dispose();
    }

    @Override
    public void buildScreen() {
        clearTable();
        parallaxBg = addParallaxBg(bgCamera);

        formatText("PICK LEVEL", Color.WHITE, 6f);
        table.row().padTop(10);

        int totalLevels = app.getStoryManager().getTotalLevels();

        for (int i = 0; i < totalLevels; i++) {
            final String mapPath = app.getStoryManager().getMapPathForLevel(i);
            final int levelNumber = i + 1;
            final String boardId = "level" + levelNumber;

            UIUtils.button("LEVEL " + levelNumber, skin).size(buttonWidth, buttonHeight)
                    .action(() -> {
                        app.getStoryManager().finishRun();
                        app.setScreen(new PlayableLevel(app, mapPath, boardId));
                    })
                    .addTo(table);
        }

        UIUtils.button("BACK", skin).size(buttonWidth, buttonHeight)
                .action(() -> {
                    app.getStoryManager().finishRun();
                    app.setScreen(new MainMenuScreen(app));
                })
                .addTo(table);
    }
}
package inf112.zoombiegeddon.view.screens.story;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.ScreenUtils;
import inf112.zoombiegeddon.app.GameApp;
import inf112.zoombiegeddon.view.screens.AbstractScreen;
import inf112.zoombiegeddon.view.screens.menus.MainMenuScreen;
import inf112.zoombiegeddon.view.utils.UIUtils;

/**
 * Screen where the player enters their name before starting story mode.
 */
public class StoryNameScreen extends AbstractScreen {

    private static final float SCROLL_SPEED = 2f;

    private final SpriteBatch bgBatch;
    private final OrthographicCamera bgCamera;

    /**
     * Creates a story name screen for the given game application.
     *
     * @param app the game application
     */
    public StoryNameScreen(GameApp app) {
        super(app);
        bgBatch = new SpriteBatch();
        bgCamera = new OrthographicCamera();
        bgCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void buildScreen() {
        clearTable();
        table.top();
        parallaxBg = addParallaxBg(bgCamera);

        formatText("STORY MODE", Color.WHITE, 6f);
        table.row().padTop(10);

        String currentName = app.getCurrentPlayerName();
        if (currentName == null) {
            currentName = "";
        }

        TextField nameField = new TextField(currentName, app.getAssets().getSkin());
        nameField.setMessageText("Player Name... ");
        table.add(nameField).width(buttonWidth).height(buttonHeight).padBottom(20).row();

        UIUtils.button("START STORY", skin).size(buttonWidth, buttonHeight)
                .action(() -> {
                    app.getStoryManager().startRun(nameField.getText());
                    app.setScreen(new StoryScreen(app));
                }).addTo(table);

        table.add().expandY();
        table.row();

        UIUtils.button("BACK", skin).size(buttonWidth, buttonHeight)
                .action(() -> {
                    app.getStoryManager().finishRun();
                    app.setScreen(new MainMenuScreen(app));
                }).addTo(table);
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
}
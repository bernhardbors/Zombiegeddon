package inf112.zoombiegeddon.view.screens.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.ScreenUtils;
import inf112.zoombiegeddon.app.GameApp;
import inf112.zoombiegeddon.view.screens.AbstractScreen;
import inf112.zoombiegeddon.view.screens.menus.MainMenuScreen;
import inf112.zoombiegeddon.view.screens.menus.PickLevelScreen;
import inf112.zoombiegeddon.view.utils.UIUtils;

/**
 * Screen where the player enters a name before playing individual levels.
 */
public class LevelNameScreen extends AbstractScreen {

    private static final float SCROLL_SPEED = 2f;

    private final SpriteBatch bgBatch;
    private final OrthographicCamera bgCamera;

    /**
     * Creates a level name screen for the given game application.
     *
     * @param app the game application
     */
    public LevelNameScreen(GameApp app) {
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

        formatText("LEVELS", Color.WHITE, 5f);
        table.row().padTop(20);

        TextField nameField = new TextField("", skin);
        nameField.setMessageText("Player name");
        table.add(nameField)
                .width(buttonWidth)
                .height(buttonHeight)
                .padBottom(20);

        table.row();

        UIUtils.button("CONTINUE", skin).size(buttonWidth, buttonHeight)
                .action(() -> {
                    app.setCurrentPlayerName(nameField.getText());
                    app.setScreen(new PickLevelScreen(app));
                })
                .addTo(table);

        table.add().expandY();
        table.row();

        UIUtils.button("BACK", skin).size(buttonWidth, buttonHeight)
                .action(() -> app.setScreen(new MainMenuScreen(app)))
                .addTo(table);
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
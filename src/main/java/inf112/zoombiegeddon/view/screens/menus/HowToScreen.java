package inf112.zoombiegeddon.view.screens.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import inf112.zoombiegeddon.app.GameApp;
import inf112.zoombiegeddon.events.GameEvent;
import inf112.zoombiegeddon.events.GameEventBus;
import inf112.zoombiegeddon.view.utils.UIUtils;
import inf112.zoombiegeddon.view.screens.AbstractScreen;

/**
 * Displays instructions for how to play the game.
 */
public class HowToScreen extends AbstractScreen {

    private static final float SCROLL_SPEED = 2f;

    private final SpriteBatch bgBatch;
    private final OrthographicCamera bgCamera;

    /**
     * Constructs the how-to-play screen.
     *
     * @param app The game application instance.
     */
    public HowToScreen(GameApp app) {
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
        dimBackground();
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
        table.top();

        GameEventBus.post(GameEvent.MENU_STARTED);
        parallaxBg = addParallaxBg(bgCamera);

        float screenScale = Gdx.graphics.getHeight() / 1080f;

        Table contentTable = new Table();
        contentTable.top();
        contentTable.defaults().padTop(6f * screenScale);

        addLabel(contentTable, "HOW TO PLAY", Color.GOLD, 3.5f, screenScale);
        addLabel(contentTable, "CONTROLS", Color.ORANGE, 2.5f, screenScale);
        addLabel(contentTable, "Move: W, A, S, D / Arrows\nJump: Space / Up\nSprint: Left Shift\nPause: P  |  Quit: ESC",
                Color.WHITE, 2.2f, screenScale);
        addLabel(contentTable, "GAME MODES", Color.ORANGE, 2.5f, screenScale);
        addLabel(contentTable, "STORY MODE", Color.CYAN, 2.2f, screenScale);
        addLabel(contentTable, "Experience the apocalypse in order.\nYour final score will be submitted to the Leaderboard!",
                Color.WHITE, 2.0f, screenScale);
        addLabel(contentTable, "LEVEL SELECT", Color.CYAN, 2.2f, screenScale);
        addLabel(contentTable, "Practice any level in any order.\nNote: Highscores are disabled in this mode.",
                Color.WHITE, 2.0f, screenScale);

        ScrollPane scrollPane = new ScrollPane(contentTable, skin);
        scrollPane.getStyle().background = null;
        scrollPane.getStyle().vScroll = null;
        scrollPane.getStyle().vScrollKnob = null;
        scrollPane.setForceScroll(false, false);
        scrollPane.setScrollingDisabled(true, false);

        float contentWidth = Math.max(buttonWidth * 2.5f, Gdx.graphics.getWidth() * 0.5f);
        table.add(scrollPane).width(contentWidth).expand().fill().row();
        stage.setScrollFocus(scrollPane);

        UIUtils.button("Back", skin).size(buttonWidth, buttonHeight)
                .action(() -> app.setScreen(new MainMenuScreen(app))).addTo(table);
    }

    private void addLabel(Table target, String text, Color color, float scale, float screenScale) {
        Label.LabelStyle style = new Label.LabelStyle(app.getAssets().getFont(), color);
        Label label = new Label(text, style);
        label.setFontScale(scale * screenScale);
        label.setAlignment(Align.center);
        target.add(label).center().row();
    }
}
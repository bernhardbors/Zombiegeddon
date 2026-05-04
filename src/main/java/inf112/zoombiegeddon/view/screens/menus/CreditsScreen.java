package inf112.zoombiegeddon.view.screens.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import inf112.zoombiegeddon.app.GameApp;
import inf112.zoombiegeddon.events.GameEvent;
import inf112.zoombiegeddon.events.GameEventBus;
import inf112.zoombiegeddon.view.screens.AbstractScreen;
import inf112.zoombiegeddon.view.utils.UIUtils;

public class CreditsScreen extends AbstractScreen {

    private static final float SCROLL_SPEED = 2f;

    private final Screen returnScreen;
    private final SpriteBatch bgBatch;
    private final OrthographicCamera bgCamera;


    public CreditsScreen(GameApp app, Screen returnScreen) {
        super(app);
        this.returnScreen = returnScreen;

        bgBatch = new SpriteBatch();
        bgCamera = new OrthographicCamera();
        bgCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

    }

    @Override
    public void buildScreen() {
        clearTable();
        table.top();
        parallaxBg = addParallaxBg(bgCamera);

        GameEventBus.post(GameEvent.MENU_STARTED);

        formatText("CREDITS", Color.GOLD, 3.0f);

        float screenScale = Gdx.graphics.getHeight() / 1080f;

        Table creditsTable = new Table();
        creditsTable.defaults().pad(6f * screenScale);

        addSection(creditsTable, "PROGRAMMERS", Color.CYAN,
                "Name Here\nName Here\nName Here", screenScale);
        addSection(creditsTable, "GAME DESIGN", Color.ORANGE,
                "Name Here\nName Here", screenScale);
        addSection(creditsTable, "ART & GRAPHICS", Color.GREEN,
                "Name Here\nName Here", screenScale);
        addSection(creditsTable, "SOUND & MUSIC", Color.PINK,
                "Name Here", screenScale);
        addSection(creditsTable, "TESTING", Color.YELLOW,
                "Name Here\nName Here", screenScale);
        addSection(creditsTable, "SPECIAL THANKS", Color.WHITE,
                "Name Here", screenScale);

        ScrollPane scrollPane = new ScrollPane(creditsTable, skin);
        scrollPane.getStyle().background = null;
        scrollPane.getStyle().vScroll = null;
        scrollPane.getStyle().vScrollKnob = null;
        scrollPane.setForceScroll(false, false);
        scrollPane.setScrollingDisabled(true, false);

        float contentWidth = Math.max(buttonWidth * 2.5f, Gdx.graphics.getWidth() * 0.5f);

        table.add(scrollPane).width(contentWidth).expand().fill().row();
        stage.setScrollFocus(scrollPane);

        UIUtils.button("BACK", app.getAssets().getSkin()).size(buttonWidth, buttonHeight)
                .action(() -> app.setScreen(returnScreen)).addTo(table);
    }

    private void addSection(Table creditsTable, String title, Color titleColor, String names, float screenScale) {
        Label.LabelStyle titleStyle = new Label.LabelStyle(app.getAssets().getFont(), titleColor);
        Label titleLabel = new Label(title, titleStyle);
        titleLabel.setFontScale(1.6f * screenScale);
        creditsTable.add(titleLabel).center().padTop(20f * screenScale).row();

        Label.LabelStyle nameStyle = new Label.LabelStyle(app.getAssets().getFont(), Color.WHITE);
        Label nameLabel = new Label(names, nameStyle);
        nameLabel.setAlignment(com.badlogic.gdx.utils.Align.center);
        nameLabel.setFontScale(1.3f * screenScale);
        creditsTable.add(nameLabel).center().padBottom(10f * screenScale).row();
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
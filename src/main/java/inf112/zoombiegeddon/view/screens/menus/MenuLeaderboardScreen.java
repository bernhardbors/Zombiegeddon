package inf112.zoombiegeddon.view.screens.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import inf112.zoombiegeddon.app.GameApp;
import inf112.zoombiegeddon.events.GameEvent;
import inf112.zoombiegeddon.events.GameEventBus;
import inf112.zoombiegeddon.model.leaderboard.Leaderboard;
import inf112.zoombiegeddon.model.leaderboard.LeaderboardEntry;
import inf112.zoombiegeddon.view.screens.AbstractScreen;
import inf112.zoombiegeddon.view.utils.UIUtils;

import java.util.List;

public class MenuLeaderboardScreen extends AbstractScreen {

    private static final float SCROLL_SPEED = 2f;
    private static final int TOP_ENTRIES = 10;

    private static final String[][] BOARDS = {
        { GameApp.STORY_LEADERBOARD_ID, "STORY MODE" },
        { "level1", "LEVEL 1" },
        { "level2", "LEVEL 2" },
        { "level3", "LEVEL 3" },
    };

    private final Screen returnScreen;
    private final SpriteBatch bgBatch;
    private final OrthographicCamera bgCamera;
    private int selectedBoard = 0;

    public MenuLeaderboardScreen(GameApp app, Screen returnScreen) {
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

        float screenScale = Gdx.graphics.getHeight() / 1080f;

        formatText("LEADERBOARD", Color.GOLD, 3.0f);

        Table tabRow = new Table();
        tabRow.defaults().pad(5f * screenScale);
        for (int i = 0; i < BOARDS.length; i++) {
            final int idx = i;
            TextButton tabBtn = new TextButton(BOARDS[i][1], skin);
            tabBtn.getLabelCell().padBottom(buttonHeight / 2);
            tabBtn.getLabelCell().size(buttonWidth, buttonHeight);
            if (i == selectedBoard) {
                tabBtn.setColor(Color.YELLOW);
            }
            tabBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    GameEventBus.post(GameEvent.UI_BUTTON_CLICK);
                    selectedBoard = idx;
                    buildScreen();
                }
            });
            tabRow.add(tabBtn).size(buttonWidth, buttonHeight);
        }
        table.add(tabRow).padTop(10f * screenScale).row();

        Table entryTable = new Table();
        entryTable.defaults().pad(4f * screenScale);

        addEntryRow(entryTable, "#", "PLAYER", "SCORE", "TIME", Color.CYAN, 1.4f * screenScale);
        entryTable.row().padBottom(8f * screenScale);

        String boardId = BOARDS[selectedBoard][0];
        Leaderboard leaderboard = app.getLeaderboardManager().getLeaderboard(boardId);
        List<LeaderboardEntry> entries = leaderboard.getTopEntries(TOP_ENTRIES);

        if (entries.isEmpty()) {
            Label noEntries = new Label("No scores yet.", new Label.LabelStyle(app.getAssets().getFont(), Color.WHITE));
            noEntries.setFontScale(1.3f * screenScale);
            entryTable.add(noEntries).colspan(4).center().row();
        } else {
            for (int i = 0; i < entries.size(); i++) {
                LeaderboardEntry e = entries.get(i);
                float roundedTime = Math.round(e.getTimeUsed() * 10f) / 10f;
                addEntryRow(entryTable,
                    String.valueOf(i + 1),
                    e.getPlayerName(),
                    String.valueOf(e.getScore()),
                    roundedTime + "s",
                    getRankColor(i),
                    1.3f * screenScale
                );
            }
        }

        float contentWidth = Math.max(buttonWidth * BOARDS.length + 40f, Gdx.graphics.getWidth() * 0.55f);

        ScrollPane scrollPane = new ScrollPane(entryTable, skin);
        scrollPane.getStyle().background = null;
        scrollPane.getStyle().vScroll = null;
        scrollPane.getStyle().vScrollKnob = null;
        scrollPane.setScrollingDisabled(true, false);

        table.add(scrollPane).width(contentWidth).expand().fill().row();
        stage.setScrollFocus(scrollPane);

        UIUtils.button("BACK", app.getAssets().getSkin()).size(buttonWidth, buttonHeight)
                .action(() -> app.setScreen(returnScreen)).addTo(table);
    }

    private void addEntryRow(Table t, String rank, String name, String score, String time,
                              Color color, float fontScale) {
        Label.LabelStyle style = new Label.LabelStyle(app.getAssets().getFont(), color);
        float w = Gdx.graphics.getWidth() * 0.12f;

        Label rankLabel = new Label(rank, style);
        Label nameLabel = new Label(name, style);
        Label scoreLabel = new Label(score, style);
        Label timeLabel = new Label(time, style);

        rankLabel.setFontScale(fontScale);
        nameLabel.setFontScale(fontScale);
        scoreLabel.setFontScale(fontScale);
        timeLabel.setFontScale(fontScale);

        nameLabel.setAlignment(Align.center);

        t.add(rankLabel).width(w * 0.4f).center();
        t.add(nameLabel).width(w * 1.6f).center();
        t.add(scoreLabel).width(w).center();
        t.add(timeLabel).width(w).center();
        t.row();
    }

    private Color getRankColor(int index) {
        return switch (index) {
            case 0 -> Color.GOLD;
            case 1 -> Color.LIGHT_GRAY;
            case 2 -> new Color(0.8f, 0.6f, 0.3f, 1f);
            default -> Color.WHITE;
        };
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
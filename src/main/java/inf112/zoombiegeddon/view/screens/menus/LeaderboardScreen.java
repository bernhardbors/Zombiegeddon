package inf112.zoombiegeddon.view.screens.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import inf112.zoombiegeddon.app.GameApp;
import inf112.zoombiegeddon.model.leaderboard.Leaderboard;
import inf112.zoombiegeddon.model.leaderboard.LeaderboardEntry;
import inf112.zoombiegeddon.view.screens.AbstractScreen;
import inf112.zoombiegeddon.view.screens.story.StoryNameScreen;
import inf112.zoombiegeddon.view.utils.UIUtils;

import java.util.List;

/**
 * Displays leaderboard entries for story mode or individual levels.
 */
public class LeaderboardScreen extends AbstractScreen {

    private static final float SCROLL_SPEED = 2f;

    private final SpriteBatch bgBatch;
    private final OrthographicCamera bgCamera;
    private final String levelId;

    /**
     * Constructs the Leaderboard screen.
     *
     * @param app            the application context
     * @param levelId        the leaderboard to display scores from
     */
    public LeaderboardScreen(GameApp app, String levelId) {
        super(app);
        this.levelId = levelId;
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

        formatText(getLeaderboardTitle(), Color.WHITE, 4f);
        table.row().padTop(10);

        Leaderboard leaderboard = app.getLeaderboardManager().getLeaderboard(levelId);
        List<LeaderboardEntry> entries = leaderboard.getTopEntries(3);

        if (entries.isEmpty()) {
            formatText("No scores yet.", Color.WHITE, 1.5f);
            table.row();
        } else {
            for (int i = 0; i < entries.size(); i++) {
                LeaderboardEntry entry = entries.get(i);
                float roundedTime = Math.round(entry.getTimeUsed() * 10f) / 10f;

                String line = (i + 1) + ". "
                        + entry.getPlayerName()
                        + " - Score: " + entry.getScore()
                        + " - Time: " + roundedTime + "s";

                formatText(line, getRankColor(i), 1.5f);
                table.row().padTop(12);
            }
        }

        table.row().padTop(25);

        UIUtils.button("PICK LEVEL", app.getAssets().getSkin()).size(buttonWidth, buttonHeight)
                .action(() -> app.setScreen(new PickLevelScreen(app)))
                .addTo(table);

        table.row().padTop(10);

        UIUtils.button("STORY MODE", app.getAssets().getSkin()).size(buttonWidth, buttonHeight)
                .action(() -> app.setScreen(new StoryNameScreen(app)))
                .addTo(table);

        table.row().padTop(10);

        UIUtils.button("MAIN MENU", app.getAssets().getSkin()).size(buttonWidth, buttonHeight)
                .action(() -> {
                    app.getStoryManager().finishRun();
                    app.setScreen(new MainMenuScreen(app));
                })
                .addTo(table);
    }

    private String getLeaderboardTitle() {
        return switch (levelId) {
            case GameApp.STORY_LEADERBOARD_ID -> "Story Mode Leaderboard";
            case "level1" -> "Level 1 Leaderboard";
            case "level2" -> "Level 2 Leaderboard";
            case "level3" -> "Level 3 Leaderboard";
            default -> "Leaderboard";
        };
    }

    private Color getRankColor(int index) {
        return switch (index) {
            case 0 -> Color.GOLD;
            case 1 -> Color.LIGHT_GRAY;
            case 2 -> new Color(0.8f, 0.6f, 0.3f, 1f);
            default -> Color.WHITE;
        };
    }
}
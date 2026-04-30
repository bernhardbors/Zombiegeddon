package inf112.zoombiegeddon.view.screens.menus;

import com.badlogic.gdx.graphics.Color;
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

    private final String levelId;

    public enum ScreenType {
        NEXT_LEVEL,
        MAIN_MENU
    }

    public LeaderboardScreen(GameApp app, String levelId, ScreenType nextScreenType) {
        super(app);
        this.levelId = levelId;
    }

    @Override
    public void buildScreen() {
        clearTable();

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
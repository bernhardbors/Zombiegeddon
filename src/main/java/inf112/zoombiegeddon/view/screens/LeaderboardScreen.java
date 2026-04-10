package inf112.zoombiegeddon.view.screens;

import com.badlogic.gdx.graphics.Color;
import inf112.zoombiegeddon.app.GameApp;
import inf112.zoombiegeddon.model.leaderboard.Leaderboard;
import inf112.zoombiegeddon.model.leaderboard.LeaderboardEntry;
import inf112.zoombiegeddon.utils.UIUtils;

import java.util.List;

public class LeaderboardScreen extends AbstractScreen {

    private final String levelId;
    private final ScreenType nextScreenType;

    public enum ScreenType {
        NEXT_LEVEL,
        MAIN_MENU
    }

    public LeaderboardScreen(GameApp app, String levelId, ScreenType nextScreenType) {
        super(app);
        this.levelId = levelId;
        this.nextScreenType = nextScreenType;
    }

    @Override
    public void buildScreen() {
        clearTable();

        formatText("Leaderboard", Color.WHITE, 4f);
        table.row();

        formatText("Level: " + levelId, Color.LIGHT_GRAY, 2f);
        table.row();

        Leaderboard leaderboard = app.leaderboardManager.getLeaderboard(levelId);
        List<LeaderboardEntry> entries = leaderboard.getTopEntries(5);

        if (entries.isEmpty()) {
            formatText("No scores yet.", Color.WHITE, 1.5f);
            table.row();
        } else {
            for (int i = 0; i < entries.size(); i++) {
                LeaderboardEntry entry = entries.get(i);
                String line = (i + 1) + ". "
                        + entry.getPlayerName()
                        + " - "
                        + entry.getScore();

                formatText(line, Color.WHITE, 1.5f);
                table.row();
            }
        }

        table.row().padTop(20);

        UIUtils.addButton("Main Menu", app.skin, table, buttonWidth, buttonHeight,
                () -> app.setScreen(new MainMenuScreen(app)));

        if (nextScreenType == ScreenType.NEXT_LEVEL) {
            UIUtils.addButton("Continue", app.skin, table, buttonWidth, buttonHeight,
                    () -> app.setScreen(new NextLevelScreen(app)));
        }
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
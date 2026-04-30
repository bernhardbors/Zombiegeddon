package inf112.zoombiegeddon.view.screens.menus;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import inf112.zoombiegeddon.app.AssetConstants;
import inf112.zoombiegeddon.app.GameApp;
import inf112.zoombiegeddon.model.leaderboard.Leaderboard;
import inf112.zoombiegeddon.model.leaderboard.LeaderboardEntry;
import inf112.zoombiegeddon.view.utils.UIUtils;
import inf112.zoombiegeddon.view.screens.AbstractScreen;
import inf112.zoombiegeddon.view.screens.level.NextLevelScreen;

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

        if (app.getAssets().getManager().isLoaded(AssetConstants.BG_SKY)) {
            Texture background = app.getAssets().getManager().get(AssetConstants.BG_SKY, Texture.class);
            table.setBackground(new TextureRegionDrawable(background));
        }

        formatText("Leaderboard", Color.WHITE, 4f);
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

                Color color = Color.WHITE;
                if (i == 0) {
                    color = Color.GOLD;
                } else if (i == 1) {
                    color = Color.LIGHT_GRAY;
                } else if (i == 2) {
                    color = new Color(0.8f, 0.6f, 0.3f, 1f);
                }

                formatText(line, color, 1.5f);
                table.row().padTop(12);
            }
        }

        table.row().padTop(25);

        if (nextScreenType == ScreenType.NEXT_LEVEL) {
            UIUtils.button("Continue", app.getAssets().getSkin()).size(buttonWidth, buttonHeight)
                    .action(() -> app.setScreen(new NextLevelScreen(app))).addTo(table);
        }

        UIUtils.button("Main Menu", app.getAssets().getSkin()).size(buttonWidth, buttonHeight)
                .action(() -> {
                    app.getStoryManager().finishRun();
                    app.setScreen(new MainMenuScreen(app));
                }).addTo(table);
    }
}
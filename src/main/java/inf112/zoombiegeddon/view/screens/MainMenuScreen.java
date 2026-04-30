package inf112.zoombiegeddon.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import inf112.zoombiegeddon.app.AssetConstants;
import inf112.zoombiegeddon.app.GameApp;
import inf112.zoombiegeddon.view.utils.UIUtils;

import inf112.zoombiegeddon.audio.MusicTrack;

/**
 * Represents the main menu screen of the game.
 * Displays navigation options such as starting the game,
 * opening settings, viewing instructions, or exiting.
 */
public class MainMenuScreen extends AbstractScreen {

    /**
     * Constructs the main menu screen.
     *
     * @param app The game application instance.
     */
    public MainMenuScreen(final GameApp app) {
        super(app);
    }

    @Override

    public void buildScreen() {

        clearTable();
        app.getStoryManager().finishRun();
        app.getAudio().playMusic(MusicTrack.MENU, true);

        if (app.getAssets().getManager().isLoaded(AssetConstants.BG_GAME)) {
            Texture background = app.getAssets().getManager().get(AssetConstants.BG_GAME, Texture.class);
            table.setBackground(new TextureRegionDrawable(background));
        }

        formatText("Zoombiegeddon", Color.WHITE, 6f);
        table.getCell(table.getChildren().peek()).expandY().top().padTop(-70);
        table.row().padTop(5);


        UIUtils.button("STORY MODE", app.getAssets().getSkin()).size(buttonWidth, buttonHeight)
                .action(() -> app.setScreen(new StoryNameScreen(app))).addTo(table);

        UIUtils.button("LEVELS", app.getAssets().getSkin()).size(buttonWidth, buttonHeight)
                .action(() -> app.setScreen(new PickLevelScreen(app))).addTo(table);

        UIUtils.button("HOW TO PLAY", app.getAssets().getSkin()).size(buttonWidth, buttonHeight)
                .action(() -> app.setScreen(new HowToScreen(app))).addTo(table);

        UIUtils.button("SETTINGS", app.getAssets().getSkin()).size(buttonWidth, buttonHeight)
                .action(() -> app.setScreen(new SettingsScreen(app, this))).addTo(table);

        UIUtils.button("SCOREBOARD", app.getAssets().getSkin()).size(buttonWidth, buttonHeight)
                .action(() -> app.setScreen(new LeaderboardScreen(app, GameApp.STORY_LEADERBOARD_ID, LeaderboardScreen.ScreenType.MAIN_MENU))).addTo(table);

        UIUtils.button("EXIT", app.getAssets().getSkin()).size(buttonWidth, buttonHeight)
                .action(Gdx.app::exit).addTo(table);
    }
}
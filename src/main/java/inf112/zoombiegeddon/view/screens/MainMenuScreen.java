package inf112.zoombiegeddon.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import inf112.zoombiegeddon.app.AssetConstants;
import inf112.zoombiegeddon.app.GameApp;
import inf112.zoombiegeddon.utils.UIUtils;

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
        app.finishStoryRun();

        app.getAudio().playMusic(MusicTrack.MENU, true);

        if (app.getAssets().getManager().isLoaded(AssetConstants.BG_GAME)) {
            Texture background = app.getAssets().getManager().get(AssetConstants.BG_GAME, Texture.class);
            table.setBackground(new TextureRegionDrawable(background));
        }

        formatText("Zoombiegeddon", Color.WHITE, 6f);
        table.getCell(table.getChildren().peek()).expandY().top().padTop(-70);
        table.row().padTop(5);


        UIUtils.addButton("STORY MODE", app.getAssets().getSkin(), table, buttonWidth, buttonHeight,
        () -> app.setScreen(new StoryNameScreen(app)));

        UIUtils.addButton("LEVELS", app.getAssets().getSkin(), table, buttonWidth, buttonHeight,
                () -> app.setScreen(new PickLevelScreen(app)));

        UIUtils.addButton("HOW TO PLAY", app.getAssets().getSkin(), table, buttonWidth, buttonHeight,
                () -> app.setScreen(new HowToScreen(app)));

        UIUtils.addButton("SETTINGS", app.getAssets().getSkin(), table, buttonWidth, buttonHeight,
                () -> app.setScreen(new SettingsScreen(app, this)));

        UIUtils.addButton("SCOREBOARD", app.getAssets().getSkin(), table, buttonWidth, buttonHeight,
            () -> app.setScreen(new LeaderboardScreen(app, GameApp.STORY_LEADERBOARD_ID, LeaderboardScreen.ScreenType.MAIN_MENU)));

        UIUtils.addButton("EXIT", app.getAssets().getSkin(), table, buttonWidth, buttonHeight,
                () -> Gdx.app.exit());
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
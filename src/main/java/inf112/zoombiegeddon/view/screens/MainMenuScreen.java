package inf112.zoombiegeddon.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import inf112.zoombiegeddon.app.AssetConstants;
import inf112.zoombiegeddon.app.GameApp;
import inf112.zoombiegeddon.utils.UIUtils;

import inf112.zoombiegeddon.audio.MusicTrack;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

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

        app.audio.playMusic(MusicTrack.MENU, true);

        if (app.manager.isLoaded(AssetConstants.BG_GAME)) {
            Texture background = app.manager.get(AssetConstants.BG_GAME, Texture.class);
            table.setBackground(new TextureRegionDrawable(background));
        }

        formatText("Zoombiegeddon", Color.WHITE, 6f);
        table.getCell(table.getChildren().peek()).expandY().top().padTop(-70);
        table.row().padTop(5);

        TextField nameField = new TextField("", app.skin);
        nameField.setMessageText("Skriv inn navnet ditt");
        table.add(nameField).width(buttonWidth).height(buttonHeight).padBottom(250).row();

        UIUtils.addButton("Start", app.skin, table, buttonWidth, buttonHeight,
                () -> {
                    app.setCurrentPlayerName(nameField.getText());
                    System.out.println("Saved player name from menu: " + app.getCurrentPlayerName());
                    app.setScreen(new PickLevelScreen(app));
                });

        UIUtils.addButton("How To Play", app.skin, table, buttonWidth, buttonHeight,
                () -> app.setScreen(new HowToScreen(app)));

        UIUtils.addButton("Settings", app.skin, table, buttonWidth, buttonHeight,
                () -> app.setScreen(new SettingsScreen(app, this)));

        UIUtils.addButton("Exit", app.skin, table, buttonWidth, buttonHeight,
                () -> Gdx.app.exit());
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
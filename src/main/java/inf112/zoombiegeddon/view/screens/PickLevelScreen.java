package inf112.zoombiegeddon.view.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import inf112.zoombiegeddon.app.AssetConstants;
import inf112.zoombiegeddon.app.GameApp;
import inf112.zoombiegeddon.utils.UIUtils;
import inf112.zoombiegeddon.view.levels.Level1;
import inf112.zoombiegeddon.view.levels.Level2;

/**
 * Displays the level selection screen.
 * Allows the player to choose a level before starting the game.
 */
public class PickLevelScreen extends AbstractScreen {

    /**
     * Constructs the level selection screen.
     *
     * @param app The game application instance.
     */
    public PickLevelScreen(GameApp app) {
        super(app);
    }

    @Override
    public void buildScreen() {
        clearTable();

        if (app.getAssets().getManager().isLoaded(AssetConstants.BG_SKY)) {
            Texture background = app.getAssets().getManager().get(AssetConstants.BG_SKY, Texture.class);
            table.setBackground(new TextureRegionDrawable(background));
        }

        formatText("PICK LEVEL", Color.WHITE, 6f);
        table.getCell(table.getChildren().peek()).expandY().top().padTop(-60);
        table.row().padTop(10);

        String currentName = app.getCurrentPlayerName();
        if (currentName == null) {
            currentName = "";
        }
        TextField nameField = new TextField(currentName, app.getAssets().getSkin());
        nameField.setMessageText("Skriv inn navnet ditt");
        table.add(nameField).width(buttonWidth).height(buttonHeight).padBottom(280).row();

        UIUtils.addButton("Level 1", skin, table, buttonWidth, buttonHeight,
                () -> {
                    app.setCurrentPlayerName(nameField.getText());
                    app.setScreen(new Level1(app));
                });

        UIUtils.addButton("Level 2", skin, table, buttonWidth, buttonHeight,
                () -> {
                    app.setCurrentPlayerName(nameField.getText());
                    app.setScreen(new Level2(app));
                });

        UIUtils.addButton("Back", skin, table, buttonWidth, buttonHeight,
                () -> app.setScreen(new MainMenuScreen(app)));
    }
}
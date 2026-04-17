package inf112.zoombiegeddon.view.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import inf112.zoombiegeddon.app.AssetConstants;
import inf112.zoombiegeddon.app.GameApp;
import inf112.zoombiegeddon.utils.UIUtils;
import inf112.zoombiegeddon.view.levels.Level1;
import inf112.zoombiegeddon.view.levels.Level2;
import inf112.zoombiegeddon.view.levels.Level3;

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
        table.row().padTop(10);

        UIUtils.addButton("LEVEL 1", skin, table, buttonWidth, buttonHeight,
                () -> {
                    app.getStoryManager().finishRun();
                    app.setScreen(new Level1(app));
                });

        UIUtils.addButton("LEVEL 2", skin, table, buttonWidth, buttonHeight,
                () -> {
                    app.getStoryManager().finishRun();
                    app.setScreen(new Level2(app));
                });

        UIUtils.addButton("PLACEHOLDER", skin, table, buttonWidth, buttonHeight,
                () -> {
                    app.setScreen(new Level3(app));
                });

        UIUtils.addButton("PLACEHOLDER", skin, table, buttonWidth, buttonHeight,
                () -> {
                    //app.setScreen(new );
                });

        UIUtils.addButton("BACK", skin, table, buttonWidth, buttonHeight,
                () -> {
                    app.getStoryManager().finishRun();
                    app.setScreen(new MainMenuScreen(app));
                });
    }
}
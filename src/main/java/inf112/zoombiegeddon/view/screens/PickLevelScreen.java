package inf112.zoombiegeddon.view.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import inf112.zoombiegeddon.app.AssetConstants;
import inf112.zoombiegeddon.app.GameApp;
import inf112.zoombiegeddon.utils.UIUtils;

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
                    app.setScreen(new PlayableLevel(app, "tilemaps/level1.tmx", "level1"));
                });

        UIUtils.addButton("LEVEL 2", skin, table, buttonWidth, buttonHeight,
                () -> {
                    app.getStoryManager().finishRun();
                    app.setScreen(new PlayableLevel(app, "tilemaps/level3.tmx", "level2"));
                });

        UIUtils.addButton("LEVEL 3", skin, table, buttonWidth, buttonHeight,
                () -> {
                    app.setScreen(new PlayableLevel(app, "tilemaps/level5.tmx", "level3"));
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
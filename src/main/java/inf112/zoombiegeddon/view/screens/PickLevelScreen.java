package inf112.zoombiegeddon.view.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import inf112.zoombiegeddon.app.AssetConstants;
import inf112.zoombiegeddon.app.GameApp;
import inf112.zoombiegeddon.view.utils.UIUtils;

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

        int totalLevels = app.getStoryManager().getTotalLevels();

        for (int i = 0; i < totalLevels; i++) {
            final String mapPath = app.getStoryManager().getMapPathForLevel(i);
            final int levelNumber = i + 1;

            UIUtils.button("LEVEL " + levelNumber, skin).size(buttonWidth, buttonHeight)
                    .action(() -> {
                        app.getStoryManager().finishRun();
                        app.setScreen(new PlayableLevel(app, mapPath));
                    }).addTo(table);
        }

        UIUtils.button("BACK", skin).size(buttonWidth, buttonHeight)
                .action(() -> {
                    app.getStoryManager().finishRun();
                    app.setScreen(new MainMenuScreen(app));
                }).addTo(table);
    }
}
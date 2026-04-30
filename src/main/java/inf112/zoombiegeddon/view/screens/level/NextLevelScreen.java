package inf112.zoombiegeddon.view.screens.level;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import inf112.zoombiegeddon.app.AssetConstants;
import inf112.zoombiegeddon.app.GameApp;
import inf112.zoombiegeddon.utils.UIUtils;
import inf112.zoombiegeddon.view.screens.AbstractScreen;
import inf112.zoombiegeddon.view.screens.menus.MainMenuScreen;


/**
 * Displays the screen shown when the player reaches the goal.
 * Allows the player to choose the next level or return to the main menu.
 */
public class NextLevelScreen extends AbstractScreen {


    /**
     * Constructs the next level screen.
     *
     * @param app The game application instance.
     */
    public NextLevelScreen(GameApp app) {super(app); }

    @Override
    public void buildScreen() {
        clearTable();

        if (app.getAssets().getManager().isLoaded(AssetConstants.BG_SKY)) {
            Texture background = app.getAssets().getManager().get(AssetConstants.BG_SKY, Texture.class);
            table.setBackground(new TextureRegionDrawable(background));


        }

        formatText("PICK NEXT LEVEL", Color.WHITE, 6f);

        int totalLevels = app.getStoryManager().getTotalLevels();

        for (int i = 0; i < totalLevels; i++) {
            final String mapPath = app.getStoryManager().getMapPathForLevel(i);
            final int levelNumber = i + 1;

            UIUtils.button("Level " + levelNumber, skin).size(buttonWidth, buttonHeight)
                    .action(() -> {
                        app.getStoryManager().finishRun();
                        app.setScreen(new PlayableLevel(app, mapPath));
                    }).addTo(table);
        }

        UIUtils.button("Main menu", skin).size(buttonWidth, buttonHeight)
                .action(() -> app.setScreen(new MainMenuScreen(app))).addTo(table);

    }
}

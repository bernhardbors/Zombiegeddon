package inf112.zoombiegeddon.view.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import inf112.zoombiegeddon.app.AssetConstants;
import inf112.zoombiegeddon.app.GameApp;
import inf112.zoombiegeddon.utils.UIUtils;
import inf112.zoombiegeddon.view.levels.Level0;


/**
 * Screen that shows when the player reaches goal.
 * You can pick any level that you want.
 */
public class NextLevelScreen extends AbstractScreen{

    public NextLevelScreen(GameApp app) {super(app); }

    @Override
    public void buildScreen() {
        clearTable();

        if (app.manager.isLoaded(AssetConstants.BG_SKY)) {
            Texture background = app.manager.get(AssetConstants.BG_SKY, Texture.class);
            table.setBackground(new TextureRegionDrawable(background));


        }

        formatText("PICK NEXT LEVEL", Color.WHITE, 6f);
        UIUtils.addButton("Level 0", skin, table, buttonWidth, buttonHeight, () -> app.setScreen(new Level0(app)));
    }
}

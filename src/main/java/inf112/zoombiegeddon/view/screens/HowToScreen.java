package inf112.zoombiegeddon.view.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import inf112.zoombiegeddon.app.AssetConstants;
import inf112.zoombiegeddon.app.GameApp;
import inf112.zoombiegeddon.utils.UIUtils;

public class HowToScreen extends AbstractScreen {

    public HowToScreen(GameApp app) {
        super(app);

    }

    @Override
    public void buildScreen() {
       clearTable();

        //Setter bakgrunn
        if (app.manager.isLoaded(AssetConstants.BG_SKY)) {
            Texture background = app.manager.get(AssetConstants.BG_SKY, Texture.class);
            table.setBackground(new TextureRegionDrawable(background));
        }

        formatText("HOW TO PLAY", Color.WHITE, 2f);

        formatText(
                "Move: W, A, S, D or Arrow Keys\n" +
                "Jump: Space or Up\n" +
                "Sprint: Shift\n" +
                "Quit: Escape",
                Color.WHITE, 1.5f);

        UIUtils.addButton("Back", skin, table, buttonWidth, buttonHeight, () -> app.setScreen(new MainMenuScreen(app))).row();
    }
}

package inf112.zoombiegeddon.view.screens.level;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import inf112.zoombiegeddon.app.AssetConstants;
import inf112.zoombiegeddon.app.GameApp;
import inf112.zoombiegeddon.view.screens.AbstractScreen;
import inf112.zoombiegeddon.view.screens.menus.MainMenuScreen;
import inf112.zoombiegeddon.view.screens.menus.PickLevelScreen;
import inf112.zoombiegeddon.view.utils.UIUtils;

/**
 * Screen where the player enters a name before playing individual levels.
 */
public class LevelNameScreen extends AbstractScreen {

    /**
     * Constructs the level name screen.
     *
     * @param app The game application instance.
     */
    public LevelNameScreen(GameApp app) {
        super(app);
    }

    @Override
    public void buildScreen() {
        clearTable();

        if (app.getAssets().getManager().isLoaded(AssetConstants.BG_SKY)) {
            Texture background = app.getAssets().getManager().get(AssetConstants.BG_SKY, Texture.class);
            table.setBackground(new TextureRegionDrawable(background));
        }

        formatText("ENTER NAME", Color.WHITE, 5f);
        table.row().padTop(20);

        TextField nameField = new TextField("", skin);
        nameField.setMessageText("Player name");
        table.add(nameField)
                .width(buttonWidth)
                .height(buttonHeight)
                .padBottom(20);

        table.row();

        UIUtils.button("CONTINUE", skin).size(buttonWidth, buttonHeight)
                .action(() -> {
                    app.setCurrentPlayerName(nameField.getText());
                    app.setScreen(new PickLevelScreen(app));
                })
                .addTo(table);

        UIUtils.button("BACK", skin).size(buttonWidth, buttonHeight)
                .action(() -> app.setScreen(new MainMenuScreen(app)))
                .addTo(table);
    }
}
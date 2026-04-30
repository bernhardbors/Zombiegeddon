package inf112.zoombiegeddon.view.screens.story;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import inf112.zoombiegeddon.app.AssetConstants;
import inf112.zoombiegeddon.app.GameApp;
import inf112.zoombiegeddon.view.utils.UIUtils;
import inf112.zoombiegeddon.view.screens.AbstractScreen;
import inf112.zoombiegeddon.view.screens.menus.MainMenuScreen;

/**
 * Screen where the player enters their name before starting story mode.
 */
public class StoryNameScreen extends AbstractScreen {

    public StoryNameScreen(GameApp app) {
        super(app);
    }

    @Override
    public void buildScreen() {
        clearTable();

        if (app.getAssets().getManager().isLoaded(AssetConstants.BG_SKY)) {
            Texture background = app.getAssets().getManager().get(AssetConstants.BG_SKY, Texture.class);
            table.setBackground(new TextureRegionDrawable(background));
        }

        formatText("STORY MODE", Color.WHITE, 6f);
        table.getCell(table.getChildren().peek()).expandY().top().padTop(-60);
        table.row().padTop(10);

        String currentName = app.getCurrentPlayerName();
        if (currentName == null) {
            currentName = "";
        }

        TextField nameField = new TextField(currentName, app.getAssets().getSkin());
        nameField.setMessageText("Player Name... ");
        table.add(nameField).width(buttonWidth).height(buttonHeight).padBottom(220).row();

        UIUtils.button("START STORY", skin).size(buttonWidth, buttonHeight)
                .action(() -> {
                    app.getStoryManager().startRun(nameField.getText());
                    app.setScreen(new StoryScreen(app));
                }).addTo(table);

        UIUtils.button("BACK", skin).size(buttonWidth, buttonHeight)
                .action(() -> {
                    app.getStoryManager().finishRun();
                    app.setScreen(new MainMenuScreen(app));
                }).addTo(table);
    }
}
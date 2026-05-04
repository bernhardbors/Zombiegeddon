package inf112.zoombiegeddon.view.screens.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import inf112.zoombiegeddon.app.AssetConstants;
import inf112.zoombiegeddon.app.GameApp;
import inf112.zoombiegeddon.events.GameEvent;
import inf112.zoombiegeddon.events.GameEventBus;
import inf112.zoombiegeddon.view.screens.AbstractScreen;
import inf112.zoombiegeddon.view.screens.level.LevelNameScreen;
import inf112.zoombiegeddon.view.screens.story.StoryNameScreen;
import inf112.zoombiegeddon.view.utils.UIUtils;

/**
 * Represents the main menu screen of the game.
 * Displays navigation options such as starting the game,
 * opening settings, viewing instructions, or exiting.
 */
public class MainMenuScreen extends AbstractScreen {

    private static final float BUTTON_SCALE = 1.2f;
    private static final float COLUMN_PADDING = 0f;
    private static final float BOTTOM_PADDING = 2f;

    /**
     * Constructs the main menu screen.
     *
     * @param app The game application instance.
     */
    public MainMenuScreen(final GameApp app) {
        super(app);
    }

    @Override
    public void show() {
        super.show();
        app.getStoryManager().finishRun();
        GameEventBus.post(GameEvent.MENU_STARTED);
    }

    @Override
    public void buildScreen() {
        clearTable();
        Skin skin = app.getAssets().getSkin();

        if (app.getAssets().getManager().isLoaded(AssetConstants.BG_GAME)) {
            Texture background = app.getAssets().getManager().get(AssetConstants.BG_GAME, Texture.class);
            table.setBackground(new TextureRegionDrawable(background));
        }

        formatText("Zoombiegeddon", Color.WHITE, 6f);
        table.getCell(table.getChildren().peek()).colspan(2).expandY().top().padTop(-70);
        table.row().padTop(5);

        float bw = buttonWidth * BUTTON_SCALE;
        float bh = buttonHeight * BUTTON_SCALE;

        Table leftColumn = new Table();
        UIUtils.button("STORY MODE", skin).size(bw, bh)
                .action(() -> app.setScreen(new StoryNameScreen(app))).addTo(leftColumn);
        UIUtils.button("HOW TO PLAY", skin).size(bw, bh)
                .action(() -> app.setScreen(new HowToScreen(app))).addTo(leftColumn);
        UIUtils.button("SCOREBOARD", skin).size(bw, bh)
                .action(() -> app.setScreen(new MenuLeaderboardScreen(app, this))).addTo(leftColumn);

        Table rightColumn = new Table();
        UIUtils.button("LEVELS", skin).size(bw, bh)
                .action(() -> app.setScreen(new LevelNameScreen(app))).addTo(rightColumn);
        UIUtils.button("CREDITS", skin).size(bw, bh)
                .action(() -> app.setScreen(new CreditsScreen(app,this))).addTo(rightColumn);
        UIUtils.button("SETTINGS", skin).size(bw, bh)
                .action(() -> app.setScreen(new SettingsScreen(app, this))).addTo(rightColumn);

        table.add(leftColumn).padRight(COLUMN_PADDING).right();
        table.add(rightColumn).padLeft(COLUMN_PADDING).left();

        Table bottomRow = new Table();
        UIUtils.button("EXIT", skin).size(bw, bh)
                .action(Gdx.app::exit).addTo(bottomRow);
        table.row();
        table.add(bottomRow).colspan(2).center().padBottom(BOTTOM_PADDING);
    }
}
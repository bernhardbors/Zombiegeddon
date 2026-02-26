package inf112.zoombiegeddon.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import inf112.zoombiegeddon.app.GameApp;
import inf112.zoombiegeddon.utils.UIUtils;
import inf112.zoombiegeddon.view.levels.TestLevel01;

/**
 * The MainMenuScreen provides the primary user interface for navigation.
 * It allows players to start the game, view instructions, adjust settings, or exit.
 */
public class MainMenuScreen extends AbstractScreen {

    private Texture background;

    /**
     * Constructor for MainMenuScreen.
     * @param app The main application instance used for global asset access and screen management.
     */
    public MainMenuScreen(final GameApp app) {
        super(app);
        
        this.background = new Texture("background/background1.png");
        table.setBackground(new TextureRegionDrawable(background));

        // Use loaded asset instead of creating new Texture
        if (app.manager.isLoaded(GameApp.SPLASH_LOGO)) {
            this.background = app.manager.get(GameApp.SPLASH_LOGO, Texture.class);
        } else {
            // Fallback just in case, though manager should have it from LoadingScreen
            app.manager.load(GameApp.SPLASH_LOGO, Texture.class);
            app.manager.finishLoading();
            this.background = app.manager.get(GameApp.SPLASH_LOGO, Texture.class);
        }
    }

    /**
     * Sets up the UI layout for the main menu.
     * Configures the background, title, and navigation buttons.
     */
    @Override
    public void buildScreen() {
        super.buildScreen();


        formatText("Zoombiegeddon", Color.WHITE, 5f);

        UIUtils.addButton("Start", skin, table, buttonWidth, buttonHeight, () -> app.setScreen(new TestLevel01(app, new Vector2(0,-9.81f))));
        UIUtils.addButton("How To Play", skin, table, buttonWidth, buttonHeight, () -> app.setScreen(new HowToScreen(app)));
        UIUtils.addButton("Settings", skin, table, buttonWidth, buttonHeight, () -> app.setScreen(new SettingsScreen(app)));
        UIUtils.addButton("Exit", skin, table, buttonWidth, buttonHeight, () -> Gdx.app.exit());
    }

    /**
     * Releases all resources used by this screen.
     * Specifically disposes of the background texture to prevent memory leaks.
     */
    @Override
    public void dispose() {
        super.dispose();
    }
}
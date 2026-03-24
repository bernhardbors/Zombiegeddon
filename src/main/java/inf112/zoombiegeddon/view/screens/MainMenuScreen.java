package inf112.zoombiegeddon.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import inf112.zoombiegeddon.app.AssetConstants;
import inf112.zoombiegeddon.app.GameApp;
import inf112.zoombiegeddon.utils.UIUtils;
import inf112.zoombiegeddon.view.levels.Level2;

/**
 * MainMenuScreen bruker ressurser som allerede er lastet inn via AssetManager.
 */
public class MainMenuScreen extends AbstractScreen {

    public MainMenuScreen(final GameApp app) {
        super(app);
    }

    @Override
    public void buildScreen() {
        // 1. Rydd tabellen før vi bygger (sikkerhet hvis skjermen vises flere ganger)
        clearTable();

        // 2. Hent bakgrunn fra manager (IKKE lag "new Texture")
        if (app.manager.isLoaded(AssetConstants.BG_SKY)) {
            Texture background = app.manager.get(AssetConstants.BG_SKY, Texture.class);
            table.setBackground(new TextureRegionDrawable(background));
        }

        // 3. Tittel
        formatText("Zoombiegeddon", Color.WHITE, 5f);
        table.row().padTop(20);

        // 4. Knapper via UIUtils
        // Vi bruker app.skin som ble initialisert i LoadingScreen
        UIUtils.addButton("Start", app.skin, table, buttonWidth, buttonHeight,
                () -> app.setScreen( new Level2(app)));

        UIUtils.addButton("How To Play", app.skin, table, buttonWidth, buttonHeight,
                () -> app.setScreen(new HowToScreen(app)));

        UIUtils.addButton("Settings", app.skin, table, buttonWidth, buttonHeight,
                () -> app.setScreen(new SettingsScreen(app)));

        UIUtils.addButton("Exit", app.skin, table, buttonWidth, buttonHeight,
                () -> Gdx.app.exit());
    }

    @Override
    public void dispose() {
        // Vi trenger IKKE dispose bakgrunnen her fordi den eies av AssetManager i GameApp.
        // Hvis vi kaller background.dispose() her, vil bakgrunnen bli svart resten av tiden spillet kjører.
        super.dispose();
    }
}
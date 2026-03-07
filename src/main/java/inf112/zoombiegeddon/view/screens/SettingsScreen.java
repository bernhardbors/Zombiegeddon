package inf112.zoombiegeddon.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import inf112.zoombiegeddon.app.AssetConstants;
import inf112.zoombiegeddon.app.GameApp;
import inf112.zoombiegeddon.utils.UIUtils;

/**
 * SettingsScreen lar spilleren justere konfigurasjoner.
 * Bruker app.prefs og app.skin for konsistens.
 */
public class SettingsScreen extends AbstractScreen {

    public SettingsScreen(final GameApp app) {
        super(app);
    }

    @Override
    public void buildScreen() {
        // Rydd tabellen før vi bygger for å unngå duplikater hvis buildScreen kalles igjen
        table.clear();

        //Setter bakgrunn
        if (app.manager.isLoaded(AssetConstants.BG_SKY)) {
            Texture background = app.manager.get(AssetConstants.BG_SKY, Texture.class);
            table.setBackground(new TextureRegionDrawable(background));
        }
        // Tittel
        formatText("SETTINGS", Color.WHITE, 2f);
        table.row().pad(20);

        // Oppløsningsvalg
        String[] resolutions = {"1280 x 720", "1366 x 768", "1600 x 900", "1920 x 1080", "2560 x 1440", "3840 x 2160"};

        // Hent lagret oppløsning fra appens preferanser
        String savedRes = app.prefs.getString(AssetConstants.PREF_RES_STRING, resolutions[0]);

        // SelectBox - Bruker app.skin
        UIUtils.addSelectBox("Resolutions:", resolutions, app.skin, table, buttonWidth, buttonHeight, this::changeResolution)
                .setSelected(savedRes);

        table.row().padTop(50);

        // Tilbake-knapp
        UIUtils.addButton("Back", app.skin, table, buttonWidth, buttonHeight,
                () -> app.setScreen(new MainMenuScreen(app)));
    }

    /**
     * Håndterer endring av oppløsning og lagrer til Preferences.
     */
    public void changeResolution(String resolution) {
        try {
            String[] parts = resolution.split("x");
            int width = Integer.parseInt(parts[0].trim());
            int height = Integer.parseInt(parts[1].trim());

            // Oppdater vinduet
            Gdx.graphics.setWindowedMode(width, height);

            // Lagre til fil
            app.prefs.putInteger(AssetConstants.PREF_WIDTH, width);
            app.prefs.putInteger(AssetConstants.PREF_HEIGHT, height);
            app.prefs.putString(AssetConstants.PREF_RES_STRING, resolution);
            app.prefs.flush();

            // Viktig: Oppdater Stage sitt viewport så UI-en ikke blir strukket
            stage.getViewport().update(width, height, true);

            // Tving tabellen til å rekalkulere posisjoner
            table.invalidateHierarchy();

        } catch (Exception e) {
            Gdx.app.error("Settings", "Kunne ikke endre oppløsning: " + resolution);
        }
    }
}
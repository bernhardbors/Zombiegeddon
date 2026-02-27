package inf112.zoombiegeddon.view.screens;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import inf112.zoombiegeddon.app.GameApp;
import inf112.zoombiegeddon.audio.GameSound;
import inf112.zoombiegeddon.model.animations.AnimationHandler;

public class LoadingScreen extends AbstractScreen {

    private Label loadingLabel;
    private final StringBuilder progressText;
    private boolean isFinished = false;
    private BitmapFont loadingFont; // Midlertidig font for loading-tekst

    public LoadingScreen(GameApp app) {
        super(app);
        this.progressText = new StringBuilder();
    }

    @Override
    public void show() {
        // 1. Lag en midlertidig font for å vise progresjon
        loadingFont = new BitmapFont();
        Label.LabelStyle style = new Label.LabelStyle(loadingFont, Color.WHITE);
        loadingLabel = new Label("Loading... 0%", style);
        loadingLabel.setFontScale(2.0f);

        Table table = new Table();
        table.setFillParent(true);
        table.center().add(loadingLabel);
        stage.addActor(table);

        // 2. Start asynkron lasting
        queueAssets();
    }

    private void queueAssets() {
        app.manager.load(GameApp.UI_SKIN, Skin.class);
        app.manager.load(GameApp.FONT_PATH, BitmapFont.class);
        app.manager.load(GameApp.CHARACTER_SHEET, Texture.class);
        app.manager.load(GameApp.ENEMY_SHEET, Texture.class);
        app.manager.load(GameApp.BG_SKY, Texture.class);
        app.manager.load(GameApp.BG_CITY, Texture.class);
        app.manager.load(GameApp.BG_L1, Texture.class);
        app.manager.load(GameApp.BG_L2, Texture.class);

        for (GameSound sound : GameSound.values()) {
            app.manager.load("assets/audio/sfx/" + sound.getFileName(), Sound.class);
        }
    }

    @Override
    public void render(float delta) {
        // Svart bakgrunn mens vi laster
        ScreenUtils.clear(0, 0, 0, 1);

        // Oppdater AssetManager. Returnerer true når ferdig.
        if (app.manager.update()) {
            if (!isFinished) {
                isFinished = true;
                finalizeLoading();
                app.setScreen(new SplashScreen(app));
            }
        } else {
            // Oppdater tekst uten å lage nye String-objekter
            int progress = (int) (app.manager.getProgress() * 100);
            progressText.setLength(0);
            progressText.append("Loading... ").append(progress).append("%");
            loadingLabel.setText(progressText);
        }

        stage.act(delta);
        stage.draw();
    }

    private void finalizeLoading() {
        // Hent ferdiglastede ressurser til app-containeren
        app.skin = app.manager.get(GameApp.UI_SKIN, Skin.class);
        app.font = app.manager.get(GameApp.FONT_PATH, BitmapFont.class);

        Texture playerSpriteSheet = app.manager.get(GameApp.CHARACTER_SHEET, Texture.class);
        app.playerAnimations = new AnimationHandler(playerSpriteSheet);

        Texture enemySpriteSheet = app.manager.get(GameApp.ENEMY_SHEET, Texture.class);
        app.enemyAnimations = new AnimationHandler(enemySpriteSheet);

        // Konfigurer bakgrunn med parallax-styrke
        app.background.addLayer(app.manager.get(GameApp.BG_SKY, Texture.class), 0.1f);
        app.background.addLayer(app.manager.get(GameApp.BG_CITY, Texture.class), 0.4f);
        app.background.addLayer(app.manager.get(GameApp.BG_L1, Texture.class), 0.7f);
        app.background.addLayer(app.manager.get(GameApp.BG_L2, Texture.class), 1.0f);

        // Oppdater Skin slik at alle widgets bruker riktig font
        app.skin.add("default-font", app.font, BitmapFont.class);
    }

    @Override
    public void hide() {
        // Rydd opp i den midlertidige fonten for å unngå lekkasje
        if (loadingFont != null) loadingFont.dispose();
        stage.clear();
    }
}
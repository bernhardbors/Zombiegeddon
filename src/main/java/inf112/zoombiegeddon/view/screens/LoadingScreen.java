package inf112.zoombiegeddon.view.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.ScreenUtils;
import inf112.zoombiegeddon.app.AssetConstants;
import inf112.zoombiegeddon.app.GameApp;
import inf112.zoombiegeddon.audio.MusicTrack;
import inf112.zoombiegeddon.audio.Sfx;
import inf112.zoombiegeddon.model.animations.AnimationHandler;

/**
 * Displays a loading screen while game assets are being loaded.
 * When loading is complete, the screen initializes shared resources
 * and transitions to the splash screen.
 */
public class LoadingScreen extends AbstractScreen {

    private Label loadingLabel;
    private final StringBuilder progressText;
    private boolean isFinished = false;
    private BitmapFont loadingFont;

    /**
     * Constructs a new loading screen.
     *
     * @param app The game application instance.
     */
    public LoadingScreen(GameApp app) {
        super(app);
        this.progressText = new StringBuilder();
    }

    @Override
    public void show() {
        loadingFont = new BitmapFont();
        Label.LabelStyle style = new Label.LabelStyle(loadingFont, Color.WHITE);
        loadingLabel = new Label("Loading... 0%", style);
        loadingLabel.setFontScale(2.0f);

        Table table = new Table();
        table.setFillParent(true);
        table.center().add(loadingLabel);
        stage.addActor(table);

        queueAssets();
    }

    @Override
    public void buildScreen() {
        clearTable();
    }

    private void queueAssets() {
        app.manager.load(AssetConstants.UI_SKIN, Skin.class);
        app.manager.load(AssetConstants.FONT_PATH, BitmapFont.class);
        app.manager.load(AssetConstants.CHARACTER_MALE_SHEET, Texture.class);
        app.manager.load(AssetConstants.CHARACTER_FEMALE_SHEET, Texture.class);
        app.manager.load(AssetConstants.ZOMBIE_SHEET, Texture.class);
        app.manager.load(AssetConstants.CHONKER_SHEET, Texture.class);
        app.manager.load(AssetConstants.HEALTHBAR, Texture.class);
        app.manager.load(AssetConstants.BG_GAME, Texture.class);
        app.manager.load(AssetConstants.BG_SKY, Texture.class);
        app.manager.load(AssetConstants.BG_CITY, Texture.class);
        app.manager.load(AssetConstants.BG_L1, Texture.class);
        app.manager.load(AssetConstants.BG_L2, Texture.class);

        Sfx.queueAssets(app.manager);
        MusicTrack.queueAssets(app.manager);

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        if (app.manager.update()) {
            if (!isFinished) {
                isFinished = true;
                finalizeLoading();
                app.setScreen(new SplashScreen(app));
            }
        } else {
            int progress = (int) (app.manager.getProgress() * 100);
            progressText.setLength(0);
            progressText.append("Loading... ").append(progress).append("%");
            loadingLabel.setText(progressText);
        }

        stage.act(delta);
        stage.draw();
    }

    private void finalizeLoading() {
        app.skin = app.manager.get(AssetConstants.UI_SKIN, Skin.class);
        app.font = app.manager.get(AssetConstants.FONT_PATH, BitmapFont.class);


        Texture playerMaleSpriteSheet = app.manager.get(AssetConstants.CHARACTER_MALE_SHEET, Texture.class);
        Texture playerFemaleSpriteSheet = app.manager.get(AssetConstants.CHARACTER_FEMALE_SHEET, Texture.class);
        app.playerMaleAnimations = new AnimationHandler(playerMaleSpriteSheet);
        app.playerFemaleAnimations = new AnimationHandler(playerFemaleSpriteSheet);

        Texture enemyZombieSpriteSheet = app.manager.get(AssetConstants.ZOMBIE_SHEET, Texture.class);
        app.zombieAnimations = new AnimationHandler(enemyZombieSpriteSheet);

        Texture enemyChonkerSpriteSheet = app.manager.get(AssetConstants.CHONKER_SHEET, Texture.class);
        app.chonkerAnimations = new AnimationHandler(enemyChonkerSpriteSheet);


        app.background.addLayer(app.manager.get(AssetConstants.HEALTHBAR, Texture.class), 0.1f);
        app.background.addLayer(app.manager.get(AssetConstants.BG_GAME, Texture.class), 0.1f);
        app.background.addLayer(app.manager.get(AssetConstants.BG_SKY, Texture.class), 0.1f);
        app.background.addLayer(app.manager.get(AssetConstants.BG_CITY, Texture.class), 0.4f);
        app.background.addLayer(app.manager.get(AssetConstants.BG_L1, Texture.class), 0.7f);
        app.background.addLayer(app.manager.get(AssetConstants.BG_L2, Texture.class), 1.0f);


        app.skin.add("default-font", app.font, BitmapFont.class);


        app.skin.get(TextButton.TextButtonStyle.class).font = app.font;
        app.skin.get(Label.LabelStyle.class).font = app.font;
    }

    @Override
    public void hide() {
        if (loadingFont != null) loadingFont.dispose();
        stage.clear();
    }
}
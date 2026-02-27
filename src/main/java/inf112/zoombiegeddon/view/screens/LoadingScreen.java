package inf112.zoombiegeddon.view.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.ScreenUtils;
import inf112.zoombiegeddon.app.GameApp;
import inf112.zoombiegeddon.model.animations.AnimationHandler;
import inf112.zoombiegeddon.view.FontGenerator;

public class LoadingScreen extends AbstractScreen {

    private Label loadingLabel;

    private static final float FONTSIZE = 2.0f;

    public LoadingScreen(GameApp app) {
        super(app);
    }

    @Override
    public void buildScreen() {
        super.buildScreen();
        stage.clear();

        BitmapFont defaultFont = new BitmapFont();
        Label.LabelStyle style = new Label.LabelStyle(defaultFont, Color.WHITE);

        this.loadingLabel = new Label("Loading... 0%", style);
        this.loadingLabel.setFontScale(FONTSIZE);

        Table loadingScreenTabel = new Table();
        loadingScreenTabel.setFillParent(true);
        loadingScreenTabel.center().add(this.loadingLabel);

        stage.addActor(loadingScreenTabel);

        if (app.manager.getQueuedAssets() == 0) {
            loadAssets();
        }
    }

    private void loadAssets() {
        app.manager.load(GameApp.UI_SKIN, Skin.class);
        app.manager.load(GameApp.SPLASH_LOGO, Texture.class);
        app.manager.load(GameApp.FONT_PATH, BitmapFont.class);
        app.manager.load(GameApp.CHARACTER_SPRITE_SHEET, Texture.class);
        app.manager.load(GameApp.BACKGROUND_LAYER0_PATH, Texture.class);
        app.manager.load(GameApp.BACKGROUND_LAYER1_PATH, Texture.class);
        app.manager.load(GameApp.BACKGROUND_LAYER2_PATH, Texture.class);
        app.manager.load(GameApp.BACKGROUND_LAYER3_PATH, Texture.class);
    }

    private void addBackgroundLevelImages(){

        app.background.addLayer(app.manager.get(GameApp.BACKGROUND_LAYER0_PATH), 0f);
        app.background.addLayer(app.manager.get(GameApp.BACKGROUND_LAYER1_PATH), 1f);
        app.background.addLayer(app.manager.get(GameApp.BACKGROUND_LAYER2_PATH), 2f);
        app.background.addLayer(app.manager.get(GameApp.BACKGROUND_LAYER3_PATH), 3f);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if (!app.manager.update()) {
            float progress = app.manager.getProgress() * 100;
            this.loadingLabel.setText("Loading... " + (int) progress + "%");
        } else {
            finalizeLoading();
            app.setScreen(new SplashScreen(app));
        }
    }

    @Override
    public void hide() {
        super.hide();
        stage.clear();
    }

    private void finalizeLoading() {

        app.skin = app.manager.get(GameApp.UI_SKIN, Skin.class);
        app.characterSpriteSheet = app.manager.get(GameApp.CHARACTER_SPRITE_SHEET, Texture.class);
        app.font = app.manager.get(GameApp.FONT_PATH, BitmapFont.class);

        app.playerAnimations = new AnimationHandler(app.characterSpriteSheet);

        addBackgroundLevelImages();
        app.skin.add("default-font", app.font, BitmapFont.class);
        app.skin.get(TextButton.TextButtonStyle.class).font = app.font;
        app.skin.get(Label.LabelStyle.class).font = app.font;
    }
}


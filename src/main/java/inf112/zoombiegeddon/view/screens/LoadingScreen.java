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
import inf112.zoombiegeddon.view.FontGenerator;

public class LoadingScreen extends AbstractScreen {

    private Label loadingLabel;

    private static final float FONTSIZE = 2.0f;

    public LoadingScreen(GameApp app) {
        super(app);
    }

    @Override
    public void buildScreen() {

        loadingLabel = new Label("Loading... 0%", new Label.LabelStyle(new com.badlogic.gdx.graphics.g2d.BitmapFont(), Color.WHITE));

        Table loadingScreenTabel = new Table();
        loadingScreenTabel.center();
        loadingScreenTabel.setFillParent(true);

        stage.addActor(loadingScreenTabel);

        loadingLabel.setFontScale(FONTSIZE);
        loadingLabel.setPosition(stage.getWidth() / 2 , stage.getHeight() / 2);

        loadingScreenTabel.add(loadingLabel);

        app.manager.load(GameApp.UI_SKIN, Skin.class);
        app.manager.load(GameApp.SPLASH_LOGO, Texture.class);
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
        ScreenUtils.clear(Color.BLACK);

        if (app.manager.update()) {
            finalizeLoading();
           app.setScreen(new SplashScreen(app));
        } else {
            float progress = app.manager.getProgress() * 100;
            loadingLabel.setText("Loading... " + (int) progress + "%");
        }
        super.render(delta);
    }

    private void finalizeLoading() {
        app.skin = app.manager.get(GameApp.UI_SKIN, Skin.class);
        app.characterSpriteSheet = app.manager.get(GameApp.CHARACTER_SPRITE_SHEET, Texture.class);

        addBackgroundLevelImages();

        app.font = new FontGenerator();
        app.font.create(GameApp.FONT_PATH);

        app.skin.add("default-font", app.font.mainFont, BitmapFont.class);
        app.skin.add("title-font", app.font.titleFont, BitmapFont.class);

        app.skin.get(TextButton.TextButtonStyle.class).font = app.font.mainFont;
        app.skin.get(Label.LabelStyle.class).font = app.font.titleFont;
    }
}


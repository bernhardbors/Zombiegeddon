package inf112.zoombiegeddon.view.screens.loading;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import inf112.zoombiegeddon.app.AssetConstants;
import inf112.zoombiegeddon.app.GameApp;
import inf112.zoombiegeddon.audio.MusicTrack;
import inf112.zoombiegeddon.audio.Sfx;
import inf112.zoombiegeddon.view.screens.AbstractScreen;

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
        app.getAssets().getManager().load(AssetConstants.STORY_IMAGE_01, Texture.class);
        app.getAssets().getManager().load(AssetConstants.STORY_IMAGE_02, Texture.class);
        app.getAssets().getManager().load(AssetConstants.STORY_IMAGE_03, Texture.class);
        app.getAssets().getManager().load(AssetConstants.STORY_IMAGE_04, Texture.class);

        Sfx.queueAssets(app.getAssets().getManager());
        MusicTrack.queueAssets(app.getAssets().getManager());

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        if (app.getAssets().getManager().update()) {
            if (!isFinished) {
                isFinished = true;
                finalizeLoading();
                app.setScreen(new SplashScreen(app));
            }
        } else {
            int progress = (int) (app.getAssets().getManager().getProgress() * 100);
            progressText.setLength(0);
            progressText.append("Loading... ").append(progress).append("%");
            loadingLabel.setText(progressText);
        }

        stage.act(delta);
        stage.draw();
    }

    private void finalizeLoading() {
        app.getAssets().finishLoading();

        //app.getBackground().addLayer(app.getAssets().getManager().get(AssetConstants.HEALTH_BAR, Texture.class), 0.1f);
        app.getBackground().addLayer(app.getAssets().getManager().get(AssetConstants.BG_GAME, Texture.class), 0.1f);
        app.getBackground().addLayer(app.getAssets().getManager().get(AssetConstants.BG_SKY, Texture.class), 0.1f);
        app.getBackground().addLayer(app.getAssets().getManager().get(AssetConstants.BG_CITY, Texture.class), 0.4f);
        app.getBackground().addLayer(app.getAssets().getManager().get(AssetConstants.BG_L1, Texture.class), 0.7f);
        app.getBackground().addLayer(app.getAssets().getManager().get(AssetConstants.BG_L2, Texture.class), 1.0f);

    }

    @Override
    public void hide() {
        if (loadingFont != null) loadingFont.dispose();
        stage.clear();
    }
}
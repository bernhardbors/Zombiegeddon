package inf112.zoombiegeddon.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import inf112.zoombiegeddon.app.GameApp;

public abstract class AbstractMenuScreen implements Screen {

    protected final Stage stage;
    protected final Skin skin;
    protected final GameApp app;
    protected final Table table;
    protected Preferences prefs;
    protected BitmapFont font;

    protected float buttonWidth;
    protected float buttonHeight;


    AbstractMenuScreen(final GameApp app) {

        this.app = app;
        this.stage = new Stage(new ScreenViewport());
        this.skin = app.skin;
        this.table = new Table();
        this.prefs = app.prefs;

        this.font = skin.getFont("default-font");


        table.setFillParent(true);
        stage.addActor(table);

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        calculateButtonSize();
        buildScreen();
    }

    public void buildScreen() {
        table.clear();
    }

    @Override
    public void render(float delta) {

        ScreenUtils.clear(Color.DARK_GRAY);

<<<<<<< HEAD
        //table.background(new TextureRegionDrawable(new Texture(Gdx.files.internal(""))));
=======
        table.background(new TextureRegionDrawable(new Texture(Gdx.files.internal("src/main/resources/assets/tilemap/tilemap.png"))));
>>>>>>> cc12fb992a4c8d76dd1339c379f0f63678064ef2
        stage.act(Math.min(delta, 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);

        calculateButtonSize();

        table.invalidateHierarchy();
        buildScreen();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    public void dispose() {
        stage.dispose();
    }

    private void calculateButtonSize(){
        this.buttonWidth = (float) Gdx.graphics.getWidth() / 5;
        this.buttonHeight = (float) Gdx.graphics.getHeight() / 15;
    }

    protected void DrawHeading(String heading, Color color, Integer Scale) {
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = this.font;
        style.fontColor = color;

        float screenScale = Gdx.graphics.getHeight() / 1080f;

        Label title = new Label(heading, style);
        title.setFontScale(Scale * screenScale);
        table.add(title).row();
    }
}

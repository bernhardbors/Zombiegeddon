package inf112.zoombiegeddon.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import inf112.zoombiegeddon.app.GameApp;

public abstract class AbstractScreen implements Screen {

    protected final Stage stage;
    protected final Skin skin;
    protected final GameApp app;
    protected final Table table;
    protected Preferences prefs;

    protected float buttonWidth;
    protected float buttonHeight;


    AbstractScreen(final GameApp app) {

        this.app = app;
        this.stage = new Stage(new ScreenViewport());
        this.skin = app.skin;
        this.table = new Table();
        this.prefs = app.prefs;



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
        Gdx.app.log("AbstractScreen", "pause() called");
    }

    @Override
    public void resume() {
        Gdx.app.log("AbstractScreen", "resume() called");
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    public void dispose() {
        if (stage != null) {
            stage.dispose();
        }
    }

    private void calculateButtonSize(){
        this.buttonWidth = (float) Gdx.graphics.getWidth() / 5;
        this.buttonHeight = (float) Gdx.graphics.getHeight() / 15;
    }

    protected Label formatText(String text, Color color, float scale) {
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = app.font.titleFont;
        style.fontColor = color;

        float screenScale = Gdx.graphics.getHeight() / 1080f;

        Label textBody = new Label(text, style);
        textBody.setFontScale(scale * screenScale);
        table.add(textBody).row();

        return textBody;
    }
}

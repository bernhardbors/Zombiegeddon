package inf112.skeleton.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import inf112.skeleton.app.GameApp;

public abstract class AbstractMenuScreen implements Screen {

    protected final Stage stage;
    protected final Skin skin;
    protected final GameApp app;
    protected final Table table;
    protected Preferences prefs;


    AbstractMenuScreen(final GameApp app) {
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
}

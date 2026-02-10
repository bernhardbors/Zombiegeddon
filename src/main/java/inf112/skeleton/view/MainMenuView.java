package inf112.skeleton.view;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import inf112.skeleton.app.GameApp;
import inf112.skeleton.utils.UIUtils;

public class MainMenuView extends ScreenAdapter {

    private final Stage stage;

    public MainMenuView(final GameApp app) {
        this.stage = new Stage(new ScreenViewport(), app.batch);
        Skin skin = app.skin;
        Table table = new Table();

        table.setFillParent(true);
        stage.addActor(table);

        float buttonWidth = (float) Gdx.graphics.getWidth() / 5;
        float buttonHeight = (float) Gdx.graphics.getHeight() / 15;

        UIUtils.addButton("Start", skin, table,buttonWidth,buttonHeight,() -> Gdx.app.exit());
        UIUtils.addButton("Settings", skin, table,buttonWidth,buttonHeight,() -> Gdx.app.exit());
        UIUtils.addButton("Exit", skin, table,buttonWidth,buttonHeight,() -> Gdx.app.exit());
    }


    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
package inf112.zoombiegeddon.view.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import inf112.zoombiegeddon.app.GameApp;
import inf112.zoombiegeddon.model.WorldPhysics;

public abstract class AbstractLevel extends ScreenAdapter {

    protected final GameApp app;
    protected final OrthographicCamera camera;
    protected final FitViewport viewport;

    protected final WorldPhysics worldPhysics;

    protected final Box2DDebugRenderer debugRenderer;

    AbstractLevel(GameApp app) {
        this.app = app;
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);

        this.debugRenderer = new Box2DDebugRenderer();

        this.worldPhysics = new WorldPhysics(new Vector2(0f, 0f));
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.LIGHT_GRAY);

        app.batch.setProjectionMatrix(camera.combined);

        app.batch.begin();
        draw(app.batch);
        app.batch.end();

    }

    protected abstract void draw(SpriteBatch batch);

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        debugRenderer.dispose();
        worldPhysics.dispose();
    }

}
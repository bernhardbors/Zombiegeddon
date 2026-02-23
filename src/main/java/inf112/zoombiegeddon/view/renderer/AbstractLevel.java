package inf112.zoombiegeddon.view.renderer;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import inf112.zoombiegeddon.app.GameApp;
import inf112.zoombiegeddon.model.WorldPhysics;

public abstract class AbstractLevel extends ScreenAdapter {

    protected final GameApp app;
    protected final OrthographicCamera camera;
    protected final ExtendViewport viewport;

    protected final WorldPhysics worldPhysics;

    protected OrthogonalTiledMapRenderer mapRenderer;

    public AbstractLevel(GameApp app, Vector2 gravity) {
        this.app = app;
        this.camera = new OrthographicCamera();
        this.worldPhysics = new WorldPhysics(gravity);

        this.viewport = new ExtendViewport(10,10,camera);
    }

    @Override
    public void render(float delta) {
        update(delta);

        ScreenUtils.clear(Color.LIGHT_GRAY);


        if (mapRenderer != null) {
            mapRenderer.setView(camera);
            mapRenderer.render();
        }

        app.batch.setProjectionMatrix(camera.combined);
        app.batch.begin();
        draw(app.batch);
        app.batch.end();
    }

    protected void update(float delta) {
        worldPhysics.update(delta);
    }

    protected abstract void draw(SpriteBatch batch);

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        mapRenderer.dispose();
        worldPhysics.dispose();
    }

}
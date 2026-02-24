package inf112.zoombiegeddon.view.renderer;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import inf112.zoombiegeddon.app.GameApp;
import inf112.zoombiegeddon.model.WorldPhysics;

public abstract class AbstractLevel extends ScreenAdapter {

    protected final GameApp app;
    protected final OrthographicCamera camera;
    protected final ExtendViewport viewport;

    protected final WorldPhysics worldPhysics;

    protected OrthogonalTiledMapRenderer mapRenderer;
    protected final Box2DDebugRenderer debugRenderer;

    private final Boolean debugMode;

    public AbstractLevel(GameApp app, Vector2 gravity, Integer cameraZoom, boolean debugMode) {
        this.app = app;
        this.camera = new OrthographicCamera();
        this.worldPhysics = new WorldPhysics(gravity);
        this.viewport = new ExtendViewport(cameraZoom,cameraZoom,camera);
        this.debugRenderer = new Box2DDebugRenderer();
        this.debugMode = debugMode;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.LIGHT_GRAY);
        update(delta);
        renderMap();

        app.batch.begin();
        app.batch.setProjectionMatrix(camera.combined);
        draw(app.batch);
        app.batch.end();
    }

    /**
     * if mapRenderer not null then render the map and set view to camera
     * if debugMode = true then render the debug hit boxes
     */
    private void renderMap() {
        if (mapRenderer != null) {
            mapRenderer.setView(camera);
            mapRenderer.render();
        }
        if (debugMode) {
            debugRenderer.render(worldPhysics.getWorld(), camera.combined);
        }
    }

    protected void update(float delta) {
        worldPhysics.update(delta);
    }

    protected abstract void draw(SpriteBatch batch);

    protected abstract void drawMapLayer(String file, String layerName);

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        mapRenderer.dispose();
        worldPhysics.dispose();
        debugRenderer.dispose();
    }
}
package inf112.zoombiegeddon.view.renderer;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import inf112.zoombiegeddon.app.GameApp;
import inf112.zoombiegeddon.model.world.WorldCollisionLayerCreator;
import inf112.zoombiegeddon.model.world.WorldPhysics;


/**
 * Base class for all game levels. Handles the boilerplate for physics updates,
 * camera management, Tiled map rendering, and debug visualization.
 * <p>
 * Subclasses should implement {@link #draw(SpriteBatch)} for entity rendering
 * and {@link #drawMapCollisionLayer(String, String)} for map initialization.
 * </p>
 */
public abstract class AbstractLevel extends ScreenAdapter {

    protected final GameApp app;
    protected final OrthographicCamera camera;
    protected final ExtendViewport viewport;

    protected final WorldPhysics worldPhysics;

    protected OrthogonalTiledMapRenderer mapRenderer;
    protected final Box2DDebugRenderer debugRenderer;

    protected TiledMap map;
    protected final float PIXEL_PR_TILE = 32.0f;

    private final Boolean debugMode;



    /**
     * @param app         The main GameApp instance for shared resources (batch, assets).
     * @param gravity     Gravity vector for this level's physics world.
     * @param worldWidth  The base width of the world in meters/units.
     * @param worldHeight The base height of the world in meters/units.
     * @param debugMode   If true, Box2D hitboxes will be rendered.
     */
    public AbstractLevel(GameApp app, Vector2 gravity, float worldWidth, float worldHeight, boolean debugMode) {
        this.app = app;
        this.debugMode = debugMode;
        this.camera = new OrthographicCamera();
        this.worldPhysics = new WorldPhysics(gravity);
        this.debugRenderer = new Box2DDebugRenderer();
        this.viewport = new ExtendViewport(worldWidth, worldHeight, camera);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.LIGHT_GRAY);
        update(delta);
        camera.update();

        app.batch.setProjectionMatrix(camera.combined);
        app.batch.begin();
        app.background.render(app.batch);
        app.batch.end();

        renderMap();

        app.batch.begin();
        draw(app.batch);
        app.batch.end();
    }

    /**
     * Renders the Tiled map and, if enabled, the Box2D debug shapes.
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

    /**
     * Updates physics and level logic.
     *
     * @param delta Time elapsed since the last frame.
     */
    protected void update(float delta) {
        worldPhysics.update(delta);
    }

    /**
     * Implement this to draw level-specific entities (Players, Zombies, etc.).
     * This is called inside a SpriteBatch begin/end block.
     *
     * @param batch The shared SpriteBatch from GameApp.
     */
    protected abstract void draw(SpriteBatch batch);

    /**
     * Loads a Tiled map (.tmx) and initializes physics collision boundaries for a specific layer.
     * <p>
     * If the map has not been loaded yet, this method initializes the {@code TmxMapLoader}
     * and {@code OrthogonalTiledMapRenderer} using the defined {@code PIXEL_PR_TILE} scale.
     * It then uses {@link WorldCollisionLayerCreator} to transform Tiled map objects into
     * static Box2D bodies.
     * </p>
     *
     * @param file      The internal path to the .tmx map file (e.g., "maps/level1.tmx").
     * @param layerName The name of the object layer in Tiled to be converted into physics collisions.
     */
    protected void drawMapCollisionLayer(String file, String layerName) {
        if (map == null) {
            map = new TmxMapLoader().load(file);
            mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / PIXEL_PR_TILE);
        }
        new WorldCollisionLayerCreator(worldPhysics.getWorld(), map, layerName, PIXEL_PR_TILE);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        if (mapRenderer != null) mapRenderer.dispose();
        if (map != null) map.dispose(); // IMPORTANT: TiledMaps are not handled by Java GC
        if (debugRenderer != null) debugRenderer.dispose();
        worldPhysics.dispose();
    }
}
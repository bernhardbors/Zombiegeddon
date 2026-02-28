package inf112.zoombiegeddon.view.renderer;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Disposable;
import inf112.zoombiegeddon.model.world.physics.WorldPhysics;

/**
 * Responsible for rendering the game world, including the tiled map
 * and physics debug shapes.
 */
public class WorldRenderer implements Disposable {

    private final OrthographicCamera camera;
    private OrthogonalTiledMapRenderer mapRenderer;
    private final Box2DDebugRenderer debugRenderer;
    private final boolean debugMode;

    /**
     * Constructs a new WorldRenderer.
     *
     * @param camera    The camera used to view the world.
     * @param debugMode Whether to render Box2D physics debug outlines.
     */
    public WorldRenderer(OrthographicCamera camera, boolean debugMode) {
        this.camera = camera;
        this.debugRenderer = new Box2DDebugRenderer();
        this.debugMode = debugMode;
    }

    /**
     * Initializes or updates the tiled map renderer with a new map and scale.
     *
     * @param map       The TiledMap to be rendered.
     * @param unitScale The scaling factor (e.g., 1/32f for 32px tiles).
     */
    public void setMap(TiledMap map, float unitScale) {
        if (mapRenderer != null) mapRenderer.dispose();
        this.mapRenderer = new OrthogonalTiledMapRenderer(map, unitScale);
    }

    /**
     * Renders the tiled map and, if enabled, the physics debug information.
     *
     * @param delta        The time in seconds since the last frame.
     * @param worldPhysics The physics world containing the bodies to debug.
     */
    public void render(float delta, WorldPhysics worldPhysics) {
        camera.update();

        if (mapRenderer != null) {
            mapRenderer.setView(camera);
            mapRenderer.render();
        }

        if (debugMode && worldPhysics != null) {
            debugRenderer.render(worldPhysics.getWorld(), camera.combined);
        }
    }

    /**
     * Disposes of the map renderer and debug renderer to free up memory.
     */
    @Override
    public void dispose() {
        if (mapRenderer != null) mapRenderer.dispose();
        debugRenderer.dispose();
    }
}
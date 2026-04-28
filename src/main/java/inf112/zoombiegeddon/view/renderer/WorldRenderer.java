package inf112.zoombiegeddon.view.renderer;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Disposable;
import inf112.zoombiegeddon.model.world.physics.WorldPhysics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Responsible for rendering the game world, including the tiled map
 * and physics debug shapes.
 */
public class WorldRenderer implements Disposable {

    private static final Logger logger = LoggerFactory.getLogger(WorldRenderer.class);

    private final OrthographicCamera camera;
    private OrthogonalTiledMapRenderer mapRenderer;
    private final Box2DDebugRenderer debugRenderer;

    private int[] backgroundLayers;
    private int[] foregroundLayers;

    /**
     * Constructs a new WorldRenderer.
     *
     * @param camera    The camera used to view the world.
     */
    public WorldRenderer(OrthographicCamera camera) {
        this.camera = camera;
        this.debugRenderer = new Box2DDebugRenderer();
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

        splitLayers(map);
    }

    private void splitLayers(TiledMap map) {
        List<Integer> bg = new ArrayList<>();
        List<Integer> fg = new ArrayList<>();

        for (int i = 0; i < map.getLayers().getCount(); i++) {
            MapLayer layer = map.getLayers().get(i);
            logger.info("{}", layer.getName());
            if (layer.getName().contains("Foreground")) {
                fg.add(i);
            } else {
                bg.add(i);
            }
        }

        backgroundLayers = bg.stream().mapToInt(i -> i).toArray();
        foregroundLayers = fg.stream().mapToInt(i -> i).toArray();
    }

    /**
     * Renders the foreground map layers.
     */
    public void renderForeground() {
        if (mapRenderer != null && foregroundLayers != null) {

            mapRenderer.render(foregroundLayers);
        }
    }

    /**
     * Renders the background map layers.
     */
    public void renderBackground() {
        if (mapRenderer != null && backgroundLayers != null) {
            
            camera.update();
            mapRenderer.setView(camera);
            mapRenderer.render(backgroundLayers);
        }
    }


    /**
     * Renders the Box2D debug view if debug mode is enabled.
     *
     * @param worldPhysics The physics world to render.
     * @param debugMode True to render debug output, false otherwise.
     */
    public void renderDebug(WorldPhysics worldPhysics, boolean debugMode) {
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
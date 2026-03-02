package inf112.zoombiegeddon.view.renderer;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import inf112.zoombiegeddon.app.GameApp;

import inf112.zoombiegeddon.audio.AudioService;
import inf112.zoombiegeddon.controller.PlayerController;
import inf112.zoombiegeddon.model.world.physics.WorldCollisionLayerCreator;
import inf112.zoombiegeddon.model.world.physics.WorldPhysics;

/**
 * Abstract base class for all game levels.
 * Provides common functionality for physics simulation, world rendering,
 * camera management, and map loading within the LibGDX framework.
 */
public abstract class AbstractLevel extends ScreenAdapter {

    protected final GameApp app;
    protected final ExtendViewport viewport;
    protected final WorldPhysics worldPhysics;
    protected final WorldRenderer worldRenderer;
    protected final AudioService audio;
    protected PlayerController controller;
    protected OrthographicCamera camera;
    protected TiledMap map;
    protected final float PIXEL_PR_TILE = 32.0f;

    /**
     * Constructs a new AbstractLevel with the specified game configuration.
     *
     * @param app         The main game application instance.
     * @param gravity     The gravity vector for the physics world.
     * @param worldWidth  The width of the world in units.
     * @param worldHeight The height of the world in units.
     * @param debugMode   Whether to enable physics debug rendering.
     */
    public AbstractLevel(GameApp app, Vector2 gravity, float worldWidth, float worldHeight, boolean debugMode) {
        this.app = app;
        this.worldPhysics = new WorldPhysics(gravity);
        this.camera = new OrthographicCamera(worldWidth, worldHeight);
        this.audio = app.audio;
        this.worldRenderer = new WorldRenderer(camera, debugMode);
        this.viewport = new ExtendViewport(worldWidth, worldHeight, camera);
    }

    /**
     * Main render loop. Handles updating the physics world and drawing
     * the background, world layers, and entities.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        app.batch.setProjectionMatrix(camera.combined);

        update(delta);
        drawBackground();
        drawWorld(delta);
        drawEntities();
    }

    /**
     * Updates the physics simulation.
     *
     * @param delta The time step in seconds.
     */
    protected void update(float delta) {
        worldPhysics.update(delta);
    }

    /**
     * Updates the viewport dimensions when the window size changes.
     *
     * @param width  New window width.
     * @param height New window height.
     */
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    /**
     * Renders the background using the application's background renderer.
     */
    private void drawBackground() {
        app.batch.begin();
        app.background.render(app.batch);
        app.batch.end();
    }

    /**
     * Renders the tiled map and physics debug lines.
     *
     * @param delta The time in seconds since the last render.
     */
    private void drawWorld(Float delta) {
        worldRenderer.render(delta, worldPhysics);
    }

    /**
     * Renders all game entities within a SpriteBatch block.
     */
    private void drawEntities() {
        app.batch.begin();
        draw(app.batch);
        app.batch.end();
    }

    /**
     * Abstract method for subclasses to implement specific entity drawing logic.
     *
     * @param batch The SpriteBatch used for drawing.
     */
    protected abstract void draw(SpriteBatch batch);

    /**
     * Loads a TiledMap (.tmx) and initializes the collision layer.
     *
     * @param file               Path to the map file.
     * @param collisionLayerName Name of the layer used for physics collisions.
     */
    protected void loadMap(String file, String collisionLayerName) {
        if (map == null) {
            map = new TmxMapLoader().load(file);
            worldRenderer.setMap(map, 1 / PIXEL_PR_TILE);
        }
        new WorldCollisionLayerCreator(worldPhysics.getWorld(), map, collisionLayerName, PIXEL_PR_TILE);
    }

    /**
     * Disposes of all resources to prevent memory leaks.
     */
    @Override
    public void dispose() {
        if (map != null) map.dispose();
        worldPhysics.dispose();
        worldRenderer.dispose();
    }
}
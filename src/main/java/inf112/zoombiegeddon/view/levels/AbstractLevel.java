package inf112.zoombiegeddon.view.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import inf112.zoombiegeddon.app.GameApp;

import inf112.zoombiegeddon.audio.AudioService;
import inf112.zoombiegeddon.controller.PlayerController;
import inf112.zoombiegeddon.model.world.physics.WorldCollisionLayerCreator;
import inf112.zoombiegeddon.model.world.physics.WorldPhysics;
import inf112.zoombiegeddon.view.renderer.WorldRenderer;

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

    public static final float PIXELS_PER_TILE = 8f;
    public static final float UNIT_SCALE = 1 / PIXELS_PER_TILE;
    protected final float VIRTUAL_WIDTH = 50f;
    protected final float VIRTUAL_HEIGHT = 25f;

    protected ShaderProgram shader;

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
        this.camera = new OrthographicCamera();
        this.viewport = new ExtendViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);
        this.worldPhysics = new WorldPhysics(gravity);
        this.worldRenderer = new WorldRenderer(camera, debugMode);
        this.audio = app.audio;

        // shaders test
        String vertexShader = Gdx.files.internal("assets/shaders/vertex.glsl").readString();
        String fragmentShader = Gdx.files.internal("assets/shaders/fragment.glsl").readString();

        shader = new ShaderProgram(vertexShader, fragmentShader);
    }

    /**
     * Main render loop. Handles updating the physics world and drawing
     * the background, world layers, and entities.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        update(delta);

        app.batch.setProjectionMatrix(camera.combined);
        drawParrallaxBackground();
        worldRenderer.renderBackground();
        drawEntities();
        worldRenderer.renderForeground();

        //shader
        //app.batch.setShader(shader);
        //app.batch.setShader(null);

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
    private void drawParrallaxBackground() {
        app.batch.begin();
        app.background.render(app.batch);
        app.batch.end();
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
     * Draws text to the screen while playing
     *
     * @param text The text wanted to show up on the screen
     * @param color The color of the text
     * @param scale The wanted scale of the text
     */
    public Label drawText(String text, Color color, float scale){
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = app.font;
        style.fontColor = color;

        style.font.setUseIntegerPositions(false);
        Label textBody = new Label(text, style);
        textBody.setFontScale(scale);

        return textBody;

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
            worldRenderer.setMap(map, UNIT_SCALE);
        }
        new WorldCollisionLayerCreator(worldPhysics.getWorld(), map, collisionLayerName,PIXELS_PER_TILE);
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
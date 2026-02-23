package inf112.zoombiegeddon.view.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import inf112.zoombiegeddon.app.GameApp;
import inf112.zoombiegeddon.controller.PlayerController;
import inf112.zoombiegeddon.model.Player;
import inf112.zoombiegeddon.model.WorldLayerCreator;
import inf112.zoombiegeddon.utils.CameraUtils;
import inf112.zoombiegeddon.view.renderer.AbstractLevel;

public class TestLevel01 extends AbstractLevel {

    public final float PIXEL_PR_TILE = 32.0f;
    protected final Box2DDebugRenderer debugRenderer;
    protected TiledMap map;

    private final Player player;
    private final PlayerController controller;

    public TestLevel01(GameApp app, Vector2 gravity) {
        super(app, gravity);

        map = new TmxMapLoader().load("src/main/resources/assets/TestMapAndTilesets+PlayerDevSprite/TestMap.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / PIXEL_PR_TILE);

        this.player = new Player(worldPhysics.getWorld());

        this.controller = new PlayerController(player);

        new WorldLayerCreator(worldPhysics.getWorld(), map, "collision", PIXEL_PR_TILE);
        this.debugRenderer = new Box2DDebugRenderer();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(controller);
    }

    @Override
    public void draw(SpriteBatch batch) {
        player.draw(batch);
    }

    @Override
    protected void update(float delta) {
        super.update(delta);
        controller.update(delta);
        player.update();

        CameraUtils.updateCamera(player, camera);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        debugRenderer.render(worldPhysics.getWorld(), camera.combined);
    }

    @Override
    public void dispose() {
        super.dispose();
        debugRenderer.dispose();
    }

}

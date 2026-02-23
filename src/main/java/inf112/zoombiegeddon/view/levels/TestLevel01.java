package inf112.zoombiegeddon.view.levels;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import inf112.zoombiegeddon.app.GameApp;
import inf112.zoombiegeddon.model.WorldLayerCreator;
import inf112.zoombiegeddon.model.WorldPhysics;
import inf112.zoombiegeddon.view.renderer.AbstractLevel;

public class TestLevel01 extends AbstractLevel {

    public final float PIXEL_PR_TILE = 32.0f;
    protected final Box2DDebugRenderer debugRenderer;
    protected TiledMap map;

    public TestLevel01(GameApp app, Vector2 gravity) {
        super(app, gravity);

        map = new TmxMapLoader().load("src/main/resources/assets/TestMapAndTilesetsForDEV/TestMap.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / PIXEL_PR_TILE);

        // TODO: create player


        // generate hitboxes
        new WorldLayerCreator(worldPhysics.getWorld(), map, "collision");
        this.debugRenderer = new Box2DDebugRenderer();
    }

    @Override
    public void draw(SpriteBatch batch) {

    }

    @Override
    protected void update(float delta) {
        super.update(delta);
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

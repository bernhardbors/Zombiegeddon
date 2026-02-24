package inf112.zoombiegeddon.view.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import inf112.zoombiegeddon.app.GameApp;
import inf112.zoombiegeddon.controller.PlayerController;
import inf112.zoombiegeddon.model.Player;
import inf112.zoombiegeddon.model.WorldCollisionLayerCreator;
import inf112.zoombiegeddon.utils.CameraUtils;
import inf112.zoombiegeddon.view.renderer.AbstractLevel;

public class TestLevel01 extends AbstractLevel {

    public final float PIXEL_PR_TILE = 32.0f;
    protected TiledMap map;

    private final Player player;
    private final PlayerController controller;
    public TextureRegion playerIdle;

    public TestLevel01(GameApp app, Vector2 gravity) {
        super(app, gravity, 10, true);

        TextureRegion[][] allFrames = TextureRegion.split(app.characterSpriteSheet, 32, 32);
        this.playerIdle = allFrames[0][0];

        this.player = new Player(worldPhysics.getWorld(), playerIdle, new Vector2(10,10));

        this.controller = new PlayerController(player);

        drawMapLayer("src/main/resources/assets/TestMapAndTilesets+PlayerDevSprite/TestMap.tmx", "collision");
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
    protected void drawMapLayer(String file, String layerName) {
        if (map == null) {
            map = new TmxMapLoader().load(file);
            mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / PIXEL_PR_TILE);
        }
        new WorldCollisionLayerCreator(worldPhysics.getWorld(), map, layerName, PIXEL_PR_TILE);
    }

    @Override
    protected void update(float delta) {
        super.update(delta);

        controller.update(delta);
        player.update(delta);

        CameraUtils.updateCamera(player, camera);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }

    @Override
    public void dispose() {
        super.dispose();
    }

}

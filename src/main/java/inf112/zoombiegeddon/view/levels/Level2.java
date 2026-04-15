package inf112.zoombiegeddon.view.levels;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import inf112.zoombiegeddon.app.GameApp;
import inf112.zoombiegeddon.controller.PlayerController;
import inf112.zoombiegeddon.model.entity.Player;
import inf112.zoombiegeddon.model.entity.enemies.Enemy;
import inf112.zoombiegeddon.model.entity.enemies.EnemyFactory;
import inf112.zoombiegeddon.model.entity.enemies.IEnemyFactory;
import inf112.zoombiegeddon.model.world.Spawner;
import inf112.zoombiegeddon.model.world.luckyblock.LuckyBlockLayerFactory;
import inf112.zoombiegeddon.model.world.physics.WorldCollisionLayerCreator;
import inf112.zoombiegeddon.view.renderer.EntityRenderer;
import inf112.zoombiegeddon.view.renderer.UIRenderer;
import inf112.zoombiegeddon.model.world.triggers.TriggerZone;

/**
 * A concrete implementation of a game level used for testing purposes.
 * This level initializes a player, sets up input handling, and manages
 * camera movement relative to the player's position.
 */
public class Level2 extends AbstractLevel {

    private TiledMap map;
    private Spawner spawner;
    private LuckyBlockLayerFactory luckyBlockLayerFactory;


    /**
     * Constructs level 3  and initializes its world, player, UI, enemies, and triggers.
     *
     * @param app The game application instance.
     */
    public Level2(GameApp app) {

        super(app, new OrthographicCamera(), new Vector2(0,-15.0f));
        loadMap("tilemaps/level3.tmx", "Collision");
        this.spawner = new Spawner(map);
        this.player = new Player(physics.getWorld(), spawner.getSpawnPoint("player_spawn"), app.getAssets().getPlayerMaleAnimations(),  3);
        this.playerController = new PlayerController(player);

        this.entityRenderer = new EntityRenderer(app);
        this.UIrenderer = new UIRenderer(camera, player);
        this.triggerZone = new TriggerZone(map, "Triggers");

        IEnemyFactory enemyFactory = new EnemyFactory(physics.getWorld());
        this.enemies = enemyFactory.createEnemiesFromLayer(
                map.getLayers().get("SpawnPoints"),
                app.getAssets().getZombieAnimations(),
                app.getAssets().getChonkerAnimations()
        );
    }

    @Override
    public void loadMap(String fileName, String collisionLayerName) {
        if (map == null) {
            map = new TmxMapLoader().load(fileName);
            worldRenderer.setMap(map, UNIT_SCALE);
        }
        new WorldCollisionLayerCreator(physics.getWorld(), map, collisionLayerName,PIXELS_PER_TILE);
        luckyBlockLayerFactory = new LuckyBlockLayerFactory(physics.getWorld(), map, "Luckyblocks", PIXELS_PER_TILE);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
    }

    @Override
    public void draw(Batch batch) {

        drawParallaxBackground();

        worldRenderer.renderBackground();
        if (luckyBlockLayerFactory != null) {
            batch.setProjectionMatrix(camera.combined);
            batch.begin();
            luckyBlockLayerFactory.render(batch, UNIT_SCALE);
            batch.end();
        }
        worldRenderer.renderDebug(physics,false);

        if (player != null) drawEntities(player);

        for(Enemy enemy : enemies) {
            drawEntities(enemy);
        }

        drawPickups();

        worldRenderer.renderForeground();

        renderTriggerText();
    }
}
package inf112.zoombiegeddon.model.levels;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import inf112.zoombiegeddon.app.GameApp;
import inf112.zoombiegeddon.model.entity.IEntity;
import inf112.zoombiegeddon.model.entity.IPlayer;
import inf112.zoombiegeddon.model.entity.Player;
import inf112.zoombiegeddon.model.entity.enemies.EnemyFactory;
import inf112.zoombiegeddon.model.entity.enemies.IEnemy;
import inf112.zoombiegeddon.model.entity.enemies.IEnemyFactory;
import inf112.zoombiegeddon.model.entity.pickups.HealPickup;
import inf112.zoombiegeddon.model.entity.pickups.SpeedBoostPickup;
import inf112.zoombiegeddon.model.world.Spawner;
import inf112.zoombiegeddon.model.world.luckyblock.LuckyBlockLayerFactory;
import inf112.zoombiegeddon.model.world.physics.WorldCollisionLayerCreator;
import inf112.zoombiegeddon.model.world.physics.WorldPhysics;
import inf112.zoombiegeddon.model.world.triggers.TriggerZone;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LevelModel {

    private WorldPhysics physics;
    private IPlayer player;
    private List<IEnemy> enemies;
    private TriggerZone triggerZone;
    private Spawner spawner;
    private LuckyBlockLayerFactory luckyBlockLayerFactory;

    private float levelTime = 0f;
    private String currentText = null;

    public static final float PIXELS_PER_TILE = 8.0f;
    public static final float UNIT_SCALE = 1 / PIXELS_PER_TILE;

    public LevelModel(GameApp app, TiledMap map) {
        this.physics = new WorldPhysics(new Vector2(0, -15.0f));

        this.physics.registerPickupFactory("speed", spawn -> new SpeedBoostPickup(physics.getWorld(), spawn));
        this.physics.registerPickupFactory("heal", spawn -> new HealPickup(physics.getWorld(), spawn));

        new WorldCollisionLayerCreator(physics.getWorld(), map, "Collision", PIXELS_PER_TILE);
        this.luckyBlockLayerFactory = new LuckyBlockLayerFactory(physics.getWorld(), map, "Luckyblocks", LevelModel.PIXELS_PER_TILE);

        this.spawner = new Spawner(map, UNIT_SCALE);
        this.player = new Player(physics.getWorld(), spawner.getSpawnPoint("player_spawn"), app.getAssets().getPlayerMaleAnimations(), 3);

        this.triggerZone = new TriggerZone(map, "Triggers", UNIT_SCALE);
        IEnemyFactory enemyFactory = new EnemyFactory(physics.getWorld());
        this.enemies = enemyFactory.createEnemiesFromLayer(
                map.getLayers().get("SpawnPoints"),
                app.getAssets().getZombieAnimations(),
                app.getAssets().getChonkerAnimations()
        );
    }

    public void update(float delta) {
        physics.update(delta);
        levelTime += delta;

        if (player != null) {
            player.update(delta);

            if (triggerZone != null) {
                Rectangle playerBounds = new Rectangle(
                        player.getPosition().x - player.getWidth() / 2f,
                        player.getPosition().y - player.getHeight() / 2f,
                        player.getWidth(),
                        player.getHeight()
                );
                currentText = triggerZone.update(playerBounds, player);
            }
        }

        Iterator<IEnemy> iterator = enemies.iterator();
        while (iterator.hasNext()) {
            IEnemy enemy = iterator.next();
            enemy.update(delta);

            if (enemy.isDead()) {
                physics.getWorld().destroyBody(enemy.getPhysics().getBody());
                iterator.remove();
            }
        }
    }

    public List<IEntity> getAllEntities() {
        List<IEntity> all = new ArrayList<>();
        if (player != null) all.add(player);
        all.addAll(enemies);
        return all;
    }

    public IPlayer getPlayer() { return player; }
    public List<IEnemy> getEnemies() { return enemies; }
    public WorldPhysics getPhysics() { return physics; }
    public String getCurrentText() { return currentText; }
    public float getLevelTime() { return levelTime; }
    public TriggerZone getTriggerZone() { return triggerZone; }
    public LuckyBlockLayerFactory getLuckyBlockFactory() { return luckyBlockLayerFactory; }
}
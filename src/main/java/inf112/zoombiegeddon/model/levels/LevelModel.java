package inf112.zoombiegeddon.model.levels;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import inf112.zoombiegeddon.app.GameConstants;
import inf112.zoombiegeddon.model.entity.IEntity;
import inf112.zoombiegeddon.model.entity.player.IPlayer;
import inf112.zoombiegeddon.model.entity.player.Player;
import inf112.zoombiegeddon.model.entity.enemies.AbstractEnemyFactory;
import inf112.zoombiegeddon.model.entity.enemies.IAbstractEnemyFactory;
import inf112.zoombiegeddon.model.entity.enemies.IEnemy;
import inf112.zoombiegeddon.model.entity.pickups.HealPickup;
import inf112.zoombiegeddon.model.entity.pickups.SpeedBoostPickup;
import inf112.zoombiegeddon.model.world.ISpawner;
import inf112.zoombiegeddon.model.world.Spawner;
import inf112.zoombiegeddon.model.world.luckyblock.LuckyBlockLayerFactory;
import inf112.zoombiegeddon.model.world.physics.IWorldPhysics;
import inf112.zoombiegeddon.model.world.physics.WorldCollisionLayerCreator;
import inf112.zoombiegeddon.model.world.physics.WorldPhysics;
import inf112.zoombiegeddon.model.world.triggers.TriggerZone;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The model for a single game level, owning all runtime state: physics,
 * player, enemies, trigger zones, and pickups.
 */
public class LevelModel implements ILevelModel {

    private final IWorldPhysics physics;
    private final IPlayer player;
    private final List<IEnemy> enemies;
    private final TriggerZone triggerZone;
    private final ISpawner spawner;
    private final LuckyBlockLayerFactory luckyBlockLayerFactory;

    private float levelTime = 0f;
    private String currentText = null;


    /**
     * Constructs and initializes the level from the given Tiled map.
     *
     * @param map the Tiled map describing the level layout and spawn points
     */
    public LevelModel(TiledMap map) {
        this.physics = new WorldPhysics(new Vector2(0, -15.0f));

        this.physics.registerPickupFactory("speed",
                spawn -> new SpeedBoostPickup(physics.getWorld(), spawn));
        this.physics.registerPickupFactory("heal",
                spawn -> new HealPickup(physics.getWorld(), spawn));

        new WorldCollisionLayerCreator(physics.getWorld(), map, "Collision", GameConstants.PIXELS_PER_TILE);
        this.luckyBlockLayerFactory = new LuckyBlockLayerFactory(
                physics.getWorld(), map, "Luckyblocks", GameConstants.PIXELS_PER_TILE);

        this.spawner = new Spawner(map, GameConstants.UNIT_SCALE);
        this.player = new Player(physics.getWorld(), spawner.getSpawnPoint("player_spawn"), 3);

        this.triggerZone = new TriggerZone(map, "Triggers", GameConstants.UNIT_SCALE);

        IAbstractEnemyFactory enemyFactory = new AbstractEnemyFactory(physics.getWorld());
        this.enemies = enemyFactory.createEnemiesFromLayer(
                map.getLayers().get("SpawnPoints"));
    }

    @Override
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
                physics.destroyBody(enemy.getPhysics());
                iterator.remove();
            }
        }
    }

    @Override
    public List<IEntity> getAllEntities() {
        List<IEntity> all = new ArrayList<>();
        if (player != null) all.add(player);
        all.addAll(enemies);
        return all;
    }

    @Override
    public IPlayer getPlayer() { return player; }
    @Override
    public List<IEnemy> getEnemies() { return enemies; }
    @Override
    public IWorldPhysics getPhysics() { return physics; }
    @Override
    public String getCurrentText() { return currentText; }
    @Override
    public float getLevelTime() { return levelTime; }
    @Override
    public TriggerZone getTriggerZone() { return triggerZone; }
    @Override
    public LuckyBlockLayerFactory getLuckyBlockFactory() { return luckyBlockLayerFactory; }
}
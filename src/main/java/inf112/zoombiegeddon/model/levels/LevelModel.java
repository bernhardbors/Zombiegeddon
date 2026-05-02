package inf112.zoombiegeddon.model.levels;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import inf112.zoombiegeddon.model.entity.IEntity;
import inf112.zoombiegeddon.model.entity.player.IPlayer;
import inf112.zoombiegeddon.model.entity.player.Player;
import inf112.zoombiegeddon.model.entity.enemies.AbstractEnemyFactory;
import inf112.zoombiegeddon.model.entity.enemies.IAbstractEnemyFactory;
import inf112.zoombiegeddon.model.entity.enemies.IEnemy;
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

/**
 * The model for a single game level, owning all runtime state: physics,
 * player, enemies, trigger zones, and pickups.
 */
public class LevelModel {

    private final WorldPhysics physics;
    private final IPlayer player;
    private final List<IEnemy> enemies;
    private final TriggerZone triggerZone;
    private final LuckyBlockLayerFactory luckyBlockLayerFactory;

    private float levelTime = 0f;
    private String currentText = null;

    public static final float PIXELS_PER_TILE = 8.0f;
    public static final float UNIT_SCALE = 1 / PIXELS_PER_TILE;

    /**
     * Constructs and initialises the level from the given Tiled map.
     *
     * @param map the Tiled map describing the level layout and spawn points
     */
    public LevelModel(TiledMap map) {
        this.physics = new WorldPhysics(new Vector2(0, -15.0f));

        this.physics.registerPickupFactory("speed",
                spawn -> new SpeedBoostPickup(physics.getWorld(), spawn));
        this.physics.registerPickupFactory("heal",
                spawn -> new HealPickup(physics.getWorld(), spawn));

        new WorldCollisionLayerCreator(physics.getWorld(), map, "Collision", PIXELS_PER_TILE);
        this.luckyBlockLayerFactory = new LuckyBlockLayerFactory(
                physics.getWorld(), map, "Luckyblocks", PIXELS_PER_TILE);

        Spawner spawner = new Spawner(map, UNIT_SCALE);
        this.player = new Player(physics.getWorld(), spawner.getSpawnPoint("player_spawn"), 3);

        this.triggerZone = new TriggerZone(map, "Triggers", UNIT_SCALE);

        IAbstractEnemyFactory enemyFactory = new AbstractEnemyFactory(physics.getWorld());
        this.enemies = enemyFactory.createEnemiesFromLayer(
                map.getLayers().get("SpawnPoints"));
    }

    /**
     * Advances the level by one frame: steps physics, updates the player and
     * trigger zones, and removes dead enemies from the world.
     *
     * @param delta time in seconds since the last frame
     */
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

    /**
     * Returns all active entities (player + enemies) as a single list,
     * suitable for rendering.
     *
     * @return a new list of all active entities
     */
    public List<IEntity> getAllEntities() {
        List<IEntity> all = new ArrayList<>();
        if (player != null) all.add(player);
        all.addAll(enemies);
        return all;
    }

    public IPlayer getPlayer()                          { return player; }
    public List<IEnemy> getEnemies()                   { return enemies; }
    public WorldPhysics getPhysics()                   { return physics; }
    public String getCurrentText()                     { return currentText; }
    public float getLevelTime()                        { return levelTime; }
    public TriggerZone getTriggerZone()                { return triggerZone; }
    public LuckyBlockLayerFactory getLuckyBlockFactory() { return luckyBlockLayerFactory; }
}
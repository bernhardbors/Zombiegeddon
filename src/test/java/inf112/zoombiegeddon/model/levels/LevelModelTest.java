package inf112.zoombiegeddon.model.levels;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import inf112.zoombiegeddon.AbstractLibGDXTest;
import inf112.zoombiegeddon.app.GameConstants;
import inf112.zoombiegeddon.model.entity.IEntity;
import inf112.zoombiegeddon.model.entity.enemies.IEnemy;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LevelModelTest extends AbstractLibGDXTest {

    private TiledMap map;
    private LevelModel levelModel;

    @BeforeEach
    void beforeEach() {
        map = new TiledMap();
        MapLayer spawnLayer = new MapLayer();
        spawnLayer.setName("SpawnPoints");
        map.getLayers().add(spawnLayer);
        levelModel = new LevelModel(map);
    }

    @AfterEach
    void afterEach() {
        map.dispose();
    }

    // --- Constants ---

    @Test
    void testPixelsPerTile() {
        assertEquals(8.0f, GameConstants.PIXELS_PER_TILE);
    }

    @Test
    void testUnitScale() {
        assertEquals(GameConstants.UNIT_SCALE,1f / GameConstants.PIXELS_PER_TILE, 0.0001f);
    }

    // --- Initialization ---

    @Test
    void testPlayerNotNullAfterConstruction() {
        assertNotNull(levelModel.getPlayer());
    }

    @Test
    void testPhysicsNotNullAfterConstruction() {
        assertNotNull(levelModel.getPhysics());
    }

    @Test
    void testEnemiesNotNullAfterConstruction() {
        assertNotNull(levelModel.getEnemies());
    }

    @Test
    void testTriggerZoneNotNullAfterConstruction() {
        assertNotNull(levelModel.getTriggerZone());
    }

    @Test
    void testLuckyBlockFactoryNotNullAfterConstruction() {
        assertNotNull(levelModel.getLuckyBlockFactory());
    }

    @Test
    void testLevelTimeStartsAtZero() {
        assertEquals(0f, levelModel.getLevelTime());
    }

    @Test
    void testCurrentTextIsNullInitially() {
        assertNull(levelModel.getCurrentText());
    }

    @Test
    void testEnemiesEmptyWhenNoSpawnPoints() {
        assertTrue(levelModel.getEnemies().isEmpty());
    }

    // --- getAllEntities ---

    @Test
    void testGetAllEntitiesContainsPlayer() {
        assertTrue(levelModel.getAllEntities().contains(levelModel.getPlayer()));
    }

    @Test
    void testGetAllEntitiesSizeWithNoEnemies() {
        assertEquals(1, levelModel.getAllEntities().size());
    }

    @Test
    void testGetAllEntitiesSizeWithEnemies() {
        LevelModel model = levelModelWithEnemySpawn();
        assertEquals(2, model.getAllEntities().size());
    }

    @Test
    void testGetAllEntitiesContainsEnemies() {
        LevelModel model = levelModelWithEnemySpawn();
        List<IEntity> all = model.getAllEntities();
        assertTrue(model.getEnemies().stream().allMatch(all::contains));
    }

    // --- update ---

    @Test
    void testLevelTimeIncreasesOnUpdate() {
        levelModel.update(1.0f);
        assertEquals(1.0f, levelModel.getLevelTime(), 0.001f);
    }

    @Test
    void testLevelTimeAccumulatesAcrossUpdates() {
        levelModel.update(0.5f);
        levelModel.update(0.3f);
        assertEquals(0.8f, levelModel.getLevelTime(), 0.001f);
    }

    @Test
    void testDeadEnemiesRemovedDuringUpdate() {
        LevelModel model = levelModelWithEnemySpawn();
        assertEquals(1, model.getEnemies().size());

        IEnemy enemy = model.getEnemies().get(0);
        enemy.takeDamage(100);
        assertTrue(enemy.isDead());

        model.update(0.016f);

        assertTrue(model.getEnemies().isEmpty());
    }

    @Test
    void testAliveEnemiesNotRemovedDuringUpdate() {
        LevelModel model = levelModelWithEnemySpawn();
        assertEquals(1, model.getEnemies().size());

        model.update(0.016f);

        assertEquals(1, model.getEnemies().size());
    }

    // --- Helper ---

    private LevelModel levelModelWithEnemySpawn() {
        TiledMap mapWithEnemy = new TiledMap();
        MapLayer spawnLayer = new MapLayer();
        spawnLayer.setName("SpawnPoints");
        RectangleMapObject enemySpawn = new RectangleMapObject(10, 10, 1, 1);
        enemySpawn.setName("enemy_spawn");
        spawnLayer.getObjects().add(enemySpawn);
        mapWithEnemy.getLayers().add(spawnLayer);
        return new LevelModel(mapWithEnemy);
    }
}
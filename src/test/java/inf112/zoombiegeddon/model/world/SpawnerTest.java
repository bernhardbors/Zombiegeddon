package inf112.zoombiegeddon.model.world;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SpawnerTest {

    private TiledMap map;
    private MapLayer layer;
    private Spawner spawner;

    @BeforeEach
    void beforeEach() {
        map = new TiledMap();
        layer = new MapLayer();
        layer.setName("SpawnPoints");
        map.getLayers().add(layer);

        spawner = new Spawner(map, 1.0f);

    }

    @AfterEach
    void afterEach() {
        map.dispose();
    }

    @Test
    void testGetSpawnPoints() {
        RectangleMapObject spawnObject = new RectangleMapObject(10, 20, 1, 2);
        spawnObject.setName("playerSpawn");
        layer.getObjects().add(spawnObject);

        Vector2 vector = spawner.getSpawnPoint("playerSpawn");

        assertEquals(10, vector.x);
        assertEquals(20, vector.y);

    }

    @Test
    void testGetSpawnPointsThatDoesNotExist() {
        Vector2 vector = spawner.getSpawnPoint("dontExists");

        assertEquals(5, vector.x);
        assertEquals(5, vector.y);
    }

    @Test
    void testGetSpawnPointsWhereObjectTypeIsNotRectangleObject() {
        MapObject object = new MapObject();
        object.setName("spawn");
        layer.getObjects().add(object);

        Vector2 vector = spawner.getSpawnPoint("spawn");

        //Should return default
        assertEquals(5, vector.x);
        assertEquals(5, vector.y);
    }
}

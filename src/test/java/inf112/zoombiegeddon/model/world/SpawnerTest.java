package inf112.zoombiegeddon.model.world;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpawnerTest {

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
    void getSpawnPointsThatDoesNotExist() {
        Vector2 vector = spawner.getSpawnPoint("dontExists");

        assertEquals(5, vector.x);
        assertEquals(5, vector.y);
    }
}

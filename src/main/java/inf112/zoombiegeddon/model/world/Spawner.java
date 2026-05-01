package inf112.zoombiegeddon.model.world;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Retrieves spawn positions from a tiled map.
 * Looks up named spawn points in the {@code SpawnPoints} layer.
 */
public class Spawner implements ISpawner {
    private final TiledMap map;
    private final float unitScale;
    /**
     * Constructs a new spawner for the given tiled map.
     *
     * @param map The tiled map containing spawn point objects.
     * @param unitScale conversion factor from pixel coordinates to world units
     */
    public Spawner (TiledMap map, float unitScale) {
        this.map = map;
        this.unitScale = unitScale;
    }

    /**
     * Returns the spawn point with the given id from the map.
     * If no matching spawn point is found, a default fallback position is returned.
     *
     * @param spawnID The name of the spawn point to look up.
     * @return The spawn position in world units.
     */
    public Vector2 getSpawnPoint(String spawnID) {
        MapObjects objects = map.getLayers().get("SpawnPoints").getObjects();
        for (MapObject object : objects) {
            if (spawnID.equals(object.getName()) && object instanceof RectangleMapObject rectangleMapObject) {
                Rectangle rect = rectangleMapObject.getRectangle();

                return new Vector2(rect.x * unitScale,
                        rect.y * unitScale);
            }
        }
        return new Vector2(5, 5);
    }
}

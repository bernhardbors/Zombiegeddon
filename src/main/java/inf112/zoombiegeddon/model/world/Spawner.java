package inf112.zoombiegeddon.model.world;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import inf112.zoombiegeddon.view.levels.AbstractLevel;

public class Spawner {
    private TiledMap map;

    public Spawner (TiledMap map) {
        this.map = map;
    }

    public Vector2 getSpawnPoint(String SpawnID) {
        MapObjects objects = map.getLayers().get("SpawnPoints").getObjects();
        for (MapObject object : objects) {
            if (SpawnID.equals(object.getName()) && object instanceof RectangleMapObject) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();

                return new Vector2(rect.x * AbstractLevel.UNIT_SCALE,
                        rect.y * AbstractLevel.UNIT_SCALE);
            }
        }
        return new Vector2(5, 5); // Default backup
    }
}

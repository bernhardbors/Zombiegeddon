package inf112.zoombiegeddon.model.world;

import com.badlogic.gdx.math.Vector2;

/**
 * Retrieves spawn positions from a level map.
 */
public interface ISpawner {

    /**
     * Returns the world-unit position of the named spawn point.
     * Falls back to a default position if the spawn ID is not found.
     *
     * @param spawnID the name of the spawn point to look up
     * @return the spawn position in world units
     */
    Vector2 getSpawnPoint(String spawnID);
}

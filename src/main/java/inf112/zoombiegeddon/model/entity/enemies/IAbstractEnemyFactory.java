package inf112.zoombiegeddon.model.entity.enemies;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.math.Vector2;
import inf112.zoombiegeddon.model.animations.AnimationHandler;

import java.util.List;

/**
 * Defines a factory for creating enemies in the game world.
 */
public interface IAbstractEnemyFactory {

    /**
     * Creates a single enemy of the given type at the specified position.
     *
     * @param type The type of enemy to create.
     * @param position The spawn position of the enemy.
     * @param normalAnims The animation handler for regular enemy types.
     * @param chonkerAnims The animation handler for tank-type enemies.
     * @return A newly created enemy.
     */
    IEnemy createEnemy(EnemyType type, Vector2 position, AnimationHandler normalAnims, AnimationHandler chonkerAnims);

    /**
     * Creates enemies from spawn objects found in a map layer.
     *
     * @param layer The map layer containing enemy spawn objects.
     * @param normalAnims The animation handler for regular enemy types.
     * @param chonkerAnims The animation handler for tank-type enemies.
     * @return A list of enemies created from the layer.
     */
    List<IEnemy> createEnemiesFromLayer(MapLayer layer, AnimationHandler normalAnims, AnimationHandler chonkerAnims);
}

package inf112.zoombiegeddon.model.entity.enemies;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.math.Vector2;
import inf112.zoombiegeddon.model.animations.AnimationHandler;

import java.util.List;

public interface IEnemyFactory {
    /**
     * Creates an enemy based on type, position and animations
     */
    Enemy createEnemy(EnemyType type, Vector2 position, AnimationHandler normalAnims, AnimationHandler chonkerAnims);

    List<Enemy> createEnemiesFromLayer(MapLayer layer, AnimationHandler normalAnims, AnimationHandler chonkerAnims);
}

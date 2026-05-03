package inf112.zoombiegeddon.model.entity.enemies;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import inf112.zoombiegeddon.model.levels.LevelModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Factory for creating enemies in the game world.
 * Supports both direct enemy creation and creation from map layer spawn objects.
 */
public class AbstractEnemyFactory implements IAbstractEnemyFactory {
    private final World world;

    /**
     * Constructs a new enemy factory for the given physics world.
     *
     * @param world The Box2D world where enemies will be created.
     */
    public AbstractEnemyFactory(World world) {
        this.world = world;
    }

    /**
     * Creates a single enemy of the given type at the specified position.
     *
     * @param type The type of enemy to create.
     * @param position The spawn position of the enemy.
     * @return A newly created enemy.
     */
    @Override
    public IEnemy createEnemy(EnemyType type, Vector2 position) {
        return switch (type) {
            case TANK      -> new Enemy(world, position, 2, 1.0f);
            case FAST_ZOMBIE -> new Enemy(world, position, 1, 5.0f);
            default        -> new Enemy(world, position, 1, 3.0f);
        };
    }

    /**
     * Creates enemies from spawn objects found in a map layer.
     * Only objects named {@code enemy_spawn} are processed.
     *
     * @param layer The map layer containing enemy spawn objects.
     * @return A list of enemies created from the layer.
     */
    @Override
    public List<IEnemy> createEnemiesFromLayer(MapLayer layer) {
        List<IEnemy> enemies = new ArrayList<>();
        if (layer == null) return enemies;

        for (MapObject object : layer.getObjects()) {
            if (object instanceof RectangleMapObject rectObj
                    && "enemy_spawn".equals(object.getName())) {
                Rectangle rect = rectObj.getRectangle();
                Vector2 pos = new Vector2(
                        rect.x * LevelModel.UNIT_SCALE,
                        rect.y * LevelModel.UNIT_SCALE
                );
                String typeStr = object.getProperties().get("type", String.class);
                enemies.add(createEnemy(EnemyType.fromString(typeStr), pos));
            }
        }
        return enemies;
    }
}

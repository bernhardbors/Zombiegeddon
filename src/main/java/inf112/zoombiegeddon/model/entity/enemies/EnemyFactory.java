package inf112.zoombiegeddon.model.entity.enemies;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import inf112.zoombiegeddon.model.animations.AnimationHandler;
import inf112.zoombiegeddon.model.levels.LevelModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Factory for creating enemies in the game world.
 * Supports both direct enemy creation and creation from map layer spawn objects.
 */
public class EnemyFactory implements IEnemyFactory{
    private final World world;

    /**
     * Constructs a new enemy factory for the given physics world.
     *
     * @param world The Box2D world where enemies will be created.
     */
    public EnemyFactory(World world) {
        this.world = world;
    }

    /**
     * Creates a single enemy of the given type at the specified position.
     *
     * @param type The type of enemy to create.
     * @param position The spawn position of the enemy.
     * @param normalAnims The animation handler for regular enemy types.
     * @param chonkerAnims The animation handler for tank-type enemies.
     * @return A newly created enemy.
     */
    @Override
    public IEnemy createEnemy(EnemyType type, Vector2 position, AnimationHandler normalAnims, AnimationHandler chonkerAnims) {
        int hp;
        float speed;
        AnimationHandler animToUse;
        switch (type) {
            case TANK -> {
                hp = 3;
                speed = 1.0f;
                animToUse = chonkerAnims;
            }
            case FAST_ZOMBIE -> {
                hp = 1;
                speed = 5.0f;
                animToUse = normalAnims;
            }
            default -> {
                hp = 1;
                speed = 3.0f;
                animToUse = normalAnims;
            }
        }

        return new Enemy(world, position, animToUse, hp, speed);
    }


    /**
     * Creates enemies from spawn objects found in a map layer.
     * Only objects named {@code enemy_spawn} are used.
     *
     * @param layer The map layer containing enemy spawn objects.
     * @param normalAnims The animation handler for regular enemy types.
     * @param chonkerAnims The animation handler for tank-type enemies.
     * @return A list of enemies created from the layer.
     */
    @Override
    public List<IEnemy> createEnemiesFromLayer(MapLayer layer, AnimationHandler normalAnims, AnimationHandler chonkerAnims) {
        List<IEnemy> enemies = new ArrayList<>();
        if (layer == null) return enemies;

        for (MapObject object : layer.getObjects()) {
            if (object instanceof RectangleMapObject rectObj && "enemy_spawn".equals(object.getName())) {
                Rectangle rect = rectObj.getRectangle();
                Vector2 pos = new Vector2(
                        rect.x * LevelModel.UNIT_SCALE,
                        rect.y * LevelModel.UNIT_SCALE
                );

                String typeStr = object.getProperties().get("type", String.class);
                EnemyType type = EnemyType.fromString(typeStr);

                enemies.add(createEnemy(type, pos, normalAnims, chonkerAnims));
            }
        }
        return enemies;
    }
}

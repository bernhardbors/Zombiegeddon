package inf112.zoombiegeddon.model.entity.enemies;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import inf112.zoombiegeddon.model.animations.AnimationHandler;
import inf112.zoombiegeddon.view.levels.AbstractLevel;

import java.util.ArrayList;
import java.util.List;

public class EnemyFactory implements IEnemyFactory{
    private final World world;

    public EnemyFactory(World world) {
        this.world = world;
    }

    @Override
    public Enemy createEnemy(EnemyType type, Vector2 position, AnimationHandler normalAnims, AnimationHandler chonkerAnims) {
        int hp;
        float speed;
        AnimationHandler animToUse;
        switch (type) {
            case TANK -> {
                hp = 3;
                speed = 0.5f;
                animToUse = chonkerAnims;
            }
            case FAST_ZOMBIE -> {
                hp = 1;
                speed = 2.5f;
                animToUse = normalAnims;
            }
            default -> {
                hp = 1;              // 1 liv
                speed = 1.5f;        // Rask
                animToUse = normalAnims;
            } // Standard Zombie
        }

        return new Enemy(world, position, animToUse, hp, speed);
    }

    @Override
    public List<Enemy> createEnemiesFromLayer(MapLayer layer, AnimationHandler normalAnims, AnimationHandler chonkerAnims) {
        List<Enemy> enemies = new ArrayList<>();
        if (layer == null) return enemies;

        for (MapObject object : layer.getObjects()) {
            if (object instanceof RectangleMapObject rectObj && "enemy_spawn".equals(object.getName())) {
                Rectangle rect = rectObj.getRectangle();
                Vector2 pos = new Vector2(
                        rect.x * AbstractLevel.UNIT_SCALE,
                        rect.y * AbstractLevel.UNIT_SCALE
                );

                String typeStr = object.getProperties().get("type", String.class);
                EnemyType type = EnemyType.fromString(typeStr);

                enemies.add(createEnemy(type, pos, normalAnims, chonkerAnims));
            }
        }
        return enemies;
    }
}

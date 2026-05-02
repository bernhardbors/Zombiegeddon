package inf112.zoombiegeddon.model.entity.enemies;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import inf112.zoombiegeddon.view.animations.AnimationHandler;
import inf112.zoombiegeddon.model.entity.AbstractEntityTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EnemyFactoryTest extends AbstractEntityTest {

    private AbstractEnemyFactory factory;

    @BeforeEach
    void setUpFactory() {
        factory = new AbstractEnemyFactory(mockedWorld);
    }

    @Test
    void testCreateEnemiesFromLayer() {
        MapLayer layer = mock(MapLayer.class);
        RectangleMapObject spawnPoint = mock(RectangleMapObject.class);
        MapProperties props = new MapProperties();

        props.put("type", "TANK");
        when(spawnPoint.getName()).thenReturn("enemy_spawn");
        when(spawnPoint.getProperties()).thenReturn(props);
        when(spawnPoint.getRectangle()).thenReturn(new Rectangle(100, 100, 32, 32));

        MapObjects objects = new MapObjects();
        objects.add(spawnPoint);
        when(layer.getObjects()).thenReturn(objects);

        List<IEnemy> enemies = factory.createEnemiesFromLayer(layer, mockedAnims, mockedAnims);

        assertEquals(1, enemies.size(), "One enemy should be created.");
        assertEquals(3, enemies.getFirst().getMaxHealth(), "A TANK should have 3 HP.");
    }

    @Test
    void testEnemyFactoryCreatesCorrectTypes() {
        AnimationHandler tankAnims = mock(AnimationHandler.class);
        AnimationHandler normalAnims = mock(AnimationHandler.class);

        IEnemy tank = factory.createEnemy(EnemyType.TANK, spawnPos, normalAnims, tankAnims);
        assertEquals(3, tank.getMaxHealth());

        IEnemy fast = factory.createEnemy(EnemyType.FAST_ZOMBIE, spawnPos, normalAnims, tankAnims);
        assertEquals(1, fast.getMaxHealth());
    }

    @Test
    void testEnemyFactoryDefaultType() {
        AnimationHandler tankAnims = mock(AnimationHandler.class);
        AnimationHandler normalAnims = mock(AnimationHandler.class);

        IEnemy enemy = factory.createEnemy(EnemyType.ZOMBIE, spawnPos, normalAnims, tankAnims);

        assertEquals(1, enemy.getHealth());
        assertEquals(3.0f, enemy.getSpeed());
        assertEquals(normalAnims, enemy.getAnimationHandler());



    }
}
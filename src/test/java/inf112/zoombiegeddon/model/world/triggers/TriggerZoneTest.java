package inf112.zoombiegeddon.model.world.triggers;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import inf112.zoombiegeddon.model.entity.AbstractEntityTest;
import inf112.zoombiegeddon.model.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TriggerZoneTest extends AbstractEntityTest {
    private float scale = 1.0f;
    private Rectangle playerBounds;
    private TriggerZone triggerZone;
    private Player player;
    private Player mockedPlayer;
    private String testText = "Test123";


    @BeforeEach
    void setUp() {
        TiledMap map = mock(TiledMap.class);
        MapLayers layers = new MapLayers();

        MapLayer layer = new MapLayer();
        layer.setName("testLayer");

        //Making the signs with the infotext to test
        RectangleMapObject sign = new RectangleMapObject(0, 0, 10, 10);
        sign.getProperties().put("type", "sign");
        sign.getProperties().put("info", testText);

        // goalzone
        RectangleMapObject goal = new RectangleMapObject(20, 20, 10, 10);
        goal.getProperties().put("type", "goalzone");

        //killzone
        RectangleMapObject kill = new RectangleMapObject(40, 40, 10, 10);
        kill.getProperties().put("type", "killzone");

        // newSpawnZone
        RectangleMapObject newSpawn = new RectangleMapObject(60, 60, 10, 10);
        newSpawn.getProperties().put("type", "newzone");
        newSpawn.getProperties().put("spawn_x", 100f);
        newSpawn.getProperties().put("spawn_y", 100f);


        layer.getObjects().add(sign);
        layer.getObjects().add(goal);
        layer.getObjects().add(kill);
        layer.getObjects().add(newSpawn);
        layers.add(layer);

        when(map.getLayers()).thenReturn(layers);

        triggerZone = new TriggerZone(map, "testLayer", scale);
        player = new Player(mockedWorld, spawnPos, mockedAnims, 3);
        mockedPlayer = mock(Player.class);

    }
    @Test
    void testTriggerZoneActivatesSigns() {
        playerBounds = new Rectangle(0,0, 1 * scale, 1 * scale);
        String result = triggerZone.update(playerBounds, player);
        assertEquals(testText,result, "Signs should return text");
    }

    @Test
    void testTriggerZoneActivatesGoals() {
        playerBounds = new Rectangle(20 * scale, 20 * scale, 1 * scale, 1 * scale);
        triggerZone.update(playerBounds, player);
        assertTrue(player.inGoal(), "Player should have been put as inGoal = true");

    }
    @Test
    void testTriggerZoneActivatesKillZone() {
        playerBounds = new Rectangle(40 * scale, 40 * scale, 1 * scale, 1 * scale);
        triggerZone.update(playerBounds, player);
        assertTrue(player.isDead(), "Player should be put as isDead = true");

    }
    @Test
    void testTriggerZoneActivatesNewSpawn() {
        playerBounds = new Rectangle(60 * scale, 60 * scale, 1 * scale, 1 * scale);
        triggerZone.update(playerBounds, mockedPlayer);
        verify(mockedPlayer).setPosition(100f * scale, 100f * scale);

    }
    @Test
    void testTriggerZoneReturnsNullWhenNotActivated() {
        playerBounds = new Rectangle(100 * scale, 100 * scale, 1 * scale, 1 * scale);
        assertNull(triggerZone.update(playerBounds, player));
    }

    @Test
    void testBuildActionReturnsNullWhenNoActionMatches() {
        TiledMap map = mock(TiledMap.class);
        MapLayers layers = new MapLayers();
        MapLayer layer = new MapLayer();

        RectangleMapObject unknownType = new RectangleMapObject(0, 0, 10, 10);
        unknownType.getProperties().put("type", "unknown");

        layer.getObjects().add(unknownType);
        layers.add(layer);
        when(map.getLayers()).thenReturn(layers);

        TriggerZone zone = new TriggerZone(map, layer.getName(), scale);
        Rectangle bounds = new Rectangle(0, 0, 5, 5);

        assertNull(zone.update(bounds, player), "Should return null when no action matches");
    }
}


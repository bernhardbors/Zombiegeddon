package inf112.zoombiegeddon.model.world.triggers;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import inf112.zoombiegeddon.model.entity.AbstractEntityTest;
import inf112.zoombiegeddon.model.entity.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TriggerZoneTest extends AbstractEntityTest {

    @Test
    public void testTriggerZoneActivatesCorrectAction() {
        //Lager et mock-kart med en "goalzone"
        TiledMap map = mock(TiledMap.class);
        MapLayers layers = new MapLayers();
        MapLayer layer = new MapLayer();

        // Legger til en goalzone for å sjekke om triggerzone funker for dette tilfelle
        RectangleMapObject obj = new RectangleMapObject(0, 0, 10, 10);
        obj.getProperties().put("type", "goalzone");
        layer.getObjects().add(obj);
        layers.add(layer);
        when(map.getLayers()).thenReturn(layers);

        TriggerZone triggerZone = new TriggerZone(map, "testLayer");
        Player player = new Player(mockedWorld, spawnPos, mockedAnims, 3);

        triggerZone.update(new Rectangle(2, 2, 1, 1), player);

        assertTrue(player.inGoal(), "Spilleren skulle blitt satt til inGoal = true");
    }
    @Test
    public void testTriggerZoneNotActivatingWhenNotSupposedTO(){

        }

    }


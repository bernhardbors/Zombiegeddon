package inf112.zoombiegeddon.model.world.triggers;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import inf112.zoombiegeddon.model.entity.player.IPlayer;

import java.util.ArrayList;
import java.util.List;

public class TriggerZone {
    private record Zone(Rectangle bounds, ITriggerAction action) {}

    private final List<Zone> zones = new ArrayList<>();
    private Rectangle lastZone;

    public TriggerZone(TiledMap map, String layerName, float unitScale) {
        MapLayer layer = map.getLayers().get(layerName);
        if (layer == null) return;

        for (MapObject object : layer.getObjects()) {
            Zone zone = createZone(object, unitScale);
            if (zone != null) zones.add(zone);
        }
    }

    private Zone createZone(MapObject object, float unitScale) {
        if (!(object instanceof RectangleMapObject rectObj)) return null;

        Rectangle r = rectObj.getRectangle();
        Rectangle worldRect = new Rectangle(
                r.x * unitScale, r.y * unitScale,
                r.width * unitScale, r.height * unitScale
        );

        ITriggerAction action = buildAction(object, unitScale);
        if (action == null) return null;

        return new Zone(worldRect, action);
    }

    private ITriggerAction buildAction(MapObject object, float unitScale) {
        String type = object.getProperties().get("type", String.class);
        return switch (type == null ? "" : type) {
            case "killzone" -> new KillAction();
            case "sign" -> new SignAction(object.getProperties().get("info", String.class));
            case "goalzone" -> new GoalAction();
            case "newzone" -> {
                float spawnX = object.getProperties().get("spawn_x", Float.class) * unitScale;
                float spawnY = object.getProperties().get("spawn_y", Float.class) * unitScale;
                yield new SpawnAction(spawnX, spawnY);
            }
            default -> null;
        };
    }

    public String update(Rectangle playerBounds, IPlayer player) {
        for (Zone zone : zones) {
            if (zone.bounds().overlaps(playerBounds)) {
                lastZone = zone.bounds();
                return zone.action().onTrigger(player);
            }
        }
        lastZone = null;
        return null;
    }

    public Rectangle getZone() {
        return lastZone;
    }
}
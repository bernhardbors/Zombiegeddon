package inf112.zoombiegeddon.model.world.triggers;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import inf112.zoombiegeddon.model.entity.IPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles trigger zones defined in a TiledMap.
 * This class identifies specific map objects (like signs), converts their
 * coordinates to world units, and detects when a player is overlapping them.
 */
public class TriggerZone {
        private final List<Rectangle> zones = new ArrayList<>();
        private final List<ITriggerAction> actions = new ArrayList<>();
        private int lastIndex = -1;

        public TriggerZone(TiledMap map, String layerName, float unitScale) {
            MapLayer layer = map.getLayers().get(layerName);
            if (layer == null) return;

            for (MapObject object : layer.getObjects()) {
                if (!(object instanceof RectangleMapObject)) continue;

                Rectangle r = ((RectangleMapObject) object).getRectangle();
                Rectangle worldRect = new Rectangle(
                        r.x * unitScale, r.y * unitScale,
                        r.width * unitScale, r.height * unitScale
                );

                String type = object.getProperties().get("type", String.class);
                ITriggerAction action;

                if ("killzone".equals(type)) {
                    action = new KillAction();
                } else if ("sign".equals(type)) {
                    String text = object.getProperties().get("info", String.class);
                    action = new SignAction(text);
                } else if ("goalzone".equals(type)) {
                    action = new GoalAction();
                } else if ("newzone".equals(type)) {
                    float spawnX = object.getProperties().get("spawn_x", Float.class);
                    float spawnY = object.getProperties().get("spawn_y", Float.class);

                    spawnX *= unitScale;
                    spawnY *= unitScale;


                    action = new SpawnAction(spawnX, spawnY);
                } else {
                    continue;
                }

                zones.add(worldRect);
                actions.add(action);
            }
        }

        public String update(Rectangle playerBounds, IPlayer player) {
            for (int i = 0; i < zones.size(); i++) {
                if (zones.get(i).overlaps(playerBounds)) {
                    lastIndex = i;
                    return actions.get(i).onTrigger(player);
                }
            }
            lastIndex = -1;
            return null;
        }

        public Rectangle getZone() {
            return (lastIndex != -1) ? zones.get(lastIndex) : null;
        }
    }

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

    /**
     * Constructs a TriggerZone by reading map objects from the specified layer.
     * Each valid object is converted to world units and paired with an action
     * based on its "type" property.
     *
     * @param map       The TiledMap containing the trigger layer.
     * @param layerName The name of the map layer to read trigger objects from.
     * @param unitScale The scale factor to convert map coordinates to world units.
     */
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

            switch (type == null ? "" : type) {
                case "killzone":
                    action = new KillAction();
                    break;
                case "sign":
                    String text = object.getProperties().get("info", String.class);
                    action = new SignAction(text);
                    break;
                case "goalzone":
                    action = new GoalAction();
                    break;
                case "newzone":
                    float spawnX = object.getProperties().get("spawn_x", Float.class);
                    float spawnY = object.getProperties().get("spawn_y", Float.class);
                    spawnX *= unitScale;
                    spawnY *= unitScale;
                    action = new SpawnAction(spawnX, spawnY);
                    break;
                default:
                    continue;
            }

        zones.add(worldRect);
        actions.add(action);
        }
    }

    /**
     * Checks if the player's bounding rectangle overlaps any registered trigger zone.
     * If an overlap is found, the associated action is triggered and its result returned.
     *
     * @param playerBounds The player's current bounding rectangle in world units.
     * @param player       The player to pass to the triggered action.
     * @return The result of the triggered action, or null if no zone was overlapped.
     */
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

    /**
     * Returns the bounding rectangle of the last triggered zone.
     *
     * @return The last triggered zone's rectangle, or null if no zone is currently active.
     */
    public Rectangle getZone() {
        return (lastIndex != -1) ? zones.get(lastIndex) : null;
    }
}

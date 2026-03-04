package inf112.zoombiegeddon.model.world.triggers;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import inf112.zoombiegeddon.view.levels.AbstractLevel;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles trigger zones defined in a TiledMap.
 * This class identifies specific map objects (like signs), converts their
 * coordinates to world units, and detects when a player is overlapping them.
 */
public class TriggerZone {

    private final List<Rectangle> zones = new ArrayList<>();
    private final List<String> texts = new ArrayList<>();
    private final List<Boolean> isInside = new ArrayList<>();
    private int lastIndex = -1;

    /**
     * Constructs a TriggerZone by parsing a specific layer in the TiledMap.
     * Only objects with the property "type" set to the objectType will be loaded.
     *
     * @param map       The TiledMap containing the trigger objects.
     * @param layerName The name of the object layer to parse (e.g., "Signs").
     * @param objectType The name of the object to be loaded
     */
    public TriggerZone(TiledMap map, String layerName, String objectType) {

        MapLayer layer = map.getLayers().get(layerName);
        if (layer == null) {
            return;
        }

        for (MapObject object : layer.getObjects()) {
            String type = object.getProperties().get("type", String.class);
            if (!type.equals(objectType)) {
                continue;
            }
            Rectangle r = ((RectangleMapObject) object).getRectangle();
            Rectangle worldRect = new Rectangle(
                    r.x * AbstractLevel.UNIT_SCALE,
                    r.y * AbstractLevel.UNIT_SCALE,
                    r.width * AbstractLevel.UNIT_SCALE,
                    r.height * AbstractLevel.UNIT_SCALE
            );

            String text = object.getProperties().get("text", String.class);
            zones.add(worldRect);
            texts.add(text);
            isInside.add(false);
        }

    }

    /**
     * Checks if the player's bounds overlap with any defined trigger zones.
     * Updates the internal state to track which zone was last triggered.
     *
     * @param playerBounds The current bounding box of the player.
     * @return The message associated with the triggered zone, or null if no zone is triggered.
     */
    public String update(Rectangle playerBounds) {

        for (int i = 0; i < zones.size(); i++) {

            boolean inside = zones.get(i).overlaps(playerBounds);

            if (inside) {
                isInside.set(i, true);
                lastIndex = i;
                return texts.get(i);
            }

            else {
                isInside.set(i, false);
            }


        }
        lastIndex = -1;
        return null;
    }

    /**
     * Gets the rectangle area of the currently active trigger zone.
     *
     * @return The Rectangle of the last triggered zone,
     * or null if the player is not currently in any zone.
     */
    public Rectangle getZone() {
        if (lastIndex == -1) {
            return null;
        }
        return zones.get(lastIndex);
    }
}

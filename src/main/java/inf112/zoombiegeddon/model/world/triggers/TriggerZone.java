package inf112.zoombiegeddon.model.world.triggers;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import inf112.zoombiegeddon.view.levels.AbstractLevel;

import java.util.ArrayList;
import java.util.List;
public class TriggerZone {

    private final List<Rectangle> zones = new ArrayList<>();
    private final List<String> texts = new ArrayList<>();
    private final List<Boolean> isInside = new ArrayList<>();

    public TriggerZone(TiledMap map, String layerName) {

        MapLayer layer = map.getLayers().get(layerName);
        if (layer == null) {
            return;
        }

        for (MapObject object : layer.getObjects()) {
            String type = object.getProperties().get("type", String.class);
            if (!type.equals("sign")) {
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

    public String update(Rectangle playerBounds) {

        for (int i = 0; i < zones.size(); i++) {

            boolean inside = zones.get(i).overlaps(playerBounds);

            if (inside && !isInside.get(i)) {
                isInside.set(i, true);
                return texts.get(i);
            }

            if (!inside) {
                isInside.set(i, false);
            }


        }
        return null;
    }
}

package inf112.zoombiegeddon.model.world.luckyblock;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Parses a Tiled object layer and creates LuckyBlock instances in the physics world.
 */
public class LuckyBlockLayerFactory {

    private record TileRenderable(TiledMapTileMapObject tileObject, ILuckyBlock block) {
    }

    private final List<ILuckyBlock> blocks = new ArrayList<>();
    private final List<TileRenderable> tileRenderables = new ArrayList<>();

    public LuckyBlockLayerFactory(World world, TiledMap map, String layerName, float pixelsPerTile) {
        MapLayer layer = map.getLayers().get(layerName);
        if (layer == null) {
            return;
        }

        for (MapObject object : layer.getObjects()) {
            Rectangle rectangle = toWorldRectangle(object);
            if (rectangle == null) {
                continue;
            }

            float width = rectangle.width / pixelsPerTile;
            float height = rectangle.height / pixelsPerTile;
            float centerX = (rectangle.x + rectangle.width / 2f) / pixelsPerTile;
            float centerY = (rectangle.y + rectangle.height / 2f) / pixelsPerTile;

            String rewardType = object.getProperties().get("info", String.class);
            if (rewardType == null || rewardType.isEmpty()) {
                rewardType = "unknown";
            }

            ILuckyBlock block = new LuckyBlock(world, new Vector2(centerX, centerY), width, height, rewardType);
            blocks.add(block);

            if (object instanceof TiledMapTileMapObject tileObject) {
                tileRenderables.add(new TileRenderable(tileObject, block));
            }
        }
    }

    private Rectangle toWorldRectangle(MapObject object) {
        if (object instanceof RectangleMapObject rectangleObject) {
            return rectangleObject.getRectangle();
        }

        if (object instanceof TiledMapTileMapObject tileObject) {
            if (tileObject.getTile() == null || tileObject.getTile().getTextureRegion() == null) {
                return null;
            }

            TextureRegion region = tileObject.getTile().getTextureRegion();
            float width = region.getRegionWidth() * Math.abs(tileObject.getScaleX());
            float height = region.getRegionHeight() * Math.abs(tileObject.getScaleY());

            Number widthValue = tileObject.getProperties().get("width", Number.class);
            Number heightValue = tileObject.getProperties().get("height", Number.class);
            if (widthValue != null && widthValue.floatValue() > 0f) {
                width = widthValue.floatValue();
            }
            if (heightValue != null && heightValue.floatValue() > 0f) {
                height = heightValue.floatValue();
            }

            if (width <= 0 || height <= 0) {
                return null;
            }

            return new Rectangle(tileObject.getX(), tileObject.getY(), width, height);
        }

        return null;
    }

    public List<ILuckyBlock> getBlocks() {
        return Collections.unmodifiableList(blocks);
    }

    public void render(Batch batch, float unitScale) {
        for (TileRenderable renderable : tileRenderables) {
            if (renderable.block().isBroken()) {
                continue;
            }

            TiledMapTileMapObject tileObject = renderable.tileObject();
            if (tileObject.getTile() == null || tileObject.getTile().getTextureRegion() == null) {
                continue;
            }

            TextureRegion region = tileObject.getTile().getTextureRegion();
            float width = region.getRegionWidth() * Math.abs(tileObject.getScaleX()) * unitScale;
            float height = region.getRegionHeight() * Math.abs(tileObject.getScaleY()) * unitScale;

            batch.draw(region,
                    tileObject.getX() * unitScale,
                    tileObject.getY() * unitScale,
                    width,
                    height);
        }
    }
}
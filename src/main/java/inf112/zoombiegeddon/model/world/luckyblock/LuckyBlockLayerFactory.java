package inf112.zoombiegeddon.model.world.luckyblock;

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
 * Creates lucky blocks from a Tiled map layer and stores the corresponding render data.
 * Supports both rectangle objects and tile map objects.
 */
public class LuckyBlockLayerFactory {

    //Public so that the Renderer-class kan read what texture belongs to which block.
    public record TileRenderable(TiledMapTileMapObject tileObject, ILuckyBlock block) {
    }

    private final List<ILuckyBlock> blocks = new ArrayList<>();
    private final List<TileRenderable> tileRenderables = new ArrayList<>();

    /**
     * Constructs lucky blocks from the specified map layer.
     *
     * @param world The Box2D world where block bodies are created.
     * @param map The tiled map containing the lucky block layer.
     * @param layerName The name of the layer to parse.
     * @param pixelsPerTile The scale factor used to convert pixel coordinates to world units.
     */
    public LuckyBlockLayerFactory(World world, TiledMap map, String layerName, float pixelsPerTile) {
        MapLayer layer = map.getLayers().get(layerName);
        if (layer == null) {
            return;
        }

        for (MapObject object : layer.getObjects()) {
            Rectangle rectangle = getRectangle(object);
            if (rectangle == null) {
                continue;
            }

            Vector2 centerPos = new Vector2(
                    (rectangle.x + rectangle.width / 2f) / pixelsPerTile,
                    (rectangle.y + rectangle.height / 2f) / pixelsPerTile
            );

            float width = rectangle.width / pixelsPerTile;
            float height = rectangle.height / pixelsPerTile;

            String rewardType = object.getProperties().get("type", String.class);
            ILuckyBlock luckyBlock = new LuckyBlock(world, centerPos, width, height, rewardType);
            blocks.add(luckyBlock);

            if (object instanceof TiledMapTileMapObject tileObject) {
                tileRenderables.add(new TileRenderable(tileObject, luckyBlock));
            }
        }
    }


    private Rectangle getRectangle(MapObject object) {
        if (object instanceof RectangleMapObject rectObject) {
            return rectObject.getRectangle();
        }

        if (object instanceof TiledMapTileMapObject tileObject) {
            TextureRegion region = tileObject.getTile().getTextureRegion();
            if (region == null) {
                return null;
            }

            float width = region.getRegionWidth() * Math.abs(tileObject.getScaleX());
            float height = region.getRegionHeight() * Math.abs(tileObject.getScaleY());

            return new Rectangle(tileObject.getX(), tileObject.getY(), width, height);
        }

        return null;
    }

    /**
     * Returns the created lucky blocks as an unmodifiable list.
     *
     * @return The list of lucky blocks.
     */
    public List<ILuckyBlock> getBlocks() {
        return Collections.unmodifiableList(blocks);
    }

    /**
     * Method for letting view-classes reach blocks to be drawn
     * @return tiles to be drawn
     */
    public List<TileRenderable> getTileRenderables() {
        return Collections.unmodifiableList(tileRenderables);
    }
}
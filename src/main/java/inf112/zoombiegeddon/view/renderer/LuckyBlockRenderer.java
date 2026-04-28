package inf112.zoombiegeddon.view.renderer;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import inf112.zoombiegeddon.model.world.luckyblock.LuckyBlockLayerFactory;
/**
 * Class for handling rendering of Lucky Blocks.
 * This is a pure View-class collecting data from {@link LuckyBlockLayerFactory}.
 */
public class LuckyBlockRenderer {
    private final LuckyBlockLayerFactory factory;

    public LuckyBlockRenderer(LuckyBlockLayerFactory factory) {
        this.factory = factory;
    }

    /**
     * Renders all unbroken tile-based lucky blocks.
     *
     * @param batch The batch used for drawing.
     * @param unitScale The scale factor used to convert map coordinates to world coordinates.
     */
    public void render(Batch batch, float unitScale) {
        for (LuckyBlockLayerFactory.TileRenderable renderable : factory.getTileRenderables()) {
            drawRenderable(batch, renderable, unitScale);
        }
    }

    private void drawRenderable(Batch batch, LuckyBlockLayerFactory.TileRenderable renderable, float unitScale) {
        if (renderable.block().isBroken()) return;

        TiledMapTileMapObject tileObject = renderable.tileObject();
        if (tileObject.getTile() == null || tileObject.getTile().getTextureRegion() == null) return;

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

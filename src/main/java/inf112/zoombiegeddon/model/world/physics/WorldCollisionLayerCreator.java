package inf112.zoombiegeddon.model.world.physics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.physics.box2d.*;
import inf112.zoombiegeddon.app.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * Responsible for parsing a specific TiledMap layer and generating static Box2D collision bodies.
 * <p>
 * This class optimizes physics performance by grouping adjacent horizontal tiles into
 * single rectangular hitboxes rather than creating a separate body for every single tile.
 * </p>
 */
public class WorldCollisionLayerCreator {

    /**
     * Internal record to store grid coordinates of a tile cell.
     */
    private record CellCord(int x, int y) {
    }

    private static final Logger logger = LoggerFactory.getLogger(WorldCollisionLayerCreator.class);

    private final World world;
    private final BodyDef bodyDef;
    private final FixtureDef fixtureDef;
    private final PolygonShape tileShape;

    /**
     * Constructs a new creator and immediately triggers the hitbox generation for the specified layer.
     *
     * @param world         The Box2D world where the physics bodies will be created.
     * @param tilemap       The TiledMap containing the collision data.
     * @param layer         The name of the layer to be parsed for collisions.
     * @param pixelsPerTile The scaling factor (Pixels Per Meter) used to convert map coordinates to physics world units.
     */
    public WorldCollisionLayerCreator(World world, TiledMap tilemap, String layer, float pixelsPerTile) {

        this.world = world;

        this.bodyDef = new BodyDef();
        this.fixtureDef = new FixtureDef();
        this.tileShape = new PolygonShape();

        fixtureDef.density = 1;
        fixtureDef.friction = 1;

        TiledMapTileLayer tileMapLayer = (TiledMapTileLayer) tilemap.getLayers().get(layer);
        if (tileMapLayer == null) {
            logger.error("Unable to load tilemap layer for {}", layer);
            return;
        }
        createHitboxes(tileMapLayer, tileMapLayer.getTileWidth(), pixelsPerTile);

        dispose();
    }

    /**
     * Iterates through the tile layer grid and identifies contiguous horizontal segments of tiles.
     *
     * @param tileMapLayer  The specific layer being processed.
     * @param tileSize      The width/height of an individual tile in pixels.
     * @param pixelsPerTile The conversion ratio from pixels to meters.
     */
    private void createHitboxes(TiledMapTileLayer tileMapLayer, float tileSize, float pixelsPerTile) {

        ArrayList<CellCord> cellArray = new ArrayList<>();

        for (int y = 0; y < tileMapLayer.getHeight(); y++) {
            for (int x = 0; x < tileMapLayer.getWidth(); x++) {
                TiledMapTileLayer.Cell cell = tileMapLayer.getCell(x, y);

                if (cell != null && cell.getTile() != null) {
                    cellArray.add(new CellCord(x, y));
                } else if (!cellArray.isEmpty() || x == tileMapLayer.getWidth() - 1 && !cellArray.isEmpty()) {
                    createStaticBody(world, cellArray, tileSize, pixelsPerTile,
                            bodyDef, fixtureDef, tileShape);
                    cellArray.clear();
                }
            }
        }
    }

    /**
     * Calculates the dimensions and center point for a physics body and adds it to the world.
     *
     * @param world         The Box2D world.
     * @param tiles         The list of coordinates for the contiguous tiles.
     * @param tileSize      Tile size in pixels.
     * @param pixelPerTile Pixels per meter ratio.
     * @param bodyDef       Reusable body definition.
     * @param fixtureDef    Reusable fixture definition.
     * @param shape         Reusable polygon shape for the box.
     */
    private void createStaticBody(World world, ArrayList<CellCord> tiles, float tileSize, float pixelPerTile,
                                  BodyDef bodyDef, FixtureDef fixtureDef, PolygonShape shape) {

        int count = tiles.size();

        float widthMeters = (count * tileSize) / pixelPerTile;
        float heightMeters = tileSize / pixelPerTile;

        float startX = (tiles.getFirst().x() * tileSize) / pixelPerTile;
        float startY = (tiles.getFirst().y() * tileSize) / pixelPerTile;

        float centerX = startX + (widthMeters / 2f);
        float centerY = startY + (heightMeters / 2f);

        bodyDef.position.set(centerX, centerY);
        Body body = world.createBody(bodyDef);

        shape.setAsBox(widthMeters / 2f, heightMeters / 2f);

        fixtureDef.shape = shape;
        body.createFixture(fixtureDef);
    }

    public void dispose() {
        tileShape.dispose();
    }
}

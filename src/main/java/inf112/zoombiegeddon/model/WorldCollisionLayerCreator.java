package inf112.zoombiegeddon.model;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.physics.box2d.*;

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
     * @param PIXEL_PR_TILE The scaling factor (Pixels Per Meter) used to convert map coordinates to physics world units.
     */
    public WorldCollisionLayerCreator(World world, TiledMap tilemap, String layer, float PIXEL_PR_TILE) {

        this.world = world;

        this.bodyDef = new BodyDef();
        this.fixtureDef = new FixtureDef();
        this.tileShape = new PolygonShape();

        fixtureDef.density = 1;
        fixtureDef.friction = 1;

        TiledMapTileLayer tileMapLayer = (TiledMapTileLayer) tilemap.getLayers().get(layer);
        if (tileMapLayer == null) {
            System.err.println("Error: Layer '" + layer + "' not found in map.");
            return;
        }
        createHitboxes(tileMapLayer, tileMapLayer.getTileWidth(), PIXEL_PR_TILE);

        dispose();
    }

    /**
     * Iterates through the tile layer grid and identifies contiguous horizontal segments of tiles.
     *
     * @param tileMapLayer  The specific layer being processed.
     * @param tileSize      The width/height of an individual tile in pixels.
     * @param PIXEL_PR_TILE The conversion ratio from pixels to meters.
     */
    private void createHitboxes(TiledMapTileLayer tileMapLayer, float tileSize, float PIXEL_PR_TILE) {

        ArrayList<CellCord> cellArray = new ArrayList<>();

        for (int y = 0; y < tileMapLayer.getHeight(); y++) {
            for (int x = 0; x < tileMapLayer.getWidth(); x++) {
                TiledMapTileLayer.Cell cell = tileMapLayer.getCell(x, y);

                if (cell != null && cell.getTile() != null) {
                    cellArray.add(new CellCord(x, y));
                }

                if (cell == null || cell.getTile() == null || x == tileMapLayer.getWidth() - 1) {

                    if (!cellArray.isEmpty()) {

                        createStaticBody(world, cellArray, tileSize, PIXEL_PR_TILE
                                , bodyDef, fixtureDef, tileShape);
                        cellArray.clear();
                    }
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
     * @param PIXEL_PR_TILE Pixels per meter ratio.
     * @param bodyDef       Reusable body definition.
     * @param fixtureDef    Reusable fixture definition.
     * @param shape         Reusable polygon shape for the box.
     */
    private void createStaticBody(World world, ArrayList<CellCord> tiles, float tileSize, float PIXEL_PR_TILE,
                                  BodyDef bodyDef, FixtureDef fixtureDef, PolygonShape shape) {

        int count = tiles.size();

        float widthMeters = (count * tileSize) / PIXEL_PR_TILE;
        float heightMeters = tileSize / PIXEL_PR_TILE;

        float startX = (tiles.getFirst().x() * tileSize) / PIXEL_PR_TILE;
        float startY = (tiles.getFirst().y() * tileSize) / PIXEL_PR_TILE;

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

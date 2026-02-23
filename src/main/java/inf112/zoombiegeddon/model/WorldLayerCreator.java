package inf112.zoombiegeddon.model;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.physics.box2d.*;

import java.util.ArrayList;

public class WorldLayerCreator {

    private record CellCord(int x, int y) {
    }

    private final World world;
    private final BodyDef bodyDef;
    private final FixtureDef fixtureDef;
    private final PolygonShape tileShape;

    public WorldLayerCreator(World world, TiledMap tilemap, String layer, float PIXEL_PR_TILE) {

        this.world = world;

        this.bodyDef = new BodyDef();
        this.fixtureDef = new FixtureDef();
        this.tileShape = new PolygonShape();

        // tile config
        fixtureDef.density = 1;
        fixtureDef.friction = 1;

        // Get collision level in the tile map.
        TiledMapTileLayer tileMapLayer = (TiledMapTileLayer) tilemap.getLayers().get(layer);

        float tileSize = tileMapLayer.getTileWidth();

        createCollisionTilesFromLayer(tileMapLayer, tileSize, PIXEL_PR_TILE);

        dispose();
    }

    /*
     *   param @tileSize pixels pr tile
     *   param @tileMapLayer tile map layer from the map
     *
     */
    private void createCollisionTilesFromLayer(TiledMapTileLayer tileMapLayer, float tileSize, float PIXEL_PR_TILE) {

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

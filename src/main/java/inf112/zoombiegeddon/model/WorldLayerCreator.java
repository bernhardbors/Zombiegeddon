package inf112.zoombiegeddon.model;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.physics.box2d.*;

import java.util.ArrayList;

import static com.badlogic.gdx.scenes.scene2d.ui.Table.Debug.cell;

public class WorldCreator {

    private record CellCord(int x, int y) {}
    
    private final World world;
    private final BodyDef bodyDef; 
    private final FixtureDef fixtureDef;
    private final PolygonShape tileShape;

    WorldCreator(final World world, TiledMap tilemap) {
        
        this.world = world;

        this.bodyDef = new BodyDef();
        this.fixtureDef = new FixtureDef();
        this.tileShape = new PolygonShape();

        // tile config
        fixtureDef.density = 1;
        fixtureDef.friction = 1;
        fixtureDef.restitution = 1;

        // Get collision level in the tile map.
        TiledMapTileLayer tileMapLayer = (TiledMapTileLayer) tilemap.getLayers().get("collision");
        createCollisionTilesFromLayer(tileMapLayer);
    }

    private void createCollisionTilesFromLayer(TiledMapTileLayer tileMapLayer) {

        float tileSize = tileMapLayer.getTileWidth();
        
        for (int y = 0; y < tileMapLayer.getHeight(); y++) {

            // create a new array for every line of y_Axis cords
            ArrayList<CellCord> cellArray = new ArrayList<>();

            for (int x = 0; x < tileMapLayer.getWidth(); x++) {

                TiledMapTileLayer.Cell cell = tileMapLayer.getCell(x, y);

                if (cell != null && cell.getTile() != null) {
                    cellArray.add(new CellCord(x, y));
                }
                if (cell == null || cell.getTile() == null || x == tileMapLayer.getWidth() - 1) {
                    if (!cellArray.isEmpty()) {
                        createStaticBody(world, cellArray, tileSize, bodyDef, fixtureDef, tileShape);
                        cellArray.clear();
                    }

                }
            }
        }
    }

    private void createStaticBody(World world, ArrayList<CellCord> tiles, float tileSize,
                                  BodyDef bdef, FixtureDef fdef, PolygonShape shape) {
        
        float PIXEL_PR_TILE = 32;
        int count = tiles.size();
        float startX = tiles.get(0).x();
        float y = tiles.get(0).y();

        float totalWidth = count * tileSize;
        float centerX = (startX * tileSize + (totalWidth / 2f)) / PIXEL_PR_TILE;
        float centerY = (y * tileSize + (tileSize / 2f)) / PIXEL_PR_TILE;

        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set(centerX, centerY);

        Body body = world.createBody(bdef);

        float halfWidth = (totalWidth / 2f) / PIXEL_PR_TILE;
        float halfHeight = (tileSize / 2f) / PIXEL_PR_TILE;

        shape.setAsBox(halfWidth, halfHeight);
        fdef.shape = shape;
        body.createFixture(fdef);
    }
}

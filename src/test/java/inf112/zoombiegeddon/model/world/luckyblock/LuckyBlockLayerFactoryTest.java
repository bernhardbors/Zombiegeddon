package inf112.zoombiegeddon.model.world.luckyblock;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LuckyBlockLayerFactoryTest {

    private World mockedWorld;
    private Body mockedBody;
    private Fixture mockedFixture;

    @BeforeEach
    void setUp() {
        mockedWorld = mock(World.class);
        mockedBody = mock(Body.class);
        mockedFixture = mock(Fixture.class);

        when(mockedWorld.createBody(any(BodyDef.class))).thenReturn(mockedBody);
        when(mockedBody.createFixture(any(FixtureDef.class))).thenReturn(mockedFixture);
    }

    @Test
    void constructorReturnsEmptyListsWhenLayerDoesNotExist() {
        TiledMap map = new TiledMap();

        LuckyBlockLayerFactory factory =
                new LuckyBlockLayerFactory(mockedWorld, map, "luckyblocks", 32f);

        assertTrue(factory.getBlocks().isEmpty());
        assertTrue(factory.getTileRenderables().isEmpty());
    }

    @Test
    void constructorCreatesLuckyBlockFromRectangleObject() {
        TiledMap map = new TiledMap();
        MapLayer layer = new MapLayer();
        map.getLayers().add(layer);
        layer.setName("luckyblocks");

        RectangleMapObject rectangleObject = new RectangleMapObject(32f, 64f, 32f, 32f);
        rectangleObject.getProperties().put("type", "heal");
        layer.getObjects().add(rectangleObject);

        LuckyBlockLayerFactory factory =
                new LuckyBlockLayerFactory(mockedWorld, map, "luckyblocks", 32f);

        assertEquals(1, factory.getBlocks().size());
        assertTrue(factory.getTileRenderables().isEmpty());

        ILuckyBlock block = factory.getBlocks().get(0);
        assertNotNull(block);
        assertInstanceOf(LuckyBlock.class, block);
        assertEquals("heal", ((LuckyBlock) block).getRewardType());
    }

    @Test
    void constructorCreatesLuckyBlockAndRenderableFromTileObject() {
        TiledMap map = new TiledMap();
        MapLayer layer = new MapLayer();
        map.getLayers().add(layer);
        layer.setName("luckyblocks");

        TiledMapTile mockedTile = mock(TiledMapTile.class);
        TextureRegion mockedRegion = mock(TextureRegion.class);

        when(mockedTile.getTextureRegion()).thenReturn(mockedRegion);
        when(mockedRegion.getRegionWidth()).thenReturn(16);
        when(mockedRegion.getRegionHeight()).thenReturn(32);

        TiledMapTileMapObject tileObject = mock(TiledMapTileMapObject.class);
        MapProperties properties = new MapProperties();
        properties.put("type", "ammo");

        when(tileObject.getProperties()).thenReturn(properties);
        when(tileObject.getTile()).thenReturn(mockedTile);
        when(tileObject.getX()).thenReturn(100f);
        when(tileObject.getY()).thenReturn(200f);
        when(tileObject.getScaleX()).thenReturn(-2f);
        when(tileObject.getScaleY()).thenReturn(3f);

        layer.getObjects().add(tileObject);

        LuckyBlockLayerFactory factory =
                new LuckyBlockLayerFactory(mockedWorld, map, "luckyblocks", 32f);

        assertEquals(1, factory.getBlocks().size());
        assertEquals(1, factory.getTileRenderables().size());

        ILuckyBlock block = factory.getBlocks().get(0);
        LuckyBlockLayerFactory.TileRenderable renderable = factory.getTileRenderables().get(0);

        assertInstanceOf(LuckyBlock.class, block);
        assertEquals("ammo", ((LuckyBlock) block).getRewardType());
        assertSame(tileObject, renderable.tileObject());
        assertSame(block, renderable.block());
    }

    @Test
    void constructorSkipsTileObjectWhenTextureRegionIsNull() {
        TiledMap map = new TiledMap();
        MapLayer layer = new MapLayer();
        map.getLayers().add(layer);
        layer.setName("luckyblocks");

        TiledMapTile mockedTile = mock(TiledMapTile.class);
        when(mockedTile.getTextureRegion()).thenReturn(null);

        TiledMapTileMapObject tileObject = mock(TiledMapTileMapObject.class);
        MapProperties properties = new MapProperties();
        properties.put("type", "heal");

        when(tileObject.getProperties()).thenReturn(properties);
        when(tileObject.getTile()).thenReturn(mockedTile);

        layer.getObjects().add(tileObject);

        LuckyBlockLayerFactory factory =
                new LuckyBlockLayerFactory(mockedWorld, map, "luckyblocks", 32f);

        assertTrue(factory.getBlocks().isEmpty());
        assertTrue(factory.getTileRenderables().isEmpty());
    }

    @Test
    void constructorSkipsUnsupportedMapObject() {
        TiledMap map = new TiledMap();
        MapLayer layer = new MapLayer();
        map.getLayers().add(layer);
        layer.setName("luckyblocks");

        MapObject unsupportedObject = new MapObject();
        unsupportedObject.getProperties().put("type", "heal");
        layer.getObjects().add(unsupportedObject);

        LuckyBlockLayerFactory factory =
                new LuckyBlockLayerFactory(mockedWorld, map, "luckyblocks", 32f);

        assertTrue(factory.getBlocks().isEmpty());
        assertTrue(factory.getTileRenderables().isEmpty());
    }

    @Test
    void getBlocksReturnsUnmodifiableList() {
        TiledMap map = new TiledMap();
        MapLayer layer = new MapLayer();
        map.getLayers().add(layer);
        layer.setName("luckyblocks");

        RectangleMapObject rectangleObject = new RectangleMapObject(0f, 0f, 32f, 32f);
        rectangleObject.getProperties().put("type", "heal");
        layer.getObjects().add(rectangleObject);

        LuckyBlockLayerFactory factory =
                new LuckyBlockLayerFactory(mockedWorld, map, "luckyblocks", 32f);

        List<ILuckyBlock> blocks = factory.getBlocks();

        assertThrows(UnsupportedOperationException.class,
                () -> blocks.add(mock(ILuckyBlock.class)));
    }

    @Test
    void getTileRenderablesReturnsUnmodifiableList() {
        TiledMap map = new TiledMap();
        MapLayer layer = new MapLayer();
        map.getLayers().add(layer);
        layer.setName("luckyblocks");

        TiledMapTile mockedTile = mock(TiledMapTile.class);
        TextureRegion mockedRegion = mock(TextureRegion.class);

        when(mockedTile.getTextureRegion()).thenReturn(mockedRegion);
        when(mockedRegion.getRegionWidth()).thenReturn(16);
        when(mockedRegion.getRegionHeight()).thenReturn(16);

        TiledMapTileMapObject tileObject = mock(TiledMapTileMapObject.class);
        MapProperties properties = new MapProperties();
        properties.put("type", "heal");

        when(tileObject.getProperties()).thenReturn(properties);
        when(tileObject.getTile()).thenReturn(mockedTile);
        when(tileObject.getX()).thenReturn(0f);
        when(tileObject.getY()).thenReturn(0f);
        when(tileObject.getScaleX()).thenReturn(1f);
        when(tileObject.getScaleY()).thenReturn(1f);

        layer.getObjects().add(tileObject);

        LuckyBlockLayerFactory factory =
                new LuckyBlockLayerFactory(mockedWorld, map, "luckyblocks", 32f);

        List<LuckyBlockLayerFactory.TileRenderable> renderables = factory.getTileRenderables();

        assertThrows(UnsupportedOperationException.class,
                () -> renderables.add(null));
    }
}
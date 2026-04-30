package inf112.zoombiegeddon.model.world.luckyblock;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import inf112.zoombiegeddon.AbstractLibGDXTest;
import inf112.zoombiegeddon.model.entity.pickups.HealPickup;
import inf112.zoombiegeddon.model.entity.pickups.IPickup;
import inf112.zoombiegeddon.model.world.physics.WorldPhysics;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LuckyBlockPickupTest extends AbstractLibGDXTest {

    @Test
    void spawnPickupCreatesHealPickup() {
        WorldPhysics worldPhysics = new WorldPhysics(new Vector2(0f, -9.81f));

        worldPhysics.registerPickupFactory("heal", spawn -> new HealPickup(worldPhysics.getWorld(), spawn));
        try {
            boolean spawned = worldPhysics.spawnPickup("heal", new Vector2(4f, 2f));

            worldPhysics.update(1/60f);
            assertTrue(spawned, "Heal pickup should spawn successfully.");
            assertEquals(1, worldPhysics.getPickups().size(), "One pickup should be registered.");
            IPickup pickup = worldPhysics.getPickups().get(0);
            assertEquals("heal", pickup.getPickupType(), "Pickup type should stay aligned with the heal key.");
        } finally {
            worldPhysics.dispose();
        }
    }

    @Test
    void luckyBlockBreaksIntoHealPickupSpawn() {
        World world = mock(World.class);
        Body body = mock(Body.class);
        Fixture fixture = mock(Fixture.class);

        when(world.createBody(any(BodyDef.class))).thenReturn(body);
        when(body.createFixture(any(FixtureDef.class))).thenReturn(fixture);
        when(body.getPosition()).thenReturn(new Vector2(12f, 7f));

        LuckyBlock luckyBlock = new LuckyBlock(world, new Vector2(12f, 7f), 1f, 1f, "heal");
        WorldPhysics worldPhysics = mock(WorldPhysics.class);

        luckyBlock.breakBlock(worldPhysics);

        verify(worldPhysics).spawnPickup(
                org.mockito.ArgumentMatchers.eq("heal"),
                org.mockito.ArgumentMatchers.argThat(position -> position.epsilonEquals(new Vector2(12f, 7.9f), 0.0001f))
        );
    }

    @Test
    void isBrokenReturnsFalseBeforeBreaking() {
        World world = mock(World.class);
        Body body = mock(Body.class);
        Fixture fixture = mock(Fixture.class);
        when(world.createBody(any(BodyDef.class))).thenReturn(body);
        when(body.createFixture(any(FixtureDef.class))).thenReturn(fixture);
        when(body.getPosition()).thenReturn(new Vector2(1f, 1f));

        LuckyBlock block = new LuckyBlock(world, new Vector2(1f, 1f), 1f, 1f, "heal");

        assertFalse(block.isBroken());
    }

    @Test
    void breakBlockIsIdempotentAndSpawnsRewardOnlyOnce() {
        World world = mock(World.class);
        Body body = mock(Body.class);
        Fixture fixture = mock(Fixture.class);
        when(world.createBody(any(BodyDef.class))).thenReturn(body);
        when(body.createFixture(any(FixtureDef.class))).thenReturn(fixture);
        when(body.getPosition()).thenReturn(new Vector2(1f, 1f));

        LuckyBlock block = new LuckyBlock(world, new Vector2(1f, 1f), 1f, 1f, "heal");
        WorldPhysics worldPhysics = mock(WorldPhysics.class);

        block.breakBlock(worldPhysics);
        assertTrue(block.isBroken());

        block.breakBlock(worldPhysics); // second call: no-op due to broken guard

        verify(worldPhysics, times(1)).spawnPickup(any(), any());
    }

    @Test
    void getRewardTypeReturnsExpectedType() {
        World world = mock(World.class);
        Body body = mock(Body.class);
        Fixture fixture = mock(Fixture.class);
        when(world.createBody(any(BodyDef.class))).thenReturn(body);
        when(body.createFixture(any(FixtureDef.class))).thenReturn(fixture);
        when(body.getPosition()).thenReturn(new Vector2(1f, 1f));

        LuckyBlock block = new LuckyBlock(world, new Vector2(1f, 1f), 1f, 1f, "speed");

        assertEquals("speed", block.getRewardType());
    }

    @Test
    void testSpawnRewardWhenNullOrEmpty() {
        World world = mock(World.class);
        Body body = mock(Body.class);
        Fixture fixture = mock(Fixture.class);

        when(world.createBody(any(BodyDef.class))).thenReturn(body);
        when(body.createFixture(any(FixtureDef.class))).thenReturn(fixture);
        when(body.getPosition()).thenReturn(new Vector2(1f, 1f));



        LuckyBlock blockNull = new LuckyBlock(world, new Vector2(1f, 1f), 1f, 1f, null);
        blockNull.spawnReward(null); //Should not crash

        //When rewardType is null, spawnReward should return
        WorldPhysics worldPhysicsNull = mock(WorldPhysics.class);
        blockNull.spawnReward(worldPhysicsNull);
        verify(worldPhysicsNull, never()).spawnPickup(any(), any());

        //When rewardType is empty, spawnReward should return
        LuckyBlock blockEmpty = new LuckyBlock(world, new Vector2(1f, 1f), 1f, 1f, "");
        WorldPhysics worldPhysicsEmpty = mock(WorldPhysics.class);
        blockEmpty.spawnReward(worldPhysicsEmpty);

        verify(worldPhysicsEmpty, never()).spawnPickup(any(), any());
    }
}

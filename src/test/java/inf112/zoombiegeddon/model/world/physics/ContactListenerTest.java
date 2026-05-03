package inf112.zoombiegeddon.model.world.physics;

import com.badlogic.gdx.physics.box2d.*;
import inf112.zoombiegeddon.model.entity.pickups.Pickup;
import inf112.zoombiegeddon.model.entity.player.Player;
import inf112.zoombiegeddon.model.entity.enemies.Enemy;
import inf112.zoombiegeddon.model.world.luckyblock.ILuckyBlock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class ContactListenerTest {

    private WorldContactListener listener;
    private WorldPhysics mockWorldPhysics;
    private World mockWorld;

    private Contact contact;
    private Fixture fixtureA;
    private Fixture fixtureB;
    private Body bodyA;
    private Body bodyB;

    @BeforeEach
    void setUp() {
        mockWorldPhysics = mock(WorldPhysics.class);
        mockWorld = mock(World.class);
        listener = new WorldContactListener(mockWorldPhysics);

        contact = mock(Contact.class);
        fixtureA = mock(Fixture.class);
        fixtureB = mock(Fixture.class);
        bodyA = mock(Body.class);
        bodyB = mock(Body.class);

        when(contact.getFixtureA()).thenReturn(fixtureA);
        when(contact.getFixtureB()).thenReturn(fixtureB);
        when(fixtureA.getBody()).thenReturn(bodyA);
        when(fixtureB.getBody()).thenReturn(bodyB);
    }

    @Test
    void testBeginContact_SetsGroundedTrue() {
        Player mockPlayer = mock(Player.class);
        when(fixtureA.getUserData()).thenReturn("feet");
        when(bodyA.getUserData()).thenReturn(mockPlayer);

        listener.beginContact(contact);

        verify(mockPlayer).setGrounded(true);
    }

    @Test
    void testEndContact_SetsGroundedFalse() {
        Player mockPlayer = mock(Player.class);
        when(fixtureB.getUserData()).thenReturn("feet");
        when(bodyB.getUserData()).thenReturn(mockPlayer);

        listener.endContact(contact);

        verify(mockPlayer).setGrounded(false);
    }

    @Test
    void testPlayerEnemyCollision_HitWithFeet() {
        Player mockPlayer = mock(Player.class);
        Enemy mockEnemy = mock(Enemy.class);

        // A is player feet, B is enemy
        when(fixtureA.getUserData()).thenReturn("feet");
        when(bodyA.getUserData()).thenReturn(mockPlayer);
        when(bodyB.getUserData()).thenReturn(mockEnemy);

        listener.beginContact(contact);

        // Verify player.hitEnemy(enemy, true) was called
        verify(mockPlayer).hitEnemy(mockEnemy, true);
    }

    @Test
    void testLuckyBlockCollision_QueuesForRemoval() {
        Player mockPlayer = mock(Player.class);
        ILuckyBlock mockBlock = mock(ILuckyBlock.class);

        when(bodyA.getUserData()).thenReturn(mockPlayer);
        when(bodyB.getUserData()).thenReturn(mockBlock);
        when(mockBlock.isBroken()).thenReturn(false);
        when(mockBlock.getBody()).thenReturn(bodyB);

        listener.beginContact(contact);

        verify(mockBlock).breakBlock(mockWorldPhysics);

        // Process removals to ensure the body was actually queued
        when(mockWorld.isLocked()).thenReturn(false);
        listener.processQueuedRemovals(mockWorld);
        verify(mockWorld).destroyBody(bodyB);
    }

    @Test
    void testPickupCollision_CallsCollect() {
        Player mockPlayer = mock(Player.class);
        Pickup mockPickup = mock(Pickup.class);

        when(bodyA.getUserData()).thenReturn(mockPlayer);
        when(bodyB.getUserData()).thenReturn(mockPickup);
        when(mockPickup.isCollected()).thenReturn(false);
        when(mockPickup.getBody()).thenReturn(bodyB);

        listener.beginContact(contact);

        verify(mockPickup).collect(mockPlayer);

        when(mockWorld.isLocked()).thenReturn(false);
        listener.processQueuedRemovals(mockWorld);
        verify(mockWorld).destroyBody(bodyB);
    }

    @Test
    void testProcessQueuedRemovals_DoesNothingWhenWorldLocked() {
        ILuckyBlock mockBlock = mock(ILuckyBlock.class);
        when(bodyA.getUserData()).thenReturn(mock(Player.class));
        when(bodyB.getUserData()).thenReturn(mockBlock);
        when(mockBlock.getBody()).thenReturn(bodyB);

        listener.beginContact(contact); // Queues bodyB

        // World is locked (currently simulating a physics step)
        when(mockWorld.isLocked()).thenReturn(true);
        listener.processQueuedRemovals(mockWorld);

        verify(mockWorld, never()).destroyBody(any());
    }
}
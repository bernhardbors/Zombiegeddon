package inf112.zoombiegeddon.model.world.physics;

import com.badlogic.gdx.physics.box2d.*;
import inf112.zoombiegeddon.model.entity.IEntity;
import inf112.zoombiegeddon.model.entity.player.IPlayer;
import inf112.zoombiegeddon.model.entity.enemies.IEnemy;
import inf112.zoombiegeddon.model.entity.pickups.IPickup;
import inf112.zoombiegeddon.model.world.luckyblock.ILuckyBlock;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles collision events within the Box2D physics world.
 * Manages grounded state, player-enemy interactions, lucky block breaks,
 * and pickup collection.
 */
public class WorldContactListener implements ContactListener {

    private final List<Body> bodiesQueuedForRemoval = new ArrayList<>();
    private final IWorldPhysics worldPhysics;

    /**
     * Creates a contact listener connected to the given physics system.
     *
     * @param worldPhysics the physics system used for world interactions
     */
    public WorldContactListener(IWorldPhysics worldPhysics) {
        this.worldPhysics = worldPhysics;
    }

    /**
     * Called when two fixtures begin to touch.
     */
    @Override
    public void beginContact(Contact contact) {
        handleFeetContact(contact, true);
        checkPlayerEnemyCollision(contact);
        checkLuckyBlockCollision(contact);
        checkPickupCollision(contact);
    }

    /**
     * Called when two fixtures cease to touch.
     */
    @Override
    public void endContact(Contact contact) {
        handleFeetContact(contact, false);
    }

    /**
     * Identifies "feet" sensor contacts and updates the grounded state
     * of the associated entity.
     *
     * @param contact  The contact between two fixtures.
     * @param grounded True on begin contact, false on end contact.
     */
    private void handleFeetContact(Contact contact, boolean grounded) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if ("feet".equals(fa.getUserData())
                && fa.getBody().getUserData() instanceof IEntity entity) {
            entity.setGrounded(grounded);
        } else if ("feet".equals(fb.getUserData())
                && fb.getBody().getUserData() instanceof IEntity entity) {
            entity.setGrounded(grounded);
        }
    }

    private void checkPlayerEnemyCollision(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        Object bodyA = fa.getBody().getUserData();
        Object bodyB = fb.getBody().getUserData();

        if (bodyA instanceof IPlayer player && bodyB instanceof IEnemy enemy) {
            player.hitEnemy(enemy, "feet".equals(fa.getUserData()));
        } else if (bodyB instanceof IPlayer player && bodyA instanceof IEnemy enemy) {
            player.hitEnemy(enemy, "feet".equals(fb.getUserData()));
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {}

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {}

    /**
     * Destroys bodies that were queued for removal during collision handling.
     *
     * @param world the Box2D world containing the queued bodies
     */
    public void processQueuedRemovals(World world) {
        if (bodiesQueuedForRemoval.isEmpty()) return;

        for (Body body : bodiesQueuedForRemoval) {
            if (body != null && !world.isLocked()) {
                world.destroyBody(body);
            }
        }
        bodiesQueuedForRemoval.clear();
    }

    private void checkLuckyBlockCollision(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        Object bodyA = fa.getBody().getUserData();
        Object bodyB = fb.getBody().getUserData();

        if (bodyA instanceof IPlayer && bodyB instanceof ILuckyBlock luckyBlock) {
            breakLuckyBlock(luckyBlock);
        } else if (bodyB instanceof IPlayer && bodyA instanceof ILuckyBlock luckyBlock) {
            breakLuckyBlock(luckyBlock);
        }
    }

    private void breakLuckyBlock(ILuckyBlock luckyBlock) {
        if (luckyBlock.isBroken()) return;
        luckyBlock.breakBlock(worldPhysics);
        Body body = luckyBlock.getBody();
        if (!bodiesQueuedForRemoval.contains(body)) {
            bodiesQueuedForRemoval.add(body);
        }
    }

    private void checkPickupCollision(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        Object bodyA = fa.getBody().getUserData();
        Object bodyB = fb.getBody().getUserData();

        if (bodyA instanceof IPlayer player && bodyB instanceof IPickup pickup) {
            collectPickup(player, pickup);
        } else if (bodyB instanceof IPlayer player && bodyA instanceof IPickup pickup) {
            collectPickup(player, pickup);
        }
    }

    private void collectPickup(IPlayer player, IPickup pickup) {
        if (pickup.isCollected()) return;
        pickup.collect(player);
        Body body = pickup.getBody();
        if (!bodiesQueuedForRemoval.contains(body)) {
            bodiesQueuedForRemoval.add(body);
        }
    }
}
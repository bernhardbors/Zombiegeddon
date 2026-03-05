package inf112.zoombiegeddon.model.world.physics;

import com.badlogic.gdx.physics.box2d.*;
import inf112.zoombiegeddon.model.entity.Enemy;
import inf112.zoombiegeddon.model.entity.Entity;
import inf112.zoombiegeddon.model.entity.IProjectile;
import inf112.zoombiegeddon.model.entity.Player;


/**
 * Handles collision inf112.zoombiegeddon.events within the Box2D physics world.
 * Specifically manages player state transitions, such as detecting
 * if the player is grounded by checking contact with sensors.
 */
public class WorldContactListener implements ContactListener {

    /**
     * Called when two fixtures begin to touch.
     * Checks if the "feet" sensor of an Entity is involved in the collision
     * to set the Entity's grounded state to true.
     *
     * @param contact The contact object containing information about the collision.
     */
    @Override
    public void beginContact(Contact contact) {
        handleFeetContact(contact, true);
        checkPlayerEntityCollision(contact);
        checkProjectileCollision(contact);
    }

    /**
     * Called when two fixtures cease to touch.
     * Checks if the "feet" sensor of an Entity has left a surface
     * to set the Entity's grounded state to false.
     *
     * @param contact The contact object containing information about the collision.
     */
    @Override
    public void endContact(Contact contact) {
        handleFeetContact(contact, false);
        checkPlayerEntityCollision(contact);
    }

    /**
     * Handles contact inf112.zoombiegeddon.events between fixtures to determine if an entity is grounded.
     * This method identifies if one of the colliding fixtures is a "feet" sensor and
     * updates the grounded state of the associated {@link Entity} (e.g., Player or Enemy).
     * * <p>The method relies on "feet" being set as the UserData on the sensor fixture,
     * and the Entity object being set as the UserData on the physics Body.</p>
     *
     * @param contact  The Box2D {@link Contact} object containing the two colliding fixtures.
     * @param grounded The state to be applied to the entity (true for beginContact, false for endContact).
     */
    private void handleFeetContact(Contact contact, boolean grounded) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        // PRINT FOR DEBUGGING
        if ("feet".equals(fa.getUserData()) || "feet".equals(fb.getUserData())) {
            System.out.println("KONTAKT: Fotsensor traff noe! Grounded: " + grounded);
            System.out.println("A: " + fa.getUserData() + " | B: " + fb.getUserData());
        }

        if ("feet".equals(fa.getUserData())) {
            Object data = fa.getBody().getUserData();
            if (data instanceof Entity) ((Entity) data).setGrounded(grounded);
        } else if ("feet".equals(fb.getUserData())) {
            Object data = fb.getBody().getUserData();
            if (data instanceof Entity) ((Entity) data).setGrounded(grounded);
        }
    }

    private void checkPlayerEntityCollision(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        Object bodyAData = fa.getBody().getUserData();
        Object bodyBData = fb.getBody().getUserData();

        if (bodyAData instanceof Player && bodyBData instanceof Enemy) {
            ((Player) bodyAData).takeDamage(1);
            System.out.println("Player hit! Health: " + ((Player) bodyAData).getHealth());
        } else if (bodyAData instanceof Enemy && bodyBData instanceof Player) {
            ((Player) bodyBData).takeDamage(1);
            System.out.println("Player hit! Health: " + ((Player) bodyBData).getHealth());
        }
    }

    private void checkProjectileCollision(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        // Get the UserData from the BODIES, not the fixtures
        Object dataA = fa.getBody().getUserData();
        Object dataB = fb.getBody().getUserData();

        // --- Case 1: Bullet hits an Enemy ---
        if (dataA instanceof IProjectile && dataB instanceof Enemy) {
            ((Enemy) dataB).takeDamage(2);
            ((IProjectile) dataA).markForRemoval();
        } else if (dataB instanceof IProjectile && dataA instanceof Enemy) {
            ((Enemy) dataA).takeDamage(2);
            ((IProjectile) dataB).markForRemoval();
        }

        // --- Case 2: Bullet hits a Wall (StaticBody) ---
        // Check if one is a projectile and the OTHER body is static
        if (dataA instanceof IProjectile && fb.getBody().getType() == BodyDef.BodyType.StaticBody) {
            ((IProjectile) dataA).markForRemoval();
        } else if (dataB instanceof IProjectile && fa.getBody().getType() == BodyDef.BodyType.StaticBody) {
            ((IProjectile) dataB).markForRemoval();
        }
    }



    /**
     * Called after collision detection but before collision resolution.
     * This can be used to disable specific contacts based on logic.
     *
     * @param contact     The contact object.
     * @param oldManifold The world manifold before the update.
     */
    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        checkPlayerEntityCollision(contact);
    }

    /**
     * Called after the collision has been resolved.
     * Provides the physics impulse results from the resolution.
     *
     * @param contact The contact object.
     * @param impulse The physics impulse applied.
     */
    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        checkPlayerEntityCollision(contact);
    }
}
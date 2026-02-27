package inf112.zoombiegeddon.model.world.physics;

import com.badlogic.gdx.physics.box2d.*;
import inf112.zoombiegeddon.model.entity.Player;

/**
 * Handles collision events within the Box2D physics world.
 * Specifically manages player state transitions, such as detecting
 * if the player is grounded by checking contact with sensors.
 */
public class WorldContactListener implements ContactListener {

    /**
     * Called when two fixtures begin to touch.
     * Checks if the "feet" sensor of a player is involved in the collision
     * to set the player's grounded state to true.
     *
     * @param contact The contact object containing information about the collision.
     */
    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if ("feet".equals(fa.getUserData()) || "feet".equals(fb.getUserData())) {
            Object bodyAData = fa.getBody().getUserData();
            Object bodyBData = fb.getBody().getUserData();

            if (bodyAData instanceof Player) {
                ((Player) bodyAData).setGrounded(true);
            } else if (bodyBData instanceof Player) {
                ((Player) bodyBData).setGrounded(true);
            }
        }
    }

    /**
     * Called when two fixtures cease to touch.
     * Checks if the "feet" sensor of a player has left a surface
     * to set the player's grounded state to false.
     *
     * @param contact The contact object containing information about the collision.
     */
    @Override
    public void endContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if ("feet".equals(fa.getUserData()) || "feet".equals(fb.getUserData())) {
            Object bodyAData = fa.getBody().getUserData();
            Object bodyBData = fb.getBody().getUserData();

            if (bodyAData instanceof Player) {
                ((Player) bodyAData).setGrounded(false);
            } else if (bodyBData instanceof Player) {
                ((Player) bodyBData).setGrounded(false);
            }
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
        // No implementation needed for current logic
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
        // No implementation needed for current logic
    }
}
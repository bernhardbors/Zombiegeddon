package inf112.zoombiegeddon.model.world;

import com.badlogic.gdx.physics.box2d.*;
import inf112.zoombiegeddon.model.entity.Player;

public class WorldContactListener implements ContactListener {


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

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}

package inf112.zoombiegeddon.model.entity.pickups;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import inf112.zoombiegeddon.model.entity.Player;

public interface IPickup {

    void applyEffect(Player player);

    void collect(Player player);

    boolean isCollected();

    Body getBody();

    Vector2 getPosition();

    float getRenderSize();

    String getPickupType();
}

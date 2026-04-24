package inf112.zoombiegeddon.model.entity.pickups;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import inf112.zoombiegeddon.model.entity.IPlayer;

public interface IPickup {

    void applyEffect(IPlayer player);

    void collect(IPlayer player);

    boolean isCollected();

    Body getBody();

    Vector2 getPosition();

    float getRenderSize();

    String getPickupType();

    Color getPickupColor();
}

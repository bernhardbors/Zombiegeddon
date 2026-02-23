package inf112.zoombiegeddon.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public class Player extends Sprite {

    private int id;
    public Body b2body;

    public Player(World world) {
        // super(new Texture("PACEHOLDER.png"));
        this.id = 1;

        float width = getTexture().getWidth();
        float height = getTexture().getHeight();
        setSize(width, height);

        // Create Hitbox

        // .dispose() of the hitbox shape
    }

    // update player position
    public void update(float dt){
        // setPosition(getX(), getY());
    }

}

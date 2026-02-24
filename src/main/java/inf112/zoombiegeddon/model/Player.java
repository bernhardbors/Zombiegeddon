package inf112.zoombiegeddon.model;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Player extends Sprite {
    // Pixels Per Meter - adjust based on your game's scale
    public static final float PPM = 32.0f;

    public Body playerBody;

    public Player(World world, TextureRegion texture, Vector2 spawnPos) {
        super(texture);

        // Set visual size based on texture dimensions scaled to physics world
        float width = getRegionWidth() / PPM;
        float height = getRegionHeight() / PPM;
        setSize(width, height);

        definePlayer(world, spawnPos);
    }

    private void definePlayer(World world, Vector2 pos) {
        BodyDef bdef = new BodyDef();
        bdef.position.set(pos);
        bdef.type = BodyDef.BodyType.DynamicBody;

        playerBody = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        // Box2D setAsBox takes half-width and half-height
        shape.setAsBox(getWidth() / 2, getHeight() / 2);

        fdef.shape = shape;
        fdef.friction = 0.5f; // Optional: prevents sliding too much

        playerBody.createFixture(fdef).setUserData(this);
        playerBody.setFixedRotation(true);

        shape.dispose();
    }

    public void update(float dt) {
        // Sync sprite position with physics body
        setPosition(
                playerBody.getPosition().x - getWidth() / 2,
                playerBody.getPosition().y - getHeight() / 2
        );
    }
}

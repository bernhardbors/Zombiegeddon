package inf112.zoombiegeddon.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Player extends Sprite {

    public Body playerBody;

    public Player(World world) {
        super(new Texture("src/main/resources/assets/TestMapAndTilesets+PlayerDevSprite/Player.png"));

        // TODO: fix magic numbers
        float spriteWidth = getTexture().getWidth() / 32.0f;
        float spriteHeight = getTexture().getHeight() / 32.0f;
        setSize(spriteWidth, spriteHeight);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(10, 10);
        playerBody = world.createBody(bodyDef);

        PolygonShape playerHitbox = new PolygonShape();
        playerHitbox.setAsBox(spriteWidth / 2, spriteHeight / 2);

        FixtureDef fixtureDef = new FixtureDef();

        fixtureDef.shape = playerHitbox;

        playerBody.createFixture(fixtureDef);
        playerBody.setFixedRotation(true);

        playerHitbox.dispose();
    }

    // update player position
    public void update() {
        setPosition(
                playerBody.getPosition().x - getWidth() / 2,
                playerBody.getPosition().y - getHeight() / 2
        );
    }
}

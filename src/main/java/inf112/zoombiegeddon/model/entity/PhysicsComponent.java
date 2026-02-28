package inf112.zoombiegeddon.model.entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

/**
 * Manages the Box2D physics body for an entity.
 * This component defines the physical properties, including the main hitbox
 * and a foot sensor for ground detection, and provides methods for movement.
 */
public class PhysicsComponent {
    private final Body body;

    /**
     * Constructs a new PhysicsComponent and initializes its physical body.
     *
     * @param world    The Box2D world to create the body in.
     * @param pos      The initial position of the body.
     * @param width    The width of the entity.
     * @param height   The height of the entity.
     * @param userData The user data to associate with the main body fixture.
     */
    public PhysicsComponent(World world, Vector2 pos, float width, float height, Object userData) {
        this.body = defineBody(world, pos, width, height, userData);
    }

    /**
     * Defines the physics body, including its type, fixtures, and sensors.
     * Creates a dynamic body with fixed rotation, a main hitbox, and a sensor
     * at the bottom labeled "feet" for collision detection.
     *
     * @param world    The world to create the body in.
     * @param pos      The starting position.
     * @param width    The reference width for scaling the hitbox.
     * @param height   The reference height for scaling the hitbox.
     * @param userData Data to be stored in the body and main fixture.
     * @return The fully constructed Box2D Body.
     */
    private Body defineBody(World world, Vector2 pos, float width, float height, Object userData) {
        BodyDef bdef = new BodyDef();
        bdef.position.set(pos);
        bdef.type = BodyDef.BodyType.DynamicBody;

        Body body = world.createBody(bdef);
        body.setUserData(userData);
        body.setFixedRotation(true);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        // Main Body (Hitbox)
        float bodyWidth = width / 10f;
        float bodyHeight = height / 3f;
        shape.setAsBox(bodyWidth, bodyHeight);
        fdef.shape = shape;
        fdef.friction = 0f;
        body.createFixture(fdef).setUserData(userData);

        // Feet (Sensor for ground contact)
        float feetWidth = bodyWidth * 0.8f;
        float feetHeight = bodyHeight / 10f;
        shape.setAsBox(feetWidth, feetHeight, new Vector2(0, -bodyHeight), 0);
        fdef.shape = shape;
        fdef.isSensor = true;
        body.createFixture(fdef).setUserData("feet");

        shape.dispose();
        return body;
    }

    /**
     * Applies a linear impulse to the body's center.
     * Used for immediate movement changes like jumping or walking starts.
     *
     * @param impulse The impulse vector to apply.
     */
    public void applyImpulse(Vector2 impulse) {
        body.applyLinearImpulse(impulse, body.getWorldCenter(), true);
    }

    /**
     * Gets the current linear velocity of the body.
     *
     * @return A Vector2 representing the velocity in m/s.
     */
    public Vector2 getVelocity() {
        return body.getLinearVelocity();
    }

    /**
     * Gets the current world position of the body.
     *
     * @return A Vector2 representing the position.
     */
    public Vector2 getPosition() {
        return body.getPosition();
    }

    /**
     * Provides access to the underlying Box2D Body object.
     *
     * @return The Body instance.
     */
    public Body getBody() {
        return body;
    }
}
package inf112.zoombiegeddon.model.entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

/**
 * Manages the Box2D physics body for an entity.
 * This component defines the physical properties, including the main hitbox
 * and a foot sensor for ground detection, and provides methods for movement.
 */
public class PhysicsComponent implements IPhysicsComponent{
    private Body body;

    /**
     * Constructs a new PhysicsComponent and initializes its physical body.
     *
     * @param world    The Box2D world to create the body in.
     * @param pos      The initial position of the body.
     * @param hitboxW    The width of the entity.
     * @param hitboxh   The height of the entity.
     * @param userData The user data to associate with the main body fixture.
     */
    public PhysicsComponent(World world, Vector2 pos, float hitboxW, float hitboxh, Object userData) {
        this.body = defineBody(world, pos, hitboxW, hitboxh, userData);
    }

    /**
     * Defines the physics body, including its type, fixtures, and sensors.
     * Creates a dynamic body with fixed rotation, a main hitbox, and a sensor
     * at the bottom labeled "feet" for collision detection.
     *
     * @param world    The world to create the body in.
     * @param pos      The starting position.
     * @param hitboxW    The reference width for scaling the hitbox.
     * @param hitboxH   The reference height for scaling the hitbox.
     * @param userData Data to be stored in the body and main fixture.
     * @return The fully constructed Box2D Body.
     */
    private Body defineBody(World world, Vector2 pos, float hitboxW, float hitboxH, Object userData) {
        BodyDef bdef = new BodyDef();
        bdef.position.set(pos);
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.fixedRotation = true;
        Body newbody = world.createBody(bdef);

        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();

        shape.setAsBox(hitboxW / 2f, hitboxH / 2f);
        fdef.shape = shape;
        fdef.friction = 0f;
        newbody.createFixture(fdef).setUserData(userData);

        float sensorWidth = (hitboxW / 2f) * 0.9f;
        float sensorHeight = 0.3f;
        Vector2 sensorCenter = new Vector2(0, -hitboxH / 2f);

        shape.setAsBox(sensorWidth, sensorHeight, sensorCenter, 0);
        fdef.shape = shape;
        fdef.isSensor = true;
        newbody.createFixture(fdef).setUserData("feet");

        shape.dispose();
        return newbody;
    }

    /**
     * Returns the Box2D world this body belongs to.
     *
     * @return The physics world.
     */
    public World getWorld() {
        return body.getWorld();
    }

    /**
     * Applies a linear impulse to the body's center.
     *
     * @param impulse The impulse vector to apply.
     */
    public void applyImpulse(Vector2 impulse) {
        body.applyLinearImpulse(impulse, body.getWorldCenter(), true);
    }

    /**
     * Sets the gravity scale of the body.
     *
     * @param scale The gravity scale to apply.
     */
    public void setGravityScale(float scale) {
        body.setGravityScale(scale);
    }

    @Override
    public void setLinearVelocity(Vector2 velocity) {
        body.setLinearVelocity(velocity);
    }

    /**
     * Teleports the body to the given position, preserving its current angle.
     *
     * @param x target x position in world units
     * @param y target y position in world units
     */
    public void setTransform(float x, float y) {
        body.setTransform(x, y, body.getAngle());
    }

    /**
     * Sets the user data on the body, used for collision identification.
     *
     * @param userData the object to associate with this body
     */
    public void setUserData(Object userData) {
        body.setUserData(userData);
    }

    @Override
    public Vector2 getVelocity() {
        return body.getLinearVelocity();
    }

    @Override
    public Vector2 getPosition() {
        return body.getPosition();
    }

    @Override
    public Body getBody() {
        return body;
    }

}
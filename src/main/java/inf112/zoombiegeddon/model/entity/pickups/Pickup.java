package inf112.zoombiegeddon.model.entity.pickups;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import inf112.zoombiegeddon.model.entity.IPlayer;

/**
 * Abstract base class for all pickups in the game world.
 * Handles Box2D body creation, collection state, and delegates
 * the effect logic to subclasses via {@link #applyEffect(IPlayer)}.
 */
public abstract class Pickup implements IPickup {

    private final Body body;
    private final float renderSize;
    private final String pickupType;
    private final Color pickupColor;
    private boolean collected;

    /**
     * Constructs a pickup and creates its physics body.
     *
     * @param world         The Box2D world.
     * @param spawnPosition The position to spawn the pickup.
     * @param renderSize    The size used for both rendering and the physics hitbox.
     * @param pickupType    A string identifier for the pickup type.
     * @param pickupColor   The color associated with this pickup's visual effect.
     */
    protected Pickup(World world, Vector2 spawnPosition, float renderSize, String pickupType, Color pickupColor) {
        this.renderSize = renderSize;
        this.pickupType = pickupType;
        this.pickupColor = pickupColor;
        this.body = createBody(world, spawnPosition, renderSize);
        this.body.setUserData(this);
        this.collected = false;
    }

    private Body createBody(World world, Vector2 spawnPosition, float size) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(spawnPosition);
        bodyDef.fixedRotation = true;

        Body pickupBody = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(size / 2f, size / 2f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.8f;
        fixtureDef.friction = 0.8f;
        fixtureDef.restitution = 0f;

        pickupBody.createFixture(fixtureDef).setUserData("pickup");
        pickupBody.setLinearDamping(1.5f);

        shape.dispose();
        return pickupBody;
    }

    @Override
    public void collect(IPlayer player) {
        if (collected) {
            return;
        }

        collected = true;
        applyEffect(player);
    }

    @Override
    public boolean isCollected() {
        return collected;
    }

    @Override
    public Body getBody() {
        return body;
    }

    @Override
    public Vector2 getPosition() {
        return body.getPosition();
    }

    @Override
    public float getRenderSize() {
        return renderSize;
    }

    @Override
    public String getPickupType() {
        return pickupType;
    }

    @Override
    public Color getPickupColor() {
        return pickupColor;
    }
}

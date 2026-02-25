package inf112.zoombiegeddon.model;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.Map;

public class Player extends Sprite {

    public enum state { IDLE, WALKING, RUNNING, JUMPING, FALLING, PUNCHING, DEAD }
    public static final float PPM = 32.0f;

    public state currentState = state.IDLE;
    public state previousState = state.IDLE;

    private final Map<state, Animation<TextureRegion>> animations;

    public Body playerBody;
    private float stateTime;

    private boolean runningRight;

    public Player(World world, Map<state, Animation<TextureRegion>> animations, Vector2 spawnPos) {
        super(animations.get(state.IDLE).getKeyFrame(0));
        this.animations = animations;

        this.stateTime = 0f;

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

        float hitboxWidthMeters = 20f / PPM;
        float hitboxHeightMeters = 34f / PPM;
        shape.setAsBox(hitboxWidthMeters / 2, hitboxHeightMeters / 2);

        fdef.shape = shape;
        fdef.friction = 0.5f;

        playerBody.createFixture(fdef).setUserData(this);
        playerBody.setFixedRotation(true);

        shape.dispose();
    }

    public void update(float dt) {
        stateTime += dt;

        TextureRegion region = getFrame(dt);
        setRegion(region);


        setPosition(
                playerBody.getPosition().x - getWidth() / 2,
                playerBody.getPosition().y - getHeight() / 2
        );
    }

    private TextureRegion getFrame(float dt) {
        currentState = getState();
        TextureRegion region = animations.get(currentState).getKeyFrame(stateTime, true);
        float velocityX = playerBody.getLinearVelocity().x;

        if (velocityX > 0.1f) {
            runningRight = true;
        } else if (velocityX < -0.1f) {
            runningRight = false;
        }

        if (runningRight && !region.isFlipX()) {
            region.flip(true, false);
        }
        else if (!runningRight && region.isFlipX()) {
            region.flip(true, false);
        }

        if (currentState != previousState) {
            stateTime = 0;
        }
        previousState = currentState;

        return region;
    }

    private state getState() {
        if (playerBody.getLinearVelocity().y > 0.1f) return state.JUMPING;
        if (playerBody.getLinearVelocity().y < -0.1f) return state.FALLING;
        if (Math.abs(playerBody.getLinearVelocity().x) > 0.1f) return state.WALKING;
        return state.IDLE;
    }
}

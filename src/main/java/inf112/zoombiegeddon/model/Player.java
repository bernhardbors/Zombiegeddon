package inf112.zoombiegeddon.model;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import inf112.zoombiegeddon.controller.PlayerController;

import java.util.Map;

public class Player extends Sprite {

    public enum State {IDLE, WALKING, RUNNING, JUMPING, FALLING, PUNCHING, DEAD}

    public static final float PPM = 32.0f;

    public State currentState = State.IDLE;
    public State previousState = State.IDLE;

    private final Map<State, Animation<TextureRegion>> animations;

    public Body playerBody;
    private float stateTime;

    private boolean runningRight;
    public static boolean isRunning;

    public Player(World world, Map<State, Animation<TextureRegion>> animations, Vector2 spawnPos) {
        super(animations.get(State.IDLE).getKeyFrame(0));
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

        float hitboxWidthMeters = 15f / PPM;
        float hitboxHeightMeters = 40f / PPM;
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

        boolean loop = (currentState != State.PUNCHING);

        TextureRegion region = animations.get(currentState).getKeyFrame(stateTime, loop);
        float velocityX = playerBody.getLinearVelocity().x;

        if (velocityX > 0.1f) {
            runningRight = true;
        } else if (velocityX < -0.1f) {
            runningRight = false;
        }

        if (runningRight && !region.isFlipX()) {
            region.flip(true, false);
        } else if (!runningRight && region.isFlipX()) {
            region.flip(true, false);
        }

        if (currentState != previousState) stateTime = 0;
        previousState = currentState;
        return region;
    }

    private State getState() {
        if (previousState == State.PUNCHING && !animations.get(State.PUNCHING).isAnimationFinished(stateTime)) {
            return State.PUNCHING;
        }
        if (PlayerController.hitPressed()) return State.PUNCHING;
        if (playerBody.getLinearVelocity().y < -0.1f) return State.FALLING;
        if (playerBody.getLinearVelocity().y > 0.1f) return State.JUMPING;
        if (isRunning) return State.RUNNING;
        if (Math.abs(playerBody.getLinearVelocity().x) > 0.1f) return State.WALKING;

        return State.IDLE;
    }

    /**
     * Helper method for the controller to check if the player is currently "busy".
     */
    public boolean isPerformingAction() {
        return currentState == State.PUNCHING && !animations.get(State.PUNCHING).isAnimationFinished(stateTime);
    }

}

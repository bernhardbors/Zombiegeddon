package inf112.zoombiegeddon.model.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import inf112.zoombiegeddon.model.animations.AnimationHandler;

public class Player extends Sprite {

    // --- Configuration Constants ---
    public static final float PPM = 32.0f;
    private static final float JUMP_IMPULSE = 5.0f;
    private static final float MOVE_IMPULSE = 0.7f;
    private static float VELOCITY_THRESHOLD = 0.1f;
    private static final float WALKING_SPEED = 0.7f;
    private static final float RUNNING_SPEED = 4f;

    // --- Player States ---
    public enum State { IDLE, WALKING, RUNNING, JUMPING, FALLING, PUNCHING, DEAD }

    private State currentState = State.IDLE;
    private State previousState = State.IDLE;
    private float stateTime = 0f;

    private boolean facingRight = true;
    private boolean isGrounded = false;
    private boolean isRunning = false;

    // --- Physics ---
    public Body playerBody;
    private final AnimationHandler animationHandler;

    public Player(World world, Vector2 spawnPos, AnimationHandler animationHandler) {

        super(animationHandler.getPlayerAnimations().get(State.IDLE).getKeyFrame(0));

        this.animationHandler = animationHandler;

        float textureWidth = getRegionWidth() / PPM;
        float textureHeight = getRegionHeight() / PPM;
        setSize(textureWidth, textureHeight);

        definePlayer(world, spawnPos, textureWidth, textureHeight);
    }

    /**
     * Initializes the Box2D body and fixtures.
     */
    private void definePlayer(World world, Vector2 pos, float textureWidth, float textureHeight) {
        BodyDef bdef = new BodyDef();
        bdef.position.set(pos);
        bdef.type = BodyDef.BodyType.DynamicBody;

        playerBody = world.createBody(bdef);
        playerBody.setUserData(this);
        playerBody.setFixedRotation(true);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        float bodyWidth = textureWidth / 10f;
        float bodyHeight = textureHeight / 3f;

        shape.setAsBox(bodyWidth, bodyHeight);
        fdef.shape = shape;
        fdef.friction = 0f;
        playerBody.createFixture(fdef).setUserData(this);

        float feetWidth = bodyWidth * 0.8f;
        float feetHeight = bodyHeight / 10f;

        shape.setAsBox(feetWidth, feetHeight, new Vector2(0, -bodyHeight), 0);
        fdef.shape = shape;
        fdef.isSensor = true;
        playerBody.createFixture(fdef).setUserData("feet");

        shape.dispose();
    }

    /**
     * Updates the player logic and sprite position.
     */
    public void update(float dt) {
        updateState(dt);

        // Get the correct frame from the handler (which handles flipping logic)
        TextureRegion frame = animationHandler.getFrame(currentState, stateTime, facingRight);
        setRegion(frame);

        // Sync sprite position with the center of the Box2D body
        setPosition(
                playerBody.getPosition().x - getWidth() / 2,
                playerBody.getPosition().y - getHeight() / 2
        );
    }

    /**
     * Determines the current state and horizontal orientation.
     */
    private void updateState(float dt) {
        stateTime += dt;
        previousState = currentState;

        float vx = playerBody.getLinearVelocity().x;
        float vy = playerBody.getLinearVelocity().y;

        // 1. Determine Facing Direction
        if (vx > VELOCITY_THRESHOLD) {
            facingRight = true;
        } else if (vx < -VELOCITY_THRESHOLD) {
            facingRight = false;
        }

        // 2. Determine Movement State
        if (!isGrounded) {
            currentState = (vy > VELOCITY_THRESHOLD) ? State.JUMPING : State.FALLING;
        } else if (Math.abs(vx) > VELOCITY_THRESHOLD) {
            currentState = isRunning ? State.RUNNING : State.WALKING;
        } else {
            currentState = State.IDLE;
        }

        // 3. Reset state timer if the state changed
        if (previousState != currentState) {
            stateTime = 0;
        }
    }

    // --- Movement Methods ---

    public void jump() {
        if (isGrounded) {
            playerBody.applyLinearImpulse(new Vector2(0, JUMP_IMPULSE), playerBody.getWorldCenter(), true);
            isGrounded = false;
        }
    }

    public void applyHorizontalImpulse(boolean left, boolean right) {
        if (left) {
            playerBody.applyLinearImpulse(new Vector2(-MOVE_IMPULSE, 0), playerBody.getWorldCenter(), true);
        }
        if (right) {
            playerBody.applyLinearImpulse(new Vector2(MOVE_IMPULSE, 0), playerBody.getWorldCenter(), true);
        }
    }

    // --- Getters and Setters ---

    public void setGrounded(boolean grounded) {
        this.isGrounded = grounded;
    }

    public void setRunning(boolean running) {
        this.isRunning = running;
    }

    public void setVelocityThreshold(float threshold) {
        VELOCITY_THRESHOLD = threshold;
    }

    public boolean getIsGrounded() {
        return isGrounded;
    }

    public float getWalkingThreshold() {
        return WALKING_SPEED;
    }

    public float getRunningThreshold() {
        return RUNNING_SPEED;
    }

    public boolean getIsRunning() {
        return isRunning;
    }

    public State getCurrentState() {
        return currentState;
    }
}
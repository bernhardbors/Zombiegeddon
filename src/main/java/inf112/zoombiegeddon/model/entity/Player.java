package inf112.zoombiegeddon.model.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import inf112.zoombiegeddon.controller.PlayerController;
import inf112.zoombiegeddon.model.animations.AnimationHandler;

public class Player extends Sprite {

    public enum State {IDLE, WALKING, RUNNING, JUMPING, FALLING, PUNCHING, DEAD}

    public static final float PPM = 32.0f;

    public State currentState = State.IDLE;
    public State previousState = State.IDLE;

    public Body playerBody;
    private float stateTime = 0f;

    private boolean runningRight;
    public static boolean isRunning;
    private boolean isGrounded = false;

    private final AnimationHandler animationHandler;

    public Player(World world, Vector2 spawnPos, AnimationHandler animationHandler) {
        super(animationHandler.getPlayerAnimations().get(State.IDLE).getKeyFrame(0));

        this.animationHandler = animationHandler;

        // set size of the player sprite texture
        float textureWidth = getRegionWidth() / PPM;
        float textureHeight = getRegionHeight() / PPM;
        setSize(textureWidth, textureHeight);

        definePlayer(world, spawnPos, textureWidth, textureHeight);
    }

    /**
     * Define Player hitbox and spawn position.
     *
     * @param world
     * @param pos
     */
    private void definePlayer(World world, Vector2 pos, float textureWidth, float textureHeight) {
        BodyDef hitbox = new BodyDef();
        hitbox.position.set(pos);
        hitbox.type = BodyDef.BodyType.DynamicBody;

        playerBody = world.createBody(hitbox);
        playerBody.setUserData(this);
        playerBody.setFixedRotation(true);

        FixtureDef hitboxShape = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        // --- MAIN BODY FIXTURE ---
        float bodyWidth = textureWidth / 10f;
        float bodyHeight = textureHeight / 3f;

        shape.setAsBox(bodyWidth,  bodyHeight);
        hitboxShape.shape = shape;
        hitboxShape.friction = 0f;
        playerBody.createFixture(hitboxShape).setUserData(this);

        // --- FEET SENSOR (For jump logic/ground detection) ---
        float feetWidth = (bodyWidth / 2f) * 0.8f;
        float feetHeight = bodyHeight / 10f;

        shape.setAsBox(feetWidth, feetHeight, new Vector2(0, -bodyHeight), 0);

        hitboxShape.shape = shape;
        hitboxShape.isSensor = true;
        playerBody.createFixture(hitboxShape).setUserData("feet");

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

        TextureRegion region = animationHandler.getPlayerAnimations().get(currentState).getKeyFrame(stateTime, loop);
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

        if (isPerformingAction()) return State.PUNCHING;
        if (PlayerController.hitPressed()) return State.PUNCHING;

        if (!isGrounded) {
            if (playerBody.getLinearVelocity().y > 0.1f) return State.JUMPING;
            if (playerBody.getLinearVelocity().y < -0.1f) return State.FALLING;
        }

        float velocityX = Math.abs(playerBody.getLinearVelocity().x);
        if (velocityX > 0.1f) {
            return isRunning ? State.RUNNING : State.WALKING;
        }

        return State.IDLE;
    }

    public void setGrounded(boolean grounded) {
        this.isGrounded = grounded;
    }

    public void jump() {
        System.out.println("Jump Attempted. Grounded: " + isGrounded);
        if (isGrounded) {
            playerBody.applyLinearImpulse(new Vector2(0, 5f), playerBody.getWorldCenter(), true);
            isGrounded = false;
        }
    }

    /**
     * Helper method for the controller to check if the player is currently "busy".
     */
    public boolean isPerformingAction() {
        return currentState == State.PUNCHING && !animationHandler.getPlayerAnimations().get(State.PUNCHING).isAnimationFinished(stateTime);
    }

}

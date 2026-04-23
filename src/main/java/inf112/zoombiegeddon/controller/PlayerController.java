package inf112.zoombiegeddon.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import inf112.zoombiegeddon.model.entity.IPlayer;

/**
 * Handles user input and translates it into player actions and movement.
 * Extends {@link InputAdapter} to handle discrete inf112.zoombiegeddon.events like jumping,
 * while providing an update loop for continuous movement and friction.
 */
public class PlayerController extends InputAdapter {

    private final IPlayer player;

    private static final float WALKING_SPEED = 4f;
    private static final float RUNNING_SPEED = 8f;
    private static final float FRICTION_COEFFICIENT = 0.9f;
    private boolean isJumpKeyPressed = false;

    /**
     * Constructs a new PlayerController for the specified player.
     *
     * @param player The player instance to be controlled.
     */
    public PlayerController(IPlayer player) {
        this.player = player;
    }

    /**
     * Updates the controller logic. Should be called once per frame.
     *
     * @param dt The time in seconds since the last update.
     */
    public void update(float dt) {
        handlePlayerMovement();

    }

    /**
     * Handles discrete key press inf112.zoombiegeddon.events.
     * Used for actions that should trigger only once per press, such as jumping
     * or exiting the application.
     *
     * @param keycode The code of the key that was pressed.
     * @return True if the input was processed.
     */
    @Override
    public boolean keyDown(int keycode) {
        if ((keycode == Input.Keys.SPACE || keycode == Input.Keys.W || keycode == Input.Keys.UP) && !isJumpKeyPressed) {
            isJumpKeyPressed = true;
            player.jump();
            return true;
        }
        return false;
    }

    /**
     * Handles released keys for jumping. When a jump key is released, the jump flag is reset.
     * @param keycode The code of the key that was released.
     * @return True if the input event was processed.
     */
    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.SPACE || keycode == Input.Keys.W || keycode == Input.Keys.UP) {
            isJumpKeyPressed = false;
            return true;
        }
        return false;
    }

    /**
     * Processes continuous horizontal movement and applies friction when needed.
     * Walking and running speed are determined by whether the shift key is pressed.
     */
    private void handlePlayerMovement() {
        boolean left = isLeftPressed();
        boolean right = isRightPressed();

        player.setRunning(isShiftPressed());
        float baseSpeed = player.isRunning() ? RUNNING_SPEED : WALKING_SPEED;
        float targetMax = baseSpeed * player.getActiveSpeedMultiplier();

        Vector2 vel = player.getPhysics().getVelocity();

        if (left && vel.x > -targetMax) {
            player.applyHorizontalImpulse(true, false);
        }
        if (right && vel.x < targetMax) {
            player.applyHorizontalImpulse(false, true);
        }

        if (!left && !right) {
            player.getPhysics().getBody().setLinearVelocity(vel.x * FRICTION_COEFFICIENT, vel.y);
        } else if (Math.abs(vel.x) > targetMax) {
            // Gradual slowdown to targetMax if Shift is released while moving fast
            player.getPhysics().getBody().setLinearVelocity(vel.x * FRICTION_COEFFICIENT, vel.y);
        }
    }

    /**
     * Checks if a hit/attack action is requested.
     *
     * @return True if the hit key (K) is currently pressed.
     */
    public boolean hitPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.K);
    }

    /**
     * Checks whether the player is moving left.
     *
     * @return True if A or Left Arrow is pressed, false otherwise.
     */
    public boolean isLeftPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT);
    }

    /**
     * Checks whether the player is moving right.
     *
     * @return True if D or Right Arrow is pressed, false otherwise.
     */
    public boolean isRightPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT);
    }

    /**
     * Checks whether a jump key was just pressed.
     *
     * @return True if Space, W, or Up Arrow was just pressed.
     */
    public boolean isJumpPressed() {
        return isJumpKeyPressed;
                /* Gdx.input.isKeyPressed(Input.Keys.SPACE) ||
                Gdx.input.isKeyPressed(Input.Keys.W) ||
                Gdx.input.isKeyPressed(Input.Keys.UP); */
    }

    /**
     * Checks whether either shift key is currently pressed.
     *
     * @return True if Left Shift or Right Shift is pressed, false otherwise.
     */
    private boolean isShiftPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT);
    }
}
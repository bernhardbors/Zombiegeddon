package inf112.zoombiegeddon.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import inf112.zoombiegeddon.model.entity.Player;

/**
 * Handles user input and translates it into player actions and movement.
 * Extends {@link InputAdapter} to handle discrete events like jumping,
 * while providing an update loop for continuous movement and friction.
 */
public class PlayerController extends InputAdapter {

    private final Player player;

    private static final float WALKING_SPEED = 4f;
    private static final float RUNNING_SPEED = 8f;
    private static final float FRICTION_COEFFICIENT = 0.9f;

    /**
     * Constructs a new PlayerController for the specified player.
     *
     * @param player The player instance to be controlled.
     */
    public PlayerController(Player player) {
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
     * Handles discrete key press events.
     * Used for actions that should trigger only once per press, such as jumping
     * or exiting the application.
     *
     * @param keycode The code of the key that was pressed.
     * @return True if the input was processed.
     */
    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.SPACE || keycode == Input.Keys.W || keycode == Input.Keys.UP) {
            player.jump();
            return true;
        }
        if (keycode == Input.Keys.ESCAPE) {
            Gdx.app.exit();
            return true;
        }
        return false;
    }

    /**
     * Processes continuous movement input and applies physics logic.
     * Manages horizontal impulses based on current speed (walking vs running)
     * and applies a custom friction coefficient for responsive stopping.
     */
    private void handlePlayerMovement() {
        boolean left = isLeftPressed();
        boolean right = isRightPressed();

        player.setRunning(isShiftPressed());
        float targetMax = player.isRunning() ? RUNNING_SPEED : WALKING_SPEED;

        Vector2 vel = player.getPhysics().getVelocity();

        // 1. Horizontal movement
        if (left && vel.x > -targetMax) {
            player.applyHorizontalImpulse(true, false);
        }
        if (right && vel.x < targetMax) {
            player.applyHorizontalImpulse(false, true);
        }

        // 2. Responsive friction
        // Applied if no movement keys are pressed or if the velocity exceeds targetMax
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
    public static boolean hitPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.K);
    }

    /** @return True if A or Left Arrow is pressed. */
    private boolean isLeftPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT);
    }

    /** @return True if D or Right Arrow is pressed. */
    private boolean isRightPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT);
    }

    /** @return True if Space, W, or Up Arrow was just pressed. */
    private boolean isJumpPressed() {
        return Gdx.input.isKeyJustPressed(Input.Keys.SPACE) ||
                Gdx.input.isKeyJustPressed(Input.Keys.W) ||
                Gdx.input.isKeyJustPressed(Input.Keys.UP);
    }

    /** @return True if either Shift key is pressed. */
    private boolean isShiftPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT);
    }
}
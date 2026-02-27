package inf112.zoombiegeddon.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import inf112.zoombiegeddon.model.entity.Player;

import java.util.HashMap;

/**
 * Handles input management and physics updates for the Player character.
 * Extends {@link InputAdapter} to handle discrete input events while
 * using polling for continuous movement logic.
 */
public class PlayerController extends InputAdapter {

    /**
     * The player instance controlled by this controller.
     */
    private final Player player;

    /**
     * Map storing action names linked to multiple possible key codes.
     */
    private static final HashMap<String, int[]> keyBindings = new HashMap<>();

    /**
     * The current maximum horizontal velocity allowed, adjusted by sprinting.
     */
    private float MAX_VELOCITY;

    /**
     * Constructs a new PlayerController and initializes default key bindings.
     *
     * @param player The player model to be controlled.
     */
    public PlayerController(Player player) {
        this.player = player;
        setupDefaultBindings();
    }

    /**
     * Initializes the default keyboard mappings for movement, jumping, and system actions.
     */
    private void setupDefaultBindings() {
        keyBindings.put("LEFT", new int[]{Input.Keys.A, Input.Keys.LEFT});
        keyBindings.put("RIGHT", new int[]{Input.Keys.D, Input.Keys.RIGHT});
        keyBindings.put("JUMP", new int[]{Input.Keys.SPACE, Input.Keys.W, Input.Keys.UP});
        keyBindings.put("SHIFT", new int[]{Input.Keys.SHIFT_LEFT, Input.Keys.SHIFT_RIGHT});
        keyBindings.put("HIT", new int[]{Input.Keys.K});
        keyBindings.put("QUIT", new int[]{Input.Keys.ESCAPE});
    }

    /**
     * Checks if any key bound to a specific action is currently being held down.
     *
     * @param action The string identifier for the action.
     * @return true if any assigned key is pressed, false otherwise.
     */
    private static boolean isActionPressed(String action) {
        if (!keyBindings.containsKey(action)) return false;
        for (int key : keyBindings.get(action)) {
            if (Gdx.input.isKeyPressed(key)) return true;
        }
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        // Check if the pressed key is mapped to "JUMP"
        for (int boundKey : keyBindings.get("JUMP")) {
            if (keycode == boundKey) {
                player.jump();
                return true;
            }
        }
        return false;
    }

    /**
     * Updates player movement and system inputs. Should be called once per frame.
     * * @param dt The time in seconds since the last render.
     */
    public void update(float dt) {
        handlePlayerMovement();
        handleSystemInput();
    }

    /**
     * Processes movement-related input polling and updates the player's physical state.
     */
    private void handlePlayerMovement() {
        boolean rightPressed = isActionPressed("RIGHT");
        boolean leftPressed = isActionPressed("LEFT");
        player.setRunning(isActionPressed("SHIFT"));

        float targetMax = player.getIsRunning() ? player.getRunningThreshold() : player.getWalkingThreshold();

        Vector2 vel = player.playerBody.getLinearVelocity();

        if (!player.getIsRunning() && Math.abs(vel.x) > targetMax) {
            player.playerBody.setLinearVelocity(Math.signum(vel.x) * targetMax, vel.y);
            vel = player.playerBody.getLinearVelocity();
        }


        if (leftPressed && vel.x > -targetMax) {
            player.applyHorizontalImpulse(true, false);
        }
        if (rightPressed && vel.x < targetMax) {
            player.applyHorizontalImpulse(false, true);
        }

        if (!leftPressed && !rightPressed) {
            player.playerBody.setLinearVelocity(vel.x * 0.4f, vel.y);
        }

    }

    /**
     * Processes system-level inputs, such as quitting the application.
     */
    private void handleSystemInput() {
        if (isActionPressed("QUIT")) Gdx.app.exit();
    }

    /**
     * Static utility to check if the attack/hit action is currently triggered.
     * Used by the {@link Player} class to determine animation state transitions.
     *
     * @return true if the "HIT" key is pressed, false otherwise.
     */
    public static Boolean hitPressed() {
        return isActionPressed("HIT");
    }
}

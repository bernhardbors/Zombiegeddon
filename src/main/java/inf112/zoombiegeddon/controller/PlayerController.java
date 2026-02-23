package inf112.zoombiegeddon.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import inf112.zoombiegeddon.model.Player;

import java.util.HashMap;

public class PlayerController extends InputAdapter {

    private final Player player;
    private final float MAX_VELOCITY = 4.0f;
    private final float JUMP_FORCE = 12.0f;

    private final HashMap<String, int[]> keyBindings = new HashMap<>();

    public PlayerController(Player player) {
        this.player = player;
        setupDefaultBindings();
    }

    private void setupDefaultBindings() {
        keyBindings.put("LEFT", new int[]{Input.Keys.A, Input.Keys.LEFT});
        keyBindings.put("RIGHT", new int[]{Input.Keys.D, Input.Keys.RIGHT});
        keyBindings.put("JUMP", new int[]{Input.Keys.SPACE, Input.Keys.W, Input.Keys.UP});
    }

    /**
     * Checks if any key bound to the given action is currently pressed (Polling)
     */
    private boolean isActionPressed(String action) {
        if (!keyBindings.containsKey(action)) return false;
        for (int key : keyBindings.get(action)) {
            if (Gdx.input.isKeyPressed(key)) return true;
        }
        return false;
    }

    public void update(float dt) {
        handlePlayerMovement();
    }

    private void handlePlayerMovement() {
        Vector2 vel = player.playerBody.getLinearVelocity();
        Vector2 pos = player.playerBody.getWorldCenter();

        boolean leftPressed = isActionPressed("LEFT");
        boolean rightPressed = isActionPressed("RIGHT");

        if (leftPressed && vel.x > -MAX_VELOCITY) {
            player.playerBody.applyLinearImpulse(new Vector2(-0.5f, 0), pos, true);
        } else if (rightPressed && vel.x < MAX_VELOCITY) {
            player.playerBody.applyLinearImpulse(new Vector2(0.5f, 0), pos, true);
        } else {

            player.playerBody.setLinearVelocity(vel.x * 0.5f, vel.y);

            if (Math.abs(vel.x) < 0.1f) {
                player.playerBody.setLinearVelocity(0, vel.y);
            }
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        System.out.println("Key pressed: " + keycode);
        for (int boundKey : keyBindings.get("JUMP")) {
            if (keycode == boundKey) {
                if (Math.abs(player.playerBody.getLinearVelocity().y) < 0.01f) {
                    player.playerBody.applyLinearImpulse(new Vector2(0, JUMP_FORCE), player.playerBody.getWorldCenter(), true);
                }
                return true;
            }
        }
        return false;
    }
}

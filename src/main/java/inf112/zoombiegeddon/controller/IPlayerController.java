package inf112.zoombiegeddon.controller;

import com.badlogic.gdx.InputProcessor;

/**
 * Defines the contract for a player input controller.
 * Extends {@link InputProcessor} so implementations can be registered
 * directly with a LibGDX {@code InputMultiplexer}.
 */
public interface IPlayerController extends InputProcessor {

    /**
     * Advances the controller state by one frame.
     *
     * @param dt time in seconds since the last update
     */
    void update(float dt);

    /** @return {@code true} if the left movement key is currently pressed */
    boolean isLeftPressed();

    /** @return {@code true} if the right movement key is currently pressed */
    boolean isRightPressed();

    /** @return {@code true} if a jump key is currently pressed */
    boolean isJumpPressed();


}

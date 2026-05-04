package inf112.zoombiegeddon.controller;

import com.badlogic.gdx.InputProcessor;

/**
 * Defines the contract for a global level input controller.
 * Extends {@link InputProcessor} so implementations can be registered
 * directly with a LibGDX {@code InputMultiplexer}.
 */
public interface ILevelController extends InputProcessor {
}

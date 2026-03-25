package inf112.zoombiegeddon.events;

import inf112.zoombiegeddon.audio.AudioManager;

/**
 * Defines global events that can occur in the game.
 * These events are used by {@link GameEventBus} to notify listeners
 * such as {@link AudioManager}.
 */
public enum GameEvent {
    PLAYER_JUMP,
    UI_BUTTON_CLICK,
    ENEMY_HIT,
    PLAYER_MOVE_START,
    PLAYER_RUN_START,
    PLAYER_RUN_STOP,
    PLAYER_MOVE_STOP,
    PLAYER_DEATH;
}

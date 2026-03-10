package inf112.zoombiegeddon.events;

import inf112.zoombiegeddon.audio.AudioManager;

/**
 * Defines all global events that can occur in game.
 * Used by {@link GameEventBus} to notify listeners, for example {@link AudioManager}
 */
public enum GameEvent {
    PLAYER_JUMP,
    UI_BUTTON_CLICK, // Examples for later
    ENEMY_HIT,
    PLAYER_MOVE_START,
    PLAYER_MOVE_STOP;// Examples for later
}

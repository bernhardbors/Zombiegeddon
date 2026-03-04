package inf112.zoombiegeddon.events;

import inf112.zoombiegeddon.audio.AudioManager;

/**
 * Defines all global events that can occur in game.
 * Used by {@link GameEventBus} to notify listeners, for example {@link AudioManager}
 * Definerer alle globale hendelser som kan inntreffe i spillet.
 * Brukes av GameEventBus for å gi beskjed til lyttere (f.eks. AudioManager).
 */
public enum GameEvent {
    PLAYER_JUMP,
    UI_BUTTON_CLICK, // Examples for later
    ENEMY_HIT        // Examples for later
}

package inf112.zoombiegeddon.audio;

import inf112.zoombiegeddon.events.GameEvent;

/**
 * Interface for classes willing to listen to game events,
 * typically to playing a sound.
 */
public interface AudioListener {
    void onEvent(GameEvent event);
}

package inf112.zoombiegeddon.events;

/**
 * Interface for classes willing to listen to game events,
 * typically to playing a sound.
 */
public interface IEventListener {
    void onEvent(GameEvent event);
}

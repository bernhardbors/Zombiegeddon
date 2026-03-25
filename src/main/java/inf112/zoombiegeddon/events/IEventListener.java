package inf112.zoombiegeddon.events;

/**
 * Interface for objects that listen for game events.
 * Typically used by systems that react to events, such as audio handling.
 */
public interface IEventListener {

    /**
     * Handles a posted game event.
     *
     * @param event The event to handle.
     */
    void onEvent(GameEvent event);

}

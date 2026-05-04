package inf112.zoombiegeddon.events;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple event bus for registering listeners and broadcasting game events.
 * Listeners that implement {@link IEventListener} can subscribe to receive
 * events posted through this class.
 */
public class GameEventBus {

    private static final List<IEventListener> listeners = new ArrayList<>();

    private GameEventBus(){}

    /**
     * Registers a listener if it is not already registered.
     *
     * @param listener The listener to register.
     */
    public static void register(IEventListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    /**
     * Unregisters a listener so it no longer receives events.
     *
     * @param listener The listener to remove.
     */
    public static void unregister(IEventListener listener) {
        listeners.remove(listener);
    }

    /**
     * Posts an event to all registered listeners.
     *
     * @param event The event to broadcast.
     */
    public static void post(GameEvent event) {
        List<IEventListener> safeListeners = new ArrayList<>(listeners);

        for (IEventListener listener : safeListeners) {
            listener.onEvent(event);
        }
    }

    /**
     * Clears all registered listeners.
     * Should be called when transitioning between major game states
     * to prevent memory leaks and duplicate event handling.
     */
    public static void clear() {
        listeners.clear();
    }
}

package inf112.zoombiegeddon.events;

import java.util.ArrayList;
import java.util.List;

public class GameEventBus {
    private static final List<IEventListener> listeners = new ArrayList<>();

    public static void register(IEventListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public static void unregister(IEventListener listener) {
        listeners.remove(listener);
    }

    public static void post(GameEvent event) {
        for (IEventListener listener : listeners) {
            listener.onEvent(event);
        }
    }
}

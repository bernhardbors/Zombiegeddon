package inf112.zoombiegeddon.events;

import inf112.zoombiegeddon.audio.AudioListener;

import java.util.ArrayList;
import java.util.List;

public class GameEventBus {
    private static final List<AudioListener> listeners = new ArrayList<>();

    public static void register(AudioListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public static void unregister(AudioListener listener) {
        listeners.remove(listener);
    }

    public static void post(GameEvent event) {
        for (AudioListener listener : listeners) {
            listener.onEvent(event);
        }
    }
}

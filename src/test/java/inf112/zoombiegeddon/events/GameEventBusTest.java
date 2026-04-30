package inf112.zoombiegeddon.events;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameEventBusTest {

    @BeforeEach
    void setUp() {
        GameEventBus.clear();
    }

    @Test
    void testRegisteredListenerReceivesEvent() {
        boolean[] received = {false};
        GameEventBus.register(event -> {
            if (event == GameEvent.PLAYER_DEATH) received[0] = true;
        });

        GameEventBus.post(GameEvent.PLAYER_DEATH);

        assertTrue(received[0], "Registered listener should receive posted event");
    }

    @Test
    void testUnregisteredListenerDoesNotReceiveEvent() {
        boolean[] received = {false};
        IEventListener listener = event -> received[0] = true;

        GameEventBus.register(listener);
        GameEventBus.unregister(listener);
        GameEventBus.post(GameEvent.PLAYER_DEATH);

        assertFalse(received[0], "Unregistered listener should not receive events");
    }

    @Test
    void testSameListenerNotRegisteredTwice() {
        int[] count = {0};
        IEventListener listener = event -> count[0]++;

        GameEventBus.register(listener);
        GameEventBus.register(listener);
        GameEventBus.post(GameEvent.PLAYER_DEATH);

        assertEquals(1, count[0], "Listener should only receive event once even if registered twice");
    }

    @Test
    void testClearRemovesAllListeners() {
        boolean[] received = {false};
        GameEventBus.register(event -> received[0] = true);

        GameEventBus.clear();
        GameEventBus.post(GameEvent.PLAYER_DEATH);

        assertFalse(received[0], "No listeners should receive events after clear()");
    }

    @Test
    void testMultipleListenersAllReceiveEvent() {
        boolean[] first = {false};
        boolean[] second = {false};

        GameEventBus.register(event -> first[0] = true);
        GameEventBus.register(event -> second[0] = true);

        GameEventBus.post(GameEvent.PLAYER_JUMP);

        assertTrue(first[0], "First listener should receive event");
        assertTrue(second[0], "Second listener should receive event");
    }
}

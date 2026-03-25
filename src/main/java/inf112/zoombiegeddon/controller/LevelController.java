package inf112.zoombiegeddon.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import inf112.zoombiegeddon.events.GameEvent;
import inf112.zoombiegeddon.events.GameEventBus;

/**
 * Class for handling user input on a global level.
 */
public class LevelController extends InputAdapter {
    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.P) {
            GameEventBus.post(GameEvent.TOGGLE_PAUSE);
            return true;
        }
        if (keycode == Input.Keys.ESCAPE) {
            Gdx.app.exit();
            return true;
        }
        return false;
    }
}

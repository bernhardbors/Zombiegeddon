package inf112.zoombiegeddon.audio;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;

/**
 * Registry for all in-game sound effects.
 * This enum works as a central source of truth for all audio resources.
 * By using this enum, we avoid magic strings and assures both {@link AudioManager}
 * and game logic consistently refers to the same files.
 */
public enum Sfx {
    JUMP1("assets/audio/sfx/jumppp11.wav"),
    JUMP2("assets/audio/sfx/jumppp22.wav"),
    BUTTON_CLICK("assets/audio/sfx/button_click.wav"),
    FOOTSTEPS("assets/audio/sfx/footstep_soft.wav"),
    FOOTSTEPS_RUN("assets/audio/sfx/footstep_run.wav");

    private final String path;

    /**
     * @param  path The name of the file (including extension) located in "assets/audio/sfx/".
     */
    Sfx(String path) {
        this.path = path;
    }


    /**
     * @return The unique key for this sound.
     */
    public String path() {
        return path;
    }

    public static void queueAssets(AssetManager manager) {
        for (Sfx s : Sfx.values()) {
            manager.load(s.path(), Sound.class);
        }
    }
}

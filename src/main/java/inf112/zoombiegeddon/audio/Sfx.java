package inf112.zoombiegeddon.audio;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;

/**
 * Registry for all in-game sound effects.
 * This enum serves as a central source of truth for audio resources.
 * By using this enum, the code avoids magic strings and ensures that
 * both {@link AudioManager} and game logic refer to the same sound files.
 */
public enum Sfx {
    JUMP1("assets/audio/sfx/jumppp11.wav"),
    JUMP2("assets/audio/sfx/jumppp22.wav"),
    BUTTON_CLICK("assets/audio/sfx/button_click.wav"),
    FOOTSTEPS("assets/audio/sfx/footstep_soft.wav"),
    FOOTSTEPS_RUN("assets/audio/sfx/footstep_run.wav"),
    PLAYER_DEATH("assets/audio/sfx/lose.wav"),
    GLASSBREAK("assets/audio/sfx/GlassBreaking.mp3"),
    VICTORY("assets/audio/sfx/VICTORY.wav"),
    WARSOUNDS("assets/audio/sfx/warSounds.mp3");

    private final String path;

    /**
     * Creates a sound effect entry with the given asset path.
     *
     * @param path The file path to the sound effect.
     */
    Sfx(String path) {
        this.path = path;
    }


    /**
     * Returns the asset path for this sound effect.
     *
     * @return The file path of the sound effect.
     */
    public String path() {
        return path;
    }

    /**
     * Queues all sound effects for loading in the asset manager.
     *
     * @param manager The asset manager used to load sound assets.
     */
    public static void queueAssets(AssetManager manager) {
        for (Sfx s : Sfx.values()) {
            manager.load(s.path(), Sound.class);
        }
    }
}

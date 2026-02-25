package inf112.zoombiegeddon.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import java.util.HashMap;

/**
 * Manages the loading, playback, and resource handling of game audio.
 * It provides separate handling for short sound effects (SFX) and looping background music.
 */
public class AudioManager {
    private HashMap<String, Sound> sounds;
    private Music backgroundMusic;
    private float soundVolume = 1.0f;
    private float musicVolume = 0.5f;

    /**
     * Constructs a new AudioManager and initializes the storage for sound effects.
     */
    public AudioManager() {
        sounds = new HashMap<>();
    }

    /**
     * Loads a sound effect from the internal assets folder and maps it to a reference name.
     *
     * @param name     The unique reference name used to play the sound later.
     * @param fileName The name of the file located in "assets/audio/sfx/".
     */
    public void loadSound(String name, String fileName) {
        Sound sound = Gdx.audio.newSound(Gdx.files.internal("assets/audio/sfx/" + fileName));
        sounds.put(name, sound);
    }

    /**
     * Plays a previously loaded sound effect at the current global sound volume.
     *
     * @param name The reference name of the sound to be played.
     */
    public void playSound(String name) {
        if (sounds.containsKey(name)) {
            sounds.get(name).play(soundVolume);
        }
    }

    /**
     * Loads and immediately starts playing background music with looping enabled.
     * If music is already playing, it will be disposed of before the new track starts.
     *
     * @param fileName The name of the file located in "assets/audio/music/".
     */
    public void playMusic(String fileName) {
        if (backgroundMusic != null) backgroundMusic.dispose();
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("assets/audio/music/" + fileName));
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(musicVolume);
        backgroundMusic.play();
    }

    /**
     * Updates the global volume levels for both sound effects and background music.
     *
     * @param newVolume The new volume level (normalized between 0.0 and 1.0).
     */
    public void setVolume(float newVolume) {
        this.soundVolume = newVolume;
        if (backgroundMusic != null) backgroundMusic.setVolume(soundVolume * musicVolume);
    }

    /**
     * Disposes of all loaded sound effects and background music to free up memory.
     * Should be called when the game or the audio manager is no longer needed.
     */
    public void dispose() {
        for (Sound s : sounds.values()) s.dispose();
        if (backgroundMusic != null) backgroundMusic.dispose();
    }


}

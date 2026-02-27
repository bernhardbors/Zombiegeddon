package inf112.zoombiegeddon.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import java.util.Random;

/**
 * Manages audio playback and resource handling.
 * This class relies on an {@link AssetManager} to provide access to loaded sound effects
 * and manages individual playback logic, volume control, and music streaming.
 */
public class AudioManager {
    private final AssetManager assetManager;
    private final Random random = new Random();

    private Music backgroundMusic;
    private float soundVolume = 1.0f;
    private float musicVolume = 0.5f;

    /**
     * Constructs a new AudioManager.
     * @param assetManager The central manager responsible for asset lifecycle.
     */
    public AudioManager(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    /**
     * Plays a sound effect using the {@link GameSound} registry key.
     * The sound is fetched directly from the asset manager.
     *
     * @param sound The GameSound constant to play.
     */
    public void playSound(GameSound sound) {
        assetManager.get("assets/audio/sfx/" + sound.getFileName(), Sound.class).play(soundVolume);
    }

    /**
     * Plays a random sound from a provided list of {@link GameSound} constants.
     * * @param sounds An array of GameSound constants to choose from.
     */
    public void playRandomSound(GameSound[] sounds) {
        GameSound randomSound = sounds[random.nextInt(sounds.length)];
        playSound(randomSound);
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
     * Disposes of the background music. Note: AssetManager handles Sound disposal.
     */
    public void dispose() {
        if (backgroundMusic != null) backgroundMusic.dispose();
    }

}

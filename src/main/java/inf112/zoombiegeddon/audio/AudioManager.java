package inf112.zoombiegeddon.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import java.util.HashMap;

public class AudioManager {
    private HashMap<String, Sound> sounds;
    private Music backgroundMusic;
    private float soundVolume = 1.0f;
    private float musicVolume = 0.5f;

    public AudioManager() {
        sounds = new HashMap<>();
    }

    public void loadSound(String name, String fileName) {
        Sound sound = Gdx.audio.newSound(Gdx.files.internal("assets/audio/sfx/" + fileName));
        sounds.put(name, sound);
    }

    public void playSound(String name) {
        if (sounds.containsKey(name)) {
            sounds.get(name).play(soundVolume);
        }
    }

    public void playMusic(String fileName) {
        if (backgroundMusic != null) backgroundMusic.dispose();
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("assets/audio/music/" + fileName));
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(musicVolume);
        backgroundMusic.play();
    }

    public void setVolume(float newVolume) {
        this.soundVolume = newVolume;
        if (backgroundMusic != null) backgroundMusic.setVolume(soundVolume * musicVolume);
    }

    public void dispose() {
        for (Sound s : sounds.values()) s.dispose();
        if (backgroundMusic != null) backgroundMusic.dispose();
    }


}

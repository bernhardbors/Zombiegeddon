package inf112.zoombiegeddon.audio;

import com.badlogic.gdx.audio.Sound;

public class AudioAssets {
    private AudioAssets(){}

    public static void queue(com.badlogic.gdx.assets.AssetManager manager) {
        for (Sfx s : Sfx.values()) {
            manager.load(s.path(), Sound.class);
        }
    }
}

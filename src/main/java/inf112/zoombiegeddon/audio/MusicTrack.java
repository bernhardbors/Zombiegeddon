package inf112.zoombiegeddon.audio;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;

public enum MusicTrack {
    MENU("assets/audio/music/02 - Title Theme.wav"),
    GAMEPLAY("assets/audio/music/04 - Silent forest.wav");

    private final String path;

    MusicTrack(String path) {
        this.path = path;
    }

    public String path() {
        return path;
    }

    public static void queueAssets(AssetManager manager) {
        for (MusicTrack t : MusicTrack.values()) {
            manager.load(t.path(), Music.class);
        }
    }
}
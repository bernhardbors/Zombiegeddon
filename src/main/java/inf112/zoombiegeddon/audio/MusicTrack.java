package inf112.zoombiegeddon.audio;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;

/**
 * Represents music tracks used in the game and their asset paths.
 */
public enum MusicTrack {
    MENU("assets/audio/Dario Durbé/song-2---african-theme-mp3-mjE9Xqj0vWI2rz2o.mp3"),
    GAMEPLAY("assets/audio/music/13 - Decisive Battle 1 - Don't Be Afraid.mp3"),
    STORY_CHILL_BACKGROUND("assets/audio/music/StoryB01.mp3");

    private final String path;

    /**
     * Creates a music track with the given asset path.
     *
     * @param path The file path to the music asset.
     */
    MusicTrack(String path) {
        this.path = path;
    }

    /**
     * Returns the asset path of the music track.
     *
     * @return The file path to the music asset.
     */
    public String path() {
        return path;
    }

    /**
     * Queues all music tracks for loading in the asset manager.
     *
     * @param manager The asset manager used to load music assets.
     */

    public static void queueAssets(AssetManager manager) {
        for (MusicTrack t : MusicTrack.values()) {
            manager.load(t.path(), Music.class);
        }
    }
}
package inf112.zoombiegeddon.audio;

import com.badlogic.gdx.audio.Sound;

/**
 * Utility class for queueing audio assets for loading.
 * Provides a centralized way to ensure all defined {@link Sfx} assets
 * are registered with the {@link com.badlogic.gdx.assets.AssetManager}.
 */
public class AudioAssets {
    private AudioAssets(){}

    /**
     * Adds all registered sound effects to the asset manager's loading queue.
     * @param manager The asset manager instance to queue resources into.
     */
    public static void queue(com.badlogic.gdx.assets.AssetManager manager) {
        for (Sfx s : Sfx.values()) {
            manager.load(s.path(), Sound.class);
        }
    }
}

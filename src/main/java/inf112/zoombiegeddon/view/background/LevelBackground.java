package inf112.zoombiegeddon.view.background;

import inf112.zoombiegeddon.app.AssetConstants;

/**
 * Defines the background layers for each game level.
 *
 * Each constant holds an ordered list of texture paths drawn back-to-front,
 * combined with {@link #FACTORS} to produce a parallax scrolling effect.
 */
public enum LevelBackground {
    /** City skyline background used in level 1. */
    LEVEL_1(AssetConstants.BG_SKY, AssetConstants.BG_CITY, AssetConstants.BG_L1, AssetConstants.BG_L2),

    /** Snowy mountain background used in level 2. */
    LEVEL_2(AssetConstants.BG_SKY, AssetConstants.BG_SNOW_3, AssetConstants.BG_SNOW_2, AssetConstants.BG_SNOW_1),

    /** Nature/forest background used in level 3. */
    LEVEL_3(AssetConstants.BG_SKY, AssetConstants.BG_NATURE_1, AssetConstants.BG_NATURE_2, AssetConstants.BG_NATURE_3);

    /**
     * Parallax movement factors for each layer, from back to front.
     * Lower values move slower and appear further away.
     */
    public static final float[] FACTORS = {0.1f, 0.3f, 0.3f, 0.3f};

    /** Ordered texture paths for this background, from back to front. */
    public final String[] layers;

    LevelBackground(String... layers) {
        this.layers = layers;
    }
}
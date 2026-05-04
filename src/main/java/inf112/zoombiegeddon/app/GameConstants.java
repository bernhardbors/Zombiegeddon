package inf112.zoombiegeddon.app;

/**
 * Shared gameplay constants used across model and view layers.
 */
public final class GameConstants {

    private GameConstants() {}

    /** Number of pixels per tile in the Tiled map. */
    public static final float PIXELS_PER_TILE = 8.0f;

    /** Scale factor for converting pixel coordinates to world units. */
    public static final float UNIT_SCALE = 1f / PIXELS_PER_TILE;
}
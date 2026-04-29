package inf112.zoombiegeddon.model.animations;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Interface for loading animation frames.
 * This abstraction allows {@link AnimationHandler} to be decoupled from the
 * LibGDX Texture loading logic, enabling easier unit testing with mock data.
 */
public interface IAnimationLoader {
    /**
     * Loads or generates a 2D array of texture regions.
     * * @param frameWidth  The width of each frame in pixels.
     * @param frameHeight The height of each frame in pixels.
     * @return A 2D array of {@link TextureRegion} where the first index
     * represents rows (animations) and the second index represents
     * columns (individual frames).
     */
    TextureRegion[][] loadFrames(int frameWidth, int frameHeight);
}

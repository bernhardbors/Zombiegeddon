package inf112.zoombiegeddon.view.animations;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import inf112.zoombiegeddon.model.entity.EntityState;
import java.util.EnumMap;
import java.util.Map;

/**
 * Handles the loading and management of entity animations.
 * This class splits a spritesheet into specific animations and maps them to
 * {@link EntityState} keys. It also manages sprite flipping based on movement direction.
 */
public class AnimationHandler implements IAnimationHandler {

    private final Texture characterSpriteSheet;
    private final Map<EntityState, Animation<TextureRegion>> animations = new EnumMap<>(EntityState.class);

    /**
     * Constructs a new AnimationHandler using a standard LibGDX Texture.
     * This is the primary constructor used during normal gameplay.
     *
     * @param characterSpriteSheet The texture containing all character animation frames.
     * @param frameWidth           The width of each individual frame in pixels.
     * @param frameHeight          The height of each individual frame in pixels.
     */
    public AnimationHandler(Texture characterSpriteSheet, int frameWidth, int frameHeight) {
        this.characterSpriteSheet = characterSpriteSheet;
        createAnimationMap((w, h) -> TextureRegion.split(characterSpriteSheet, w, h), frameWidth, frameHeight);
    }

    /**
     * Constructs a new AnimationHandler using a custom loader.
     * This constructor is primarily used for testing purposes to inject mock frames
     * without requiring a full LibGDX graphics context.
     *
     * @param loader      An implementation of {@link IAnimationLoader} to provide animation frames.
     * @param frameWidth  The width of each individual frame in pixels.
     * @param frameHeight The height of each individual frame in pixels.
     */
    public AnimationHandler(IAnimationLoader loader, int frameWidth, int frameHeight) {
        this.characterSpriteSheet = null;
        createAnimationMap(loader, frameWidth, frameHeight);
    }

    /**
     * Retrieves the correct texture region (frame) based on the current state,
     * elapsed time, and orientation.
     *
     * @param state       The current state of the entity (e.g., IDLE, WALKING).
     * @param stateTime   The total time spent in the current state.
     * @param facingRight Whether the entity is currently facing right.
     * @return The specific TextureRegion for the current animation frame.
     */
    public TextureRegion getFrame(EntityState state, float stateTime, boolean facingRight) {
        boolean loop = !(state == EntityState.JUMPING || state == EntityState.FALLING);

        Animation<TextureRegion> animation = animations.get(state);

        if (animation == null) {
            animation = animations.get(EntityState.IDLE);
        }

        TextureRegion region = animation.getKeyFrame(stateTime, loop);

        if ((!facingRight && !region.isFlipX()) || (facingRight && region.isFlipX())) {
            region.flip(true, false);
        }

        return region;
    }

    /**
     * Splits the spritesheet and populates the animation map with sequences
     * for all entity states.
     */
    private void createAnimationMap(IAnimationLoader loader, int frameWidth, int frameHeight) {
        TextureRegion[][] characterFrames = loader.loadFrames(frameWidth, frameHeight);

        int rows = characterFrames.length;

        animations.put(EntityState.IDLE,     createAnimation(characterFrames[0], 3, 0.5f));
        if (rows > 1) animations.put(EntityState.WALKING,  createAnimation(characterFrames[1], 3, 0.06f));
        if (rows > 1) animations.put(EntityState.RUNNING,  createAnimation(characterFrames[1], 3, 0.06f));
        if (rows > 2) animations.put(EntityState.JUMPING,  createAnimation(characterFrames[2], 3, 0.05f));
        if (rows > 3) animations.put(EntityState.FALLING,  createAnimation(characterFrames[3], 3, 0.05f));
        if (rows > 4) animations.put(EntityState.PUNCHING, createAnimation(characterFrames[4], 3, 0.1f));
    }

    /**
     * Helper method to create a LibGDX Animation from a row of texture regions.
     *
     * @param row           The row of frames from the split spritesheet.
     * @param frameCount    The number of frames to include in the animation.
     * @param frameDuration How long each frame should be displayed in seconds.
     * @return A new Animation object containing the specified frames.
     */
    private Animation<TextureRegion> createAnimation(TextureRegion[] row, int frameCount, float frameDuration) {
        TextureRegion[] frames = new TextureRegion[frameCount];
        System.arraycopy(row, 0, frames, 0, frameCount);
        return new Animation<>(frameDuration, frames);
    }

    /**
     * Provides access to the full map of animations.
     *
     * @return A map where keys are EntityStates and values are their corresponding animations.
     */
    public Map<EntityState, Animation<TextureRegion>> getAnimations() {
        return animations;
    }

    public Texture getCharacterSpriteSheet() {
        return characterSpriteSheet;
    }
}
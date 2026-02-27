package inf112.zoombiegeddon.model.animations;

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
public class AnimationHandler {

    private final Texture characterSpriteSheet;
    private final Map<EntityState, Animation<TextureRegion>> playerAnimations = new EnumMap<>(EntityState.class);

    /**
     * Constructs a new AnimationHandler using a provided spritesheet.
     * Automatically triggers the creation of the animation map.
     *
     * @param characterSpriteSheet The texture containing all character animation frames.
     */
    public AnimationHandler(Texture characterSpriteSheet) {
        this.characterSpriteSheet = characterSpriteSheet;
        createPlayerAnimationMap();
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
        boolean loop = !(state == EntityState.PUNCHING || state == EntityState.DEAD);

        Animation<TextureRegion> animation = playerAnimations.get(state);

        if (animation == null) {
            animation = playerAnimations.get(EntityState.IDLE);
        }

        TextureRegion region = animation.getKeyFrame(stateTime, loop);

        // Logic for flipping the texture based on facing direction
        if ((facingRight && !region.isFlipX()) || (!facingRight && region.isFlipX())) {
            region.flip(true, false);
        }

        return region;
    }

    /**
     * Splits the spritesheet and populates the animation map with sequences
     * for all entity states.
     */
    private void createPlayerAnimationMap() {
        TextureRegion[][] characterFrames = TextureRegion.split(characterSpriteSheet, 80, 64);

        playerAnimations.put(EntityState.IDLE,     createAnimation(characterFrames[0], 4, 0.3f));
        playerAnimations.put(EntityState.WALKING,  createAnimation(characterFrames[1], 7, 0.06f));
        playerAnimations.put(EntityState.RUNNING,  createAnimation(characterFrames[2], 7, 0.06f));
        playerAnimations.put(EntityState.JUMPING,  createAnimation(characterFrames[3], 3, 0.1f));
        playerAnimations.put(EntityState.FALLING,  createAnimation(characterFrames[4], 3, 0.1f));
        playerAnimations.put(EntityState.PUNCHING, createAnimation(characterFrames[5], 5, 0.1f));
        playerAnimations.put(EntityState.DEAD,     createAnimation(characterFrames[6], 9, 0.1f));
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
    public Map<EntityState, Animation<TextureRegion>> getPlayerAnimations() {
        return playerAnimations;
    }
}
package inf112.zoombiegeddon.view.animations;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import inf112.zoombiegeddon.model.entity.EntityState;

import java.util.Map;

/**
 * Abstraction for animation frame retrieval and animation data access.
 * Decouples entity rendering from the concrete {@link AnimationHandler} implementation,
 * enabling test doubles that don't require a LibGDX graphics context.
 */
public interface IAnimationHandler {

    /**
     * Returns the texture region for the current animation frame.
     *
     * @param state       the current entity state
     * @param stateTime   time spent in the current state
     * @param facingRight whether the entity faces right
     * @return the frame to render
     */
    TextureRegion getFrame(EntityState state, float stateTime, boolean facingRight);

    /**
     * Returns the full animation map keyed by entity state.
     *
     * @return an unmodifiable view of all loaded animations
     */
    Map<EntityState, Animation<TextureRegion>> getAnimations();

    /**
     * Returns the underlying spritesheet texture, or {@code null} when constructed
     * with a custom {@link IAnimationLoader}.
     */
    Texture getCharacterSpriteSheet();
}

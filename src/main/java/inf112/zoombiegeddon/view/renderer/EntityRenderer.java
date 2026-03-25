package inf112.zoombiegeddon.view.renderer;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import inf112.zoombiegeddon.model.entity.IEntity;

/**
 * Renders entities using their current animation frame.
 */
public class EntityRenderer  {

    /**
     * Draws an entity at its current position using the given texture region.
     *
     * @param batch The sprite batch used for rendering.
     * @param entity The entity to render.
     * @param currentFrame The current animation frame of the entity.
     */
    public void render(SpriteBatch batch, IEntity entity, TextureRegion currentFrame) {

        float x = entity.getPosition().x - entity.getScaledSpriteDimensions() / 2f;
        float y = entity.getPosition().y - entity.getScaledSpriteDimensions() / 2f;

        batch.draw(currentFrame, x, y, entity.getScaledSpriteDimensions(), entity.getScaledSpriteDimensions());
    }
}

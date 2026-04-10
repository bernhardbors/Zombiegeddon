package inf112.zoombiegeddon.view.renderer;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import inf112.zoombiegeddon.app.GameApp;
import inf112.zoombiegeddon.model.entity.IEntity;
import inf112.zoombiegeddon.model.entity.Player;

/**
 * Renders entities using their current animation frame.
 */
public class EntityRenderer  {

    private final GameApp app;

    public EntityRenderer(GameApp app) {
        this.app = app;
    }

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

        Player playerEntity = entity instanceof Player ? (Player) entity : null;
        boolean usePickupShader = playerEntity != null
                && playerEntity.hasPickupVisualEffect()
                && app.pickupEffectShader != null;

        if (usePickupShader) {
            batch.setShader(app.pickupEffectShader);
            app.pickupEffectShader.setUniformf("u_time", playerEntity.getStateTime());
            app.pickupEffectShader.setUniformf("u_intensity", playerEntity.getPickupFxIntensity());
            app.pickupEffectShader.setUniformf(
                    "u_pickupColor",
                    playerEntity.getPickupFxColorR(),
                    playerEntity.getPickupFxColorG(),
                    playerEntity.getPickupFxColorB()
            );
        }

        batch.draw(currentFrame, x, y, entity.getScaledSpriteDimensions(), entity.getScaledSpriteDimensions());

        if (usePickupShader) {
            batch.setShader(null);
        }
    }
}

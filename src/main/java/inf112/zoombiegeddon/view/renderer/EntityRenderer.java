package inf112.zoombiegeddon.view.renderer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import inf112.zoombiegeddon.app.GameApp;
import inf112.zoombiegeddon.model.entity.IEntity;
import inf112.zoombiegeddon.model.entity.player.IPlayerVisuals;
import inf112.zoombiegeddon.view.animations.AnimationHandler;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Renders entities using their current animation frame.
 * Owns the mapping between entities and their AnimationHandlers,
 * sprite dimensions, and active pickup effect colors.
 * All animation and visual resources are kept in the View layer. */
public class EntityRenderer  {

    private static final Color PICKUP_COLOR_SPEED = Color.YELLOW;
    private static final Color PICKUP_COLOR_HEAL  = Color.RED;
    private static final Color PICKUP_COLOR_DEFAULT = Color.WHITE;

    private final GameApp app;
    private final Map<IEntity, AnimationHandler> animationHandlers = new LinkedHashMap<>();
    private final Map<IEntity, Float> spriteDimensions = new LinkedHashMap<>();
    private final Map<IEntity, Color> pickupEffectColors = new LinkedHashMap<>();

    public EntityRenderer(GameApp app) {
        this.app = app;
    }

    /**
     * Registers an entity with its animation handler and sprite size.
     * Call this when an entity is created, before the first render.
     *
     * @param entity          The entity to register.
     * @param animationHandler The animation handler for this entity.
     * @param spriteDim       The scaled sprite dimension (width and height) in world units.
     */
    public void register(IEntity entity, AnimationHandler animationHandler, float spriteDim) {
        animationHandlers.put(entity, animationHandler);
        spriteDimensions.put(entity, spriteDim);
    }

    /**
     * Unregisters an entity when it is removed from the world.
     *
     * @param entity The entity to unregister.
     */
    public void unregister(IEntity entity) {
        animationHandlers.remove(entity);
        spriteDimensions.remove(entity);
        pickupEffectColors.remove(entity);
    }

    /**
     * Sets the color to use for the pickup shader effect on the given entity.
     * The color is chosen based on the pickup type string from {@link inf112.zoombiegeddon.model.entity.pickups.IPickup#getPickupType()}.
     *
     * @param entity     The entity to apply the color to.
     * @param pickupType The pickup type string, e.g. "speed" or "heal".
     */
    public void setPickupEffectColor(IEntity entity, String pickupType) {
        Color color = switch (pickupType) {
            case "speed" -> PICKUP_COLOR_SPEED;
            case "heal"  -> PICKUP_COLOR_HEAL;
            default      -> PICKUP_COLOR_DEFAULT;
        };
        pickupEffectColors.put(entity, color);
    }

    private float victoryTime = -1f;

    public void setVictoryTime(float t) {
        this.victoryTime = t;
    }

    /**
     * Renders all registered entities in registration order.
     *
     * @param batch The sprite batch used for rendering.
     */
    public void renderAll(SpriteBatch batch) {
        for (Map.Entry<IEntity, AnimationHandler> entry : animationHandlers.entrySet()) {
            IEntity entity = entry.getKey();
            AnimationHandler handler = entry.getValue();
            float spriteDim = spriteDimensions.get(entity);

            boolean facingRight = entity.isFacingRight();
            if (victoryTime >= 0 && entity instanceof IPlayerVisuals) {
                facingRight = (int)(victoryTime * 8) % 2 == 0;
            }

            TextureRegion frame = handler.getFrame(
                    entity.getCurrentState(),
                    entity.getStateTime(),
                    facingRight
            );
            render(batch, entity, frame, spriteDim);
        }
    }

    private void render(SpriteBatch batch, IEntity entity, TextureRegion frame, float spriteDim) {
        float x = entity.getPosition().x - spriteDim / 2f;
        float y = entity.getPosition().y - spriteDim / 2f;

        ShaderProgram shader = app.getAssets().getPickupEffectShader();

        if (entity instanceof IPlayerVisuals playerVisuals
                && playerVisuals.hasPickupVisualEffect()
                && shader != null) {
            Color c = pickupEffectColors.getOrDefault(entity, PICKUP_COLOR_DEFAULT);
            batch.setShader(shader);
            shader.setUniformf("u_time", entity.getStateTime());
            shader.setUniformf("u_intensity", playerVisuals.getPickupFxIntensity());
            shader.setUniformf("u_pickupColor", c.r, c.g, c.b);
            batch.draw(frame, x, y, spriteDim, spriteDim);
            batch.setShader(null);
            return;
        }

        batch.draw(frame, x, y, spriteDim, spriteDim);
    }

    /**
     * Removes any registered entities that are not present in the given active list.
     * Call once per frame to keep the renderer in sync with the model.
     *
     * @param activeEntities The current list of active entities from the model.
     */
    public void syncWithModel(List<IEntity> activeEntities) {
        List<IEntity> toRemove = new ArrayList<>();
        for (IEntity entity : animationHandlers.keySet()) {
            if (!activeEntities.contains(entity)) {
                toRemove.add(entity);
            }
        }
        toRemove.forEach(this::unregister);
    }
}

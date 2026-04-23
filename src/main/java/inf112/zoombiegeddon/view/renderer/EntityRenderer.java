package inf112.zoombiegeddon.view.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import inf112.zoombiegeddon.app.AssetConstants;
import inf112.zoombiegeddon.app.GameApp;
import inf112.zoombiegeddon.model.entity.Entity;
import inf112.zoombiegeddon.model.entity.IEntity;
import inf112.zoombiegeddon.model.entity.Player;
import inf112.zoombiegeddon.model.entity.enemies.Enemy;
import inf112.zoombiegeddon.model.entity.pickups.IPickup;
import inf112.zoombiegeddon.model.levels.LevelModel;
import inf112.zoombiegeddon.model.world.luckyblock.LuckyBlockLayerFactory;
import inf112.zoombiegeddon.utils.CameraUtils;

import java.util.List;

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
        ShaderProgram shader = app.getAssets().getPickupEffectShader();

        boolean usePickupShader = playerEntity != null
                && playerEntity.hasPickupVisualEffect()
                && shader != null;

        if (usePickupShader) {
            batch.setShader(shader);
            shader.setUniformf("u_time", playerEntity.getStateTime());
            shader.setUniformf("u_intensity", playerEntity.getPickupFxIntensity());
            shader.setUniformf(
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

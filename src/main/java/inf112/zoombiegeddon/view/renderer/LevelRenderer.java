package inf112.zoombiegeddon.view.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import inf112.zoombiegeddon.app.AssetConstants;
import inf112.zoombiegeddon.app.GameApp;
import inf112.zoombiegeddon.model.entity.IEntity;
import inf112.zoombiegeddon.model.entity.pickups.IPickup;
import inf112.zoombiegeddon.model.levels.ILevelModel;
import inf112.zoombiegeddon.model.levels.LevelModel;
import inf112.zoombiegeddon.view.utils.CameraUtils;

import java.util.List;

/**
 * Renders a complete game level, including background, world tiles, entities,
 * pickups, UI, and trigger text.
 */
public class LevelRenderer {

    private GameApp app;
    private ILevelModel model;

    private OrthographicCamera camera;
    private Viewport viewport;
    private WorldRenderer worldRenderer;
    private EntityRenderer entityRenderer;
    private UIRenderer uiRenderer;
    private Box2DDebugRenderer debugRenderer;
    private LuckyBlockRenderer luckyBlockRenderer;

    private Texture healthbar;
    private boolean showDebug;

    public static final float VIEWPORT_WIDTH = 50.0f;
    public static final float VIEWPORT_HEIGHT = 30.0f;

    private static final float SIGN_BOX_WIDTH = 0.6F;
    private static final float SIGN_PADDING = 0.02f;
    private static final float SIGN_FONT_SCALE = 2.0f;
    private static final float SIGN_Y_POSITION = 0.75f;

    /**
     * @param app   the application context, used to access assets and batch
     * @param model the level model to render
     * @param map   the Tiled map used to set up the world renderer
     */
    public LevelRenderer(GameApp app, ILevelModel model, TiledMap map) {
        this.app = app;
        this.model = model;

        this.camera = new OrthographicCamera();
        app.getBackground().setCamera(camera);
        this.viewport = new ExtendViewport(VIEWPORT_WIDTH, VIEWPORT_HEIGHT, camera);

        this.worldRenderer = new WorldRenderer(camera);
        this.worldRenderer.setMap(map, LevelModel.UNIT_SCALE);

        this.entityRenderer = new EntityRenderer(app);
        this.uiRenderer = new UIRenderer(camera, model.getPlayer());
        this.debugRenderer = new Box2DDebugRenderer();
        this.healthbar = app.getAssets().getManager().get(AssetConstants.HEALTH_BAR, Texture.class);

        this.luckyBlockRenderer = new LuckyBlockRenderer(model.getLuckyBlockFactory());
    }

    /** Renders one frame of the level in draw order. */
    public void render() {
        if (model.getPlayer() != null) {
            CameraUtils.updateCamera(model.getPlayer().getPosition(), camera);
        }

        viewport.apply();
        Batch batch = app.getBatch();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        app.getBackground().render(batch);
        batch.end();

        worldRenderer.renderBackground();

        if (luckyBlockRenderer != null) {
            batch.setProjectionMatrix(camera.combined);
            batch.begin();
            luckyBlockRenderer.render(batch, LevelModel.UNIT_SCALE);
            batch.end();
        }

        worldRenderer.renderDebug(model.getPhysics(), showDebug);

        for (IEntity entity : model.getAllEntities()) {
            drawEntities(entity);
        }

        drawPickups();

        worldRenderer.renderForeground();

        if (uiRenderer != null) {
            uiRenderer.drawHearts(batch, healthbar);
            uiRenderer.drawStoryTime(batch, app.getAssets().getFont(), getDisplayedTime());
        }

        renderTriggerText();
        if (showDebug) {
            debugRenderer.render(model.getPhysics().getWorld(), camera.combined);
        }
    }

    private void drawEntities(IEntity entity) {
        if (entity.getAnimationHandler() == null) return;
        TextureRegion playerFrame = entity.getAnimationHandler().getFrame(entity.getCurrentState(), entity.getStateTime(), entity.isFacingRight());
        app.getBatch().setProjectionMatrix(camera.combined);
        app.getBatch().begin();
        entityRenderer.render(app.getBatch(), entity, playerFrame);
        app.getBatch().end();
    }

    private void drawPickups() {
        List<IPickup> pickups = model.getPhysics().getPickups();
        if (pickups.isEmpty()) return;

        Batch batch = app.getBatch();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        Texture speedTex = app.getAssets().getManager().get(AssetConstants.PICKUP_SPEED, Texture.class);
        Texture healTex  = app.getAssets().getManager().get(AssetConstants.PICKUP_HEAL,  Texture.class);

        for (IPickup pickup : pickups) {
            if (pickup.isCollected()) continue;
            float s = pickup.getRenderSize();
            float x = pickup.getPosition().x - s / 2f;
            float y = pickup.getPosition().y - s / 2f;
            Texture tex = "speed".equals(pickup.getPickupType()) ? speedTex : healTex;
            batch.draw(tex, x, y, s, s);
        }
        batch.end();
    }

    private void renderTriggerText() {
        String text = model.getCurrentText();
        if (text != null && model.getTriggerZone() != null && model.getTriggerZone().getZone() != null) {
            drawSignText(text);
        }
    }

    private void drawSignText(String text) {
        int screenW = Gdx.graphics.getWidth();
        int screenH = Gdx.graphics.getHeight();
        Matrix4 screenMatrix = new Matrix4().setToOrtho2D(0, 0, screenW, screenH);

        float boxW = SIGN_BOX_WIDTH * screenW;
        float padding = SIGN_PADDING * screenH;

        app.getAssets().getFont().getData().setScale(SIGN_FONT_SCALE);
        GlyphLayout layout = new GlyphLayout();
        layout.setText(app.getAssets().getFont(), text, Color.WHITE, boxW - padding * 2, Align.center, true);

        float boxX = (screenW - boxW) / 2f;
        float boxY = screenH * SIGN_Y_POSITION;

        app.getBatch().setProjectionMatrix(screenMatrix);
        app.getBatch().begin();
        app.getAssets().getFont().draw(app.getBatch(), layout, boxX + padding, boxY);
        app.getBatch().end();

        app.getAssets().getFont().getData().setScale(1f);
    }

    private float getDisplayedTime() {
        if (app.getStoryManager().isActive()) {
            return app.getStoryManager().getElapsedSeconds(model.getLevelTime());
        }

        return model.getLevelTime();
    }

    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    /** Releases native resources held by the world and debug renderers. */
    public void dispose() {
        worldRenderer.dispose();
        debugRenderer.dispose();
    }
}
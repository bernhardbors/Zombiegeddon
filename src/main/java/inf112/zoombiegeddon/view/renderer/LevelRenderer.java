package inf112.zoombiegeddon.view.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
import inf112.zoombiegeddon.model.levels.LevelModel;
import inf112.zoombiegeddon.utils.CameraUtils;

import java.util.List;

public class LevelRenderer {

    private GameApp app;
    private LevelModel model;

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

    public LevelRenderer(GameApp app, LevelModel model, TiledMap map) {
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
        this.healthbar = app.getAssets().getManager().get(AssetConstants.HEALTHBAR, Texture.class);

        this.luckyBlockRenderer = new LuckyBlockRenderer(model.getLuckyBlockFactory());
    }

    public void render() {
        if (model.getPlayer() != null) {
            CameraUtils.updateCamera(model.getPlayer(), camera);
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
            if (app.getStoryManager().isActive()) {
                float timeUsed = app.getStoryManager().getElapsedSeconds(model.getLevelTime());
                uiRenderer.drawStoryTime(batch, app.getAssets().getFont(), timeUsed);
            }
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

        app.getShapeRenderer().setProjectionMatrix(camera.combined);
        app.getShapeRenderer().begin(ShapeRenderer.ShapeType.Filled);
        for (IPickup pickup : pickups) {
            if (pickup.isCollected()) continue;

            float size = pickup.getRenderSize();
            float x = pickup.getPosition().x - size / 2f;
            float y = pickup.getPosition().y - size / 2f;
            app.getShapeRenderer().setColor(pickup.getPickupColor());
            app.getShapeRenderer().rect(x, y, size, size);
        }
        app.getShapeRenderer().end();
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

    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    public void dispose() {
        worldRenderer.dispose();
        debugRenderer.dispose();
    }
}
package inf112.zoombiegeddon.view.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import inf112.zoombiegeddon.app.GameApp;

import inf112.zoombiegeddon.audio.IAudioService;
import inf112.zoombiegeddon.controller.PlayerController;
import inf112.zoombiegeddon.model.entity.Entity;
import inf112.zoombiegeddon.model.entity.Player;
import inf112.zoombiegeddon.model.world.physics.WorldPhysics;
import inf112.zoombiegeddon.model.world.triggers.TriggerZone;
import inf112.zoombiegeddon.view.renderer.EntityRenderer;
import inf112.zoombiegeddon.view.renderer.UIRenderer;
import inf112.zoombiegeddon.view.renderer.WorldRenderer;
import inf112.zoombiegeddon.view.screens.GameOverScreen;

/**
 * Abstract base class for all game levels.
 * Provides common functionality for physics simulation, world rendering,
 * camera management, and map loading within the LibGDX framework.
 */
public abstract class AbstractLevel extends ScreenAdapter implements ILevel {

    protected GameApp app;
    protected OrthographicCamera camera;
    protected Viewport viewport;
    protected IAudioService audioService;
    protected PlayerController playerController;
    protected WorldRenderer worldRenderer;
    protected WorldPhysics physics;
    protected EntityRenderer entityRenderer;
    protected UIRenderer UIrenderer;
    private ShapeRenderer hudRenderer;
    protected Box2DDebugRenderer debugRenderer;
    protected Player player;
    protected TriggerZone triggerZone;
    protected String currentText = null;

    public static final float PIXELS_PER_TILE = 8.0f;
    public static final float UNIT_SCALE = 1 / PIXELS_PER_TILE;
    public static final float viewportWidth = 50.0f;
    public static final float viewportHeight = 30.0f;

    public AbstractLevel(GameApp app, OrthographicCamera camera, Vector2 gravity) {
        this.physics = new WorldPhysics(gravity);
        this.worldRenderer = new WorldRenderer(camera);
        this.audioService = app.audio;
        this.viewport = new ExtendViewport(viewportWidth, viewportHeight, camera);
        this.camera = camera;
        this.app = app;
        this.hudRenderer = new ShapeRenderer();
        this.debugRenderer = new Box2DDebugRenderer();
    }

    @Override
    public void show() {
        app.background.setCamera(camera);
        if (playerController != null) {
            Gdx.input.setInputProcessor(playerController);
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        viewport.apply();
        if (playerController != null) {
            playerController.update(delta);
        }
        update(delta);
        draw(app.batch);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }


    @Override
    public void update(float delta) {
        physics.update(delta);

        if (player != null) {
            player.update(delta);
            if (player.isDead()) {
                app.setScreen(new GameOverScreen(app));
                return;
            }
            inf112.zoombiegeddon.utils.CameraUtils.updateCamera(player, camera);

            if (triggerZone != null) {
                Rectangle playerBounds = new Rectangle(
                        player.getPosition().x - player.getWidth() / 2f,
                        player.getPosition().y - player.getHeight() / 2f,
                        player.getWidth(),
                        player.getHeight()
                );
                currentText = triggerZone.update(playerBounds, player);
            }
        }
    }

    public void drawEntities(Entity entity) {
        TextureRegion playerFrame = entity.getAnimationHandler().getFrame(entity.getCurrentState(), entity.getStateTime(), entity.isFacingRight());
        app.batch.setProjectionMatrix(camera.combined);
        app.batch.begin();
        entityRenderer.render(app.batch, entity, playerFrame);
        app.batch.end();
    }

    @Override
    public void drawParallaxBackground() {
        app.batch.setProjectionMatrix(camera.combined);
        app.batch.begin();
        app.background.render(app.batch);
        app.batch.end();
    }

    @Override
    public Label drawText(String text, Color color, float scale) {
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = app.font;
        style.fontColor = color;

        style.font.setUseIntegerPositions(false);
        Label textBody = new Label(text, style);
        textBody.setFontScale(scale);

        return textBody;
    }

    public void drawSignText(String text, Rectangle signRect) {
        int screenW = Gdx.graphics.getWidth();
        int screenH = Gdx.graphics.getHeight();
        Matrix4 screenMatrix = new Matrix4().setToOrtho2D(0, 0, screenW, screenH);

        float boxW = screenW * 0.6f;
        float padding = 16f;
        float fontScale = 2.0f;

        app.font.getData().setScale(fontScale);
        GlyphLayout layout = new GlyphLayout();
        layout.setText(app.font, text, Color.WHITE, boxW - padding * 2, Align.center, true);

        float boxH = layout.height + padding * 2;

        // Project the top-centre of the sign zone from world space to screen space
        Vector3 signTop = new Vector3(
                signRect.x + signRect.width / 2f,
                signRect.y + signRect.height,
                0);
        camera.project(signTop);

        float boxX = signTop.x - boxW / 2f;
        float boxY = signTop.y + 8f;

        // Clamp so the panel stays within the screen
        boxX = Math.max(0, Math.min(boxX, screenW - boxW));
        boxY = Math.min(boxY, screenH - boxH);

        // Draw wrapped text centred in the panel
        app.batch.setProjectionMatrix(screenMatrix);
        app.batch.begin();
        app.font.draw(app.batch, layout, boxX + padding, boxY + boxH - padding);
        app.batch.end();

        // Restore font scale for other uses
        app.font.getData().setScale(1f);
    }

    public void drawDebug() {
        debugRenderer.render(physics.getWorld(), camera.combined);
    }
    public void renderTriggerText() {
        if (currentText != null && triggerZone != null && triggerZone.getZone() != null) {
            drawSignText(currentText, triggerZone.getZone());
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        worldRenderer.dispose();
        audioService.dispose();
        hudRenderer.dispose();
        debugRenderer.dispose();
    }
}
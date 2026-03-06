package inf112.zoombiegeddon.view.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import inf112.zoombiegeddon.app.GameApp;

import inf112.zoombiegeddon.audio.AudioService;
import inf112.zoombiegeddon.controller.PlayerController;
import inf112.zoombiegeddon.model.entity.Player;
import inf112.zoombiegeddon.model.world.physics.WorldCollisionLayerCreator;
import inf112.zoombiegeddon.model.world.physics.WorldPhysics;
import inf112.zoombiegeddon.view.renderer.WorldRenderer;

import static com.badlogic.gdx.Gdx.app;
import static com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable.draw;

/**
 * Abstract base class for all game levels.
 * Provides common functionality for physics simulation, world rendering,
 * camera management, and map loading within the LibGDX framework.
 */
public abstract class AbstractLevel extends ScreenAdapter implements ILevel {

    protected GameApp app;
    protected OrthographicCamera camera;
    protected Viewport viewport;
    protected AudioService audioService;
    protected PlayerController playerController;
    protected WorldRenderer worldRenderer;
    protected WorldPhysics physics;

    public AbstractLevel( Viewport viewport, OrthographicCamera camera, GameApp app, Vector2 gravity) {
        this.physics = new WorldPhysics(gravity);
        this.worldRenderer = new WorldRenderer(camera);
        this.audioService = app.audio;
        this.viewport = viewport;
        this.camera = camera;
        this.app = app;
    }


    @Override
    public void update(float delta) {
        physics.update(delta);
    }

    @Override
    public void drawEntities() {
        app.batch.begin();
        draw(app.batch);
        app.batch.end();
    }

    @Override
    public void drawParallaxBackground() {
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

    @Override
    public void dispose() {
        worldRenderer.dispose();
        audioService.dispose();
    }
}
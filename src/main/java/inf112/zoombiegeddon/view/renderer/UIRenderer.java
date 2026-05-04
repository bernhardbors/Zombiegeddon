package inf112.zoombiegeddon.view.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import inf112.zoombiegeddon.model.entity.IDamageable;
import inf112.zoombiegeddon.model.entity.IEntity;

import java.util.Locale;

/**
 * Renders UI elements related to an entity, such as health hearts.
 */
public class UIRenderer {

    private static final float HEARTS_X_OFFSET_RATIO = 0.02f;
    private static final float HEARTS_Y_OFFSET_RATIO = 0.92f;
    private static final float TIMER_EDGE_PADDING_RATIO = 0.02f;
    private static final float TIMER_SHADOW_OFFSET_RATIO = 0.002f;
    private static final float TIMER_SHADOW_ALPHA = 0.75f;
    private static final float TIMER_REFERENCE_HEIGHT = 720f;
    private static final float TIMER_SCALE_MULTIPLIER = 1.4f;
    private static final int HEALTH_TEXTURE_FRAMES = 7;
    private static final int HEALTH_FRAME_ROW_MULTIPLIER = 2;

    private final ShapeRenderer shapeRenderer;
    private final IDamageable damageable;

    /**
     * Creates a UI renderer for an entity that can take damage.
     *
     * @param camera The camera associated with the current view.
     * @param entity The entity whose UI information should be rendered.
     * @param <T> The entity type, which must extend {@link IEntity} and implement {@link IDamageable}.
     */
    public <T extends IEntity & IDamageable> UIRenderer(OrthographicCamera camera, T entity) {
        this.shapeRenderer = new ShapeRenderer();
        this.damageable = entity;


    }

    /**
     * Draws the health hearts for the tracked entity.
     *
     * @param batch The sprite batch used for rendering.
     * @param texture The texture containing the heart UI frames.
     */
    public void drawHearts(Batch batch, Texture texture, float scale) {
        Matrix4 uiMatrix = new Matrix4().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.setProjectionMatrix(uiMatrix);

        batch.begin();
        float x_pos = Gdx.graphics.getWidth() * HEARTS_X_OFFSET_RATIO;
        float y_pos = Gdx.graphics.getHeight() * HEARTS_Y_OFFSET_RATIO;
        int currentHealth = damageable.getHealth();
        int row = (3 - currentHealth) * HEALTH_FRAME_ROW_MULTIPLIER;
        int frameHeight = texture.getHeight() / HEALTH_TEXTURE_FRAMES;
        int srcY = row * frameHeight;

        TextureRegion currentFrame = new TextureRegion(texture, 0, srcY, texture.getWidth(), frameHeight);

        batch.draw(currentFrame, x_pos, y_pos, (float) texture.getWidth() * scale, (float) frameHeight * scale);

        batch.end();
    }

    /**
     * Draws the accumulated story time in the top-right corner of the HUD.
     *
     * @param batch The sprite batch used for rendering.
     * @param font The bitmap font used for the timer label.
     * @param storyTimeSeconds The elapsed story time to display.
     */
    public void drawTime(Batch batch, BitmapFont font, float storyTimeSeconds) {
        Matrix4 uiMatrix = new Matrix4().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.setProjectionMatrix(uiMatrix);

        batch.begin();

        String timerText = formatStoryTime(storyTimeSeconds);
        float screenScale = (Gdx.graphics.getHeight() / TIMER_REFERENCE_HEIGHT) * TIMER_SCALE_MULTIPLIER;
        font.getData().setScale(screenScale);
        GlyphLayout layout = new GlyphLayout(font, timerText);
        float edgePadding = Gdx.graphics.getHeight() * TIMER_EDGE_PADDING_RATIO;
        float shadowOffset = Gdx.graphics.getHeight() * TIMER_SHADOW_OFFSET_RATIO;
        float x = Gdx.graphics.getWidth() - layout.width - edgePadding;
        float y = Gdx.graphics.getHeight() - edgePadding;

        Color previousColor = font.getColor().cpy();
        font.setColor(0f, 0f, 0f, TIMER_SHADOW_ALPHA);
        font.draw(batch, timerText, x + shadowOffset, y - shadowOffset);
        font.setColor(Color.WHITE);
        font.draw(batch, timerText, x, y);
        font.setColor(previousColor);
        font.getData().setScale(1f);

        batch.end();
    }

    private String formatStoryTime(float storyTimeSeconds) {
        int totalSeconds = Math.max(0, (int) storyTimeSeconds);
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        int tenths = (int) ((storyTimeSeconds - totalSeconds) * 10f);

        return String.format(Locale.ROOT, "%02d:%02d.%d", minutes, seconds, tenths);
    }

    /**
     * Releases resources used by the UI renderer.
     */
    public void dispose() {
        shapeRenderer.dispose();
    }
}
package inf112.zoombiegeddon.view.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import inf112.zoombiegeddon.model.entity.Entity;
import inf112.zoombiegeddon.model.entity.IDamageable;

import java.util.Locale;

/**
 * Renders UI elements related to an entity, such as health hearts.
 */
public class UIRenderer {

    private final ShapeRenderer shapeRenderer;
    private final IDamageable damageable;
    private int row;

    /**
     * Creates a UI renderer for an entity that can take damage.
     *
     * @param camera The camera associated with the current view.
     * @param entity The entity whose UI information should be rendered.
     * @param <T> The entity type, which must extend {@link Entity} and implement {@link IDamageable}.
     */
    public <T extends Entity & IDamageable> UIRenderer(OrthographicCamera camera, T entity) {
        this.shapeRenderer = new ShapeRenderer();
        this.damageable = entity;


    }

    /**
     * Draws the health hearts for the tracked entity.
     *
     * @param batch The sprite batch used for rendering.
     * @param texture The texture containing the heart UI frames.
     */
    public void drawHearts(SpriteBatch batch, Texture texture) {
        Matrix4 uiMatrix = new Matrix4().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.setProjectionMatrix(uiMatrix);

        batch.begin();

        int currentHealth = (int) damageable.getHealth();

        this.row= (3 - currentHealth) *2;

        int frameHeight = texture.getHeight() / 7;
        int srcY = row * frameHeight;

        TextureRegion currentFrame = new TextureRegion(texture, 0, srcY, texture.getWidth(), frameHeight);

        batch.draw(currentFrame, 20, Gdx.graphics.getHeight() - 60, texture.getWidth() * 4, frameHeight * 4);

        batch.end();
    }

    /**
     * Draws the accumulated story time in the top-right corner of the HUD.
     *
     * @param batch The sprite batch used for rendering.
     * @param font The bitmap font used for the timer label.
     * @param storyTimeSeconds The elapsed story time to display.
     */
    public void drawStoryTime(SpriteBatch batch, BitmapFont font, float storyTimeSeconds) {
        Matrix4 uiMatrix = new Matrix4().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.setProjectionMatrix(uiMatrix);

        batch.begin();

        String timerText = formatStoryTime(storyTimeSeconds);
        GlyphLayout layout = new GlyphLayout(font, timerText);
        float x = Gdx.graphics.getWidth() - layout.width - 20f;
        float y = Gdx.graphics.getHeight() - 20f;

        Color previousColor = font.getColor().cpy();
        font.setColor(0f, 0f, 0f, 0.75f);
        font.draw(batch, timerText, x + 2f, y - 2f);
        font.setColor(Color.WHITE);
        font.draw(batch, timerText, x, y);
        font.setColor(previousColor);

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
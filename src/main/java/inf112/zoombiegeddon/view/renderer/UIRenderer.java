package inf112.zoombiegeddon.view.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import inf112.zoombiegeddon.model.entity.Entity;
import inf112.zoombiegeddon.model.entity.IDamageable;
import inf112.zoombiegeddon.model.entity.IEntity;

public class UIRenderer {

    private final ShapeRenderer shapeRenderer;
    private final OrthographicCamera camera;
    private final IEntity targetEntity;
    private final IDamageable damageable;
    private int row;

    /**
     * We pass the Player/Enemy as an Entity that also implements IDamageable.
     */
    public <T extends Entity & IDamageable> UIRenderer(OrthographicCamera camera, T entity) {
        this.camera = camera;
        this.shapeRenderer = new ShapeRenderer();
        this.targetEntity = entity;
        this.damageable = entity;
    }

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
    public void dispose() {
        shapeRenderer.dispose();
    }
}
package inf112.zoombiegeddon.view.renderer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import inf112.zoombiegeddon.model.entity.Entity;
import inf112.zoombiegeddon.model.entity.IDamageable;
import inf112.zoombiegeddon.model.entity.IEntity;

public class UIRenderer {

    private final ShapeRenderer shapeRenderer;
    private final OrthographicCamera camera;
    private final IEntity targetEntity;
    private final IDamageable damageable;

    /**
     * We pass the Player/Enemy as an Entity that also implements IDamageable.
     */
    public <T extends Entity & IDamageable> UIRenderer(OrthographicCamera camera, T entity) {
        this.camera = camera;
        this.shapeRenderer = new ShapeRenderer();
        this.targetEntity = entity;
        this.damageable = entity;
    }

    public void drawUI() {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(Color.RED);
        float x = targetEntity.getPosition().x;
        float y = targetEntity.getPosition().y + targetEntity.getHeight() + 0.2f;
        float width = targetEntity.getWidth();
        float height = 0.1f;

        shapeRenderer.rect(x, y, width, height);

        float currentHp = (float) damageable.getHealth();
        float maxHp = (float) damageable.getMaxHealth();
        float healthPercentage = (maxHp > 0) ? (currentHp / maxHp) : 0;

        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(x, y, width * healthPercentage, height);

        shapeRenderer.end();
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}
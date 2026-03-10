package inf112.zoombiegeddon.view.renderer;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import inf112.zoombiegeddon.model.entity.IEntity;

public class EntityRenderer  {

    public void render(SpriteBatch batch, IEntity entity, TextureRegion currentFrame) {

        float x = entity.getPosition().x - entity.getScaledSpriteDimensions() / 2f;
        float y = entity.getPosition().y - entity.getScaledSpriteDimensions() / 2f;

        batch.draw(currentFrame, x, y, entity.getScaledSpriteDimensions(), entity.getScaledSpriteDimensions());
    }
}

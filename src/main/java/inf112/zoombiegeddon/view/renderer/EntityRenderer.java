package inf112.zoombiegeddon.view.renderer;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import inf112.zoombiegeddon.model.entity.IEntity;

public class EntityRenderer  {

    public void render(SpriteBatch batch, IEntity entity, TextureRegion currentFrame) {

        float x = entity.getPosition().x - entity.getWidth() / 2f;
        float y = entity.getPosition().y - entity.getHeight() / 2f;

        batch.draw(currentFrame, x, y, entity.getWidth(), entity.getHeight());
    }
}

package inf112.zoombiegeddon.model.world.background;

import com.badlogic.gdx.graphics.Texture;

public class Layer {

        Texture texture;
        float factor;

        protected Layer(Texture texture, float factor) {
            this.texture = texture;
            this.factor = factor;

            this.texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        }
}

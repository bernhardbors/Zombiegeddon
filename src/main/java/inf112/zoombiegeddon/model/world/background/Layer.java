package inf112.zoombiegeddon.model.world.background;

import com.badlogic.gdx.graphics.Texture;

/**
 * Represents a single graphical layer in a parallax background system.
 * Each layer consists of a texture and a movement factor that determines
 * how fast the layer moves relative to the camera.
 */
public class Layer {

    /** The texture image used for this background layer. */
    Texture texture;

    /** * The parallax factor.
     * A value of 0 means the layer is static, while values closer to 1
     * make the layer move almost as fast as the camera.
     */
    float factor;

    /**
     * Constructs a new Layer with a texture and a parallax factor.
     * Sets the texture wrap mode to {@link Texture.TextureWrap#Repeat} to allow
     * infinite horizontal scrolling.
     *
     * @param texture The texture to be rendered for this layer.
     * @param factor  The speed multiplier for the parallax effect.
     */
    protected Layer(Texture texture, float factor) {
        this.texture = texture;
        this.factor = factor;

        this.texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
    }
}
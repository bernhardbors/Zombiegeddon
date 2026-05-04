package inf112.zoombiegeddon.view.background;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages a multi-layered parallax background system.
 * This class calculates the horizontal offset for each background layer based on
 * the camera's position and a specific parallax factor, creating an illusion
 * of depth as the player moves.
 */
public class ParallaxBackground {
    private final List<Layer> layers;
    private OrthographicCamera camera;

    /**
     * Constructs a new ParallaxBackground with a reference to the game camera.
     *
     * @param camera The camera used to determine the background offset.
     */
    public ParallaxBackground(OrthographicCamera camera) {
        this.camera = camera;
        this.layers = new ArrayList<>();
    }

    /**
     * Empty constructor for creating the background before the camera is available.
     */
    public ParallaxBackground() {
        this.layers = new ArrayList<>();
    }

    /**
     * Updates the camera reference used for calculating background movement.
     * Useful if the camera is recreated or swapped between levels.
     *
     * @param camera The new OrthographicCamera instance.
     */
    public void setCamera(OrthographicCamera camera) {
        this.camera = camera;
    }

    /**
     * Adds a new background layer to the parallax system.
     *
     * @param texture The texture for the layer.
     * @param factor  The parallax factor (0 = static, higher = faster movement).
     */
    public void addLayer(Texture texture, float factor) {
        layers.add(new Layer(texture, factor));
    }

    /**
     * Renders all registered background layers.
     * Calculates the horizontal offset for each layer by multiplying the camera's
     * X-position with the layer's factor, ensuring the background follows the
     * camera while scrolling internally.
     *
     * @param batch The SpriteBatch used for drawing the textures.
     */
    public void render(Batch batch) {
        if (camera == null) return;

        for (Layer layer : layers) {
            float xOffset = (camera.position.x * layer.factor) / 100;

            batch.draw(layer.texture,
                    camera.position.x - camera.viewportWidth / 2,
                    camera.position.y - camera.viewportHeight /2,
                    camera.viewportWidth,
                    camera.viewportHeight,
                    (int) (xOffset * layer.texture.getWidth()),
                    0,
                    layer.texture.getWidth(),
                    layer.texture.getHeight(),
                    false, false);
        }
    }

    /**
     * Removes all background layers from the parallax system.
     */
    public void clearLayers() {
        layers.clear();
    }
}
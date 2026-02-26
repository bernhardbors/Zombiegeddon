package inf112.zoombiegeddon.model.world.background;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

public class ParallaxBackground {
    private final List<Layer> layers;
    private OrthographicCamera camera;

    public void setCamera(OrthographicCamera camera) {
        this.camera = camera;
    }

    public ParallaxBackground(OrthographicCamera camera) {
        this.camera = camera;
        this.layers = new ArrayList<>();
    }

    public void addLayer(Texture texture, float factor) {
        layers.add(new Layer(texture, factor));
    }

    public void render(SpriteBatch batch) {
        for (Layer layer : layers) {

            float xOffset = (camera.position.x * layer.factor) / 100;

            batch.draw(layer.texture,
                    camera.position.x - camera.viewportWidth / 2,
                    camera.position.y - camera.viewportHeight / 2,
                    camera.viewportWidth,
                    camera.viewportHeight,
                    (int) (xOffset * layer.texture.getWidth()),
                    0,
                    layer.texture.getWidth(),
                    layer.texture.getHeight(),
                    false, false);
        }
    }
}

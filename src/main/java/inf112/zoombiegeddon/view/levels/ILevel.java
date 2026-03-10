package inf112.zoombiegeddon.view.levels;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import inf112.zoombiegeddon.model.entity.Entity;

/**
 * Interface for all game levels.
 * Provides the game loop for updating physics simulation, world rendering,
 * camera management, and map loading within the LibGDX framework.
 */
public interface ILevel {

    /**
     * Loads a TiledMap (.tmx) and initializes the collision layer.
     *
     * @param fileName               Path to the map file.
     * @param collisionLayerName Name of the layer used for physics collisions.
     */
    public void loadMap(String fileName, String collisionLayerName);

    /**
     * Updates the physics simulation.
     *
     * @param delta The time step in seconds.
     */
    void update(float delta);

    public abstract void draw(Batch batch);

    /**
     * Renders all game entities within a SpriteBatch block.
     */
    public void drawEntities(Entity entity);

    /**
     * Renders the background using the application's background renderer.
     */
    public void drawParallaxBackground();

    /**
     * Draws text to the screen while playing
     *
     * @param text The text wanted to show up on the screen
     * @param color The color of the text
     * @param scale The wanted scale of the text
     */
    public Label drawText(String text, Color color, float scale);

}

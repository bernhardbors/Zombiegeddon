package inf112.zoombiegeddon.view.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import inf112.zoombiegeddon.app.GameApp;
import inf112.zoombiegeddon.controller.PlayerController;
import inf112.zoombiegeddon.model.entity.Bullet;
import inf112.zoombiegeddon.model.entity.Enemy;
import inf112.zoombiegeddon.model.entity.Player;
import inf112.zoombiegeddon.model.world.Spawner;
import inf112.zoombiegeddon.utils.CameraUtils;
import inf112.zoombiegeddon.view.renderer.EntityRenderer;
import inf112.zoombiegeddon.view.renderer.UIRenderer;
import inf112.zoombiegeddon.view.screens.MainMenuScreen;
import inf112.zoombiegeddon.model.world.triggers.TriggerZone;

/**
 * A concrete implementation of a game level used for testing purposes.
 * This level initializes a player, sets up input handling, and manages
 * camera movement relative to the player's position.
 */
public class Level0 extends AbstractLevel {


    public Level0(Viewport viewport, OrthographicCamera camera, GameApp app, Vector2 gravity) {
        super(viewport, camera, app, gravity);
    }

    @Override
    public void loadMap(String fileName, String collisionLayerName) {

    }

    @Override
    public void draw(Batch batch) {

    }

    @Override
    public void dispose() {

    }
}
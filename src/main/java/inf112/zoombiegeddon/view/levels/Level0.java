package inf112.zoombiegeddon.view.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import inf112.zoombiegeddon.app.GameApp;
import inf112.zoombiegeddon.controller.PlayerController;
import inf112.zoombiegeddon.model.entity.Enemy;
import inf112.zoombiegeddon.model.entity.Player;
import inf112.zoombiegeddon.utils.CameraUtils;
import inf112.zoombiegeddon.view.renderer.AbstractLevel;

/**
 * A concrete implementation of a game level used for testing purposes.
 * This level initializes a player, sets up input handling, and manages
 * camera movement relative to the player's position.
 */
public class Level0 extends AbstractLevel {

    private final Player player;

    private final Enemy enemy;

    /**
     * Constructs a new TestLevel01.
     * Initializes the physics world, creates the player at a starting position,
     * sets up the controller, and loads the specific tiled map for the level.
     *
     * @param app     The main game application instance.
     * @param gravity The gravity vector for this level's physics.
     */
    public Level0(GameApp app, Vector2 gravity) {
        super(app, gravity, 20f, 20f, true);

        this.player = new Player(worldPhysics.getWorld(), new Vector2(10, 10), app.playerAnimations);
        this.controller = new PlayerController(player, audio);

        this.enemy = new Enemy(worldPhysics.getWorld(), new Vector2(10, 10), app.enemyAnimations);

        app.background.setCamera(camera);

        loadMap("tilemaps/testLevel.tmx", "Collision");
    }

    /**
     * Called when this screen becomes the current screen for the game.
     * Sets the input processor to the player controller to enable user interaction.
     */
    @Override
    public void show() {
        Gdx.input.setInputProcessor(controller);
    }

    /**
     * Updates the level logic, including physics, player state, and camera positioning.
     *
     * @param delta The time in seconds since the last frame.
     */
    @Override
    protected void update(float delta) {
        super.update(delta);

        controller.update(delta);
        player.update(delta);
        enemy.update(delta);

        CameraUtils.updateCamera(player, camera);
    }

    /**
     * Draws the level entities.
     *
     * @param batch The SpriteBatch used for rendering the player.
     */
    @Override
    protected void draw(SpriteBatch batch) {
        player.draw(batch);
        enemy.draw(batch);
    }
}
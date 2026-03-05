package inf112.zoombiegeddon.view.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import inf112.zoombiegeddon.app.GameApp;
import inf112.zoombiegeddon.controller.PlayerController;
import inf112.zoombiegeddon.model.entity.Bullet;
import inf112.zoombiegeddon.model.entity.Enemy;
import inf112.zoombiegeddon.model.entity.Player;
import inf112.zoombiegeddon.model.world.Spawner;
import inf112.zoombiegeddon.utils.CameraUtils;
import inf112.zoombiegeddon.view.renderer.UIRenderer;
import inf112.zoombiegeddon.view.screens.MainMenuScreen;
import inf112.zoombiegeddon.model.world.triggers.TriggerZone;

/**
 * A concrete implementation of a game level used for testing purposes.
 * This level initializes a player, sets up input handling, and manages
 * camera movement relative to the player's position.
 */
public class Level0 extends AbstractLevel {

    private final Player player;
    private final Enemy zombie;

    private final UIRenderer UIRendererPlayer;
    private final UIRenderer UIRendererEnemy;
    private final TriggerZone triggerZone;

    private final Spawner spawner;
    private Label infoText;

    /**
     * Constructs a new TestLevel01.
     * Initializes the physics world, creates the player at a starting position,
     * sets up the controller, and loads the specific tiled map for the level.
     *
     * @param app     The main game application instance.
     * @param gravity The gravity vector for this level's physics.
     */
    public Level0(GameApp app, Vector2 gravity) {
        super(app, gravity, 10f, 10f, false);

        loadMap("tilemaps/level1.tmx", "Collision");
        this.spawner = new Spawner(map);
        this.triggerZone = new TriggerZone(map, "Signs", "sign");

        System.out.println("Spawning player at: " + spawner.getSpawnPoint("player_spawn"));
        this.player = new Player(worldPhysics.getWorld(), spawner.getSpawnPoint("player_spawn"), app.playerMaleAnimations, 10);
        this.controller = new PlayerController(player);

        System.out.println("Spawning enemy at: " + spawner.getSpawnPoint("enemy_spawn"));
        this.zombie = new Enemy(worldPhysics.getWorld(), spawner.getSpawnPoint("enemy_spawn"), app.zombieAnimations,10);

        app.background.setCamera(camera);

        this.UIRendererEnemy = new UIRenderer(camera, zombie);

        this.UIRendererPlayer = new UIRenderer(camera, player);


        if (!shader.isCompiled()) {
            Gdx.app.error("Shader", "Compile fail: " + shader.getLog());
        }

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

        super.update(delta);

        player.update(delta);
        zombie.update(delta);

        checkShooting(delta);

        if (player.isDead()) {
            app.setScreen(new MainMenuScreen(app));
        }

        zombie.update(delta);

        CameraUtils.updateCamera(player, camera);

        String message = triggerZone.update(player.getBounds());

        if(message!=null) {

            if (infoText == null || !infoText.getText().equals(message)) {
                infoText = drawText(message, Color.WHITE, 0.07f);
                infoText.pack();
            }

            Rectangle zone = triggerZone.getZone();
            float centerX = zone.x - (infoText.getWidth() / 2f);
            float centerY = zone.y + zone.height + 2f;

            infoText.setPosition(centerX, centerY);

        }
        else{
            infoText=null;
        }
    }

    /**
     * Draws the level entities.
     *
     * @param batch The SpriteBatch used for rendering the player.
     */
    @Override
    protected void draw(SpriteBatch batch) {

        player.draw(batch);


            zombie.draw(batch);
            if(infoText != null) {
                infoText.draw(batch,1f);
            }

            batch.end();
            UIRendererPlayer.drawUI();
            UIRendererEnemy.drawUI();
            batch.begin();

        for (Bullet b : bullets) {
            b.draw(batch);
        }
    }

    private final Array<Bullet> bullets = new Array<>();

    private void checkShooting(float delta) {

        if (controller.isShooting()) {
            Bullet newBullet = player.createBullet(worldPhysics.getWorld());

            bullets.add(newBullet);
            controller.setIsShooting(false);
        }

        for (int i = 0; i < bullets.size; i++) {
            Bullet b = bullets.get(i);
            b.update(delta);

            if (b.isToRemove()) {
                worldPhysics.getWorld().destroyBody(b.getPhysics().getBody());
                bullets.removeIndex(i);
                i--;
            }
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        UIRendererPlayer.dispose();
    }
}
package inf112.zoombiegeddon.view.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import inf112.zoombiegeddon.app.GameApp;
import inf112.zoombiegeddon.controller.PlayerController;
import inf112.zoombiegeddon.model.animations.AnimationHandler;
import inf112.zoombiegeddon.model.entity.Player;
import inf112.zoombiegeddon.utils.CameraUtils;
import inf112.zoombiegeddon.view.renderer.AbstractLevel;

public class TestLevel01 extends AbstractLevel {

    private final Player player;
    private final PlayerController controller;

    public TestLevel01(GameApp app, Vector2 gravity) {
        super(app, gravity, 10, 10,true);

        this.player = new Player(worldPhysics.getWorld(), new Vector2(10, 10), app.playerAnimations);

        app.background.setCamera(this.camera);

        this.controller = new PlayerController(player);

        drawMapCollisionLayer("src/main/resources/tilemaps/testLevel.tmx", "Collision");
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(controller);
    }

    @Override
    public void draw(SpriteBatch batch) {
        player.draw(batch);
    }

    @Override
    protected void update(float delta) {
        super.update(delta);

        controller.update(delta);
        player.update(delta);

        CameraUtils.updateCamera(player, camera);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }

    @Override
    public void dispose() {
        super.dispose();
    }

}

package inf112.zoombiegeddon.view.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import inf112.zoombiegeddon.app.GameApp;
import inf112.zoombiegeddon.controller.PlayerController;
import inf112.zoombiegeddon.model.Player;
import inf112.zoombiegeddon.utils.CameraUtils;
import inf112.zoombiegeddon.view.renderer.AbstractLevel;

import java.util.HashMap;
import java.util.Map;

public class TestLevel01 extends AbstractLevel {

    private final Player player;
    private final PlayerController controller;
    private final Map<Player.state, Animation<TextureRegion>> animations = new HashMap<>();


    public TestLevel01(GameApp app, Vector2 gravity) {
        super(app, gravity, 10, 10,false);

        TextureRegion[][] IdelAnimation = TextureRegion.split(app.characterSpriteSheet, 80, 64);

        animations.put(Player.state.IDLE ,createAnimation(IdelAnimation[0],4, 0.2f));
        animations.put(Player.state.WALKING ,createAnimation(IdelAnimation[1],7, 0.2f));
        animations.put(Player.state.RUNNING ,createAnimation(IdelAnimation[2],7, 0.2f));
        animations.put(Player.state.JUMPING ,createAnimation(IdelAnimation[3],3, 0.2f));
        animations.put(Player.state.FALLING ,createAnimation(IdelAnimation[4],3, 0.2f));
        animations.put(Player.state.PUNCHING ,createAnimation(IdelAnimation[5],5, 0.2f));
        animations.put(Player.state.DEAD ,createAnimation(IdelAnimation[6],9, 0.2f));

        this.player = new Player(worldPhysics.getWorld(), animations, new Vector2(10, 10));

        this.controller = new PlayerController(player);

        drawMapCollisionLayer("src/main/resources/tilemaps/testLevel.tmx", "Collision", 3);
    }

    /**
     * Extracts a specific number of frames from a row to prevent "blinking" blank frames.
     */
    private Animation<TextureRegion> createAnimation(TextureRegion[] row, int frameCount, float frameDuration) {
        TextureRegion[] frames = new TextureRegion[frameCount];
        for (int i = 0; i < frameCount; i++) {
            frames[i] = row[i];
        }
        return new Animation<>(frameDuration, frames);
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

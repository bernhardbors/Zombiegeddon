package inf112.zoombiegeddon.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class WorldPhysics {

    private final World world;
    private float accumulatedTime = 0;

    public WorldPhysics(Vector2 gravity) {
        this.world = new World(gravity, true);
    }

    public WorldPhysics(World world) {
        this.world = world;
    }

    // from https://gafferongames.com/post/fix_your_timestep/
    public void update(float delta) {
        accumulatedTime += delta;
        while (accumulatedTime >= (1 / 60f)) {
            accumulatedTime -= (1 / 60f);
            world.step(0.05f, 6, 2);
            accumulatedTime -= (1 / 60f);
        }
    }

    public World getWorld() {
        return world;
    }

    public void dispose() {
        world.dispose();
    }
}


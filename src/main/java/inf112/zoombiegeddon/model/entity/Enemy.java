package inf112.zoombiegeddon.model.entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import inf112.zoombiegeddon.model.animations.AnimationHandler;

public class Enemy extends Entity {
    public Enemy(World world, Vector2 pos, AnimationHandler handler) {
        super(world, pos, handler);
    }

    @Override
    protected void defineEntity(Vector2 pos) {
        // Define a simpler or different hitbox for enemies
    }

    @Override
    public void update(float dt) {
        stateTime += dt;
        // Simple AI: Move left until hitting a wall
        float moveSpeed = -2.0f;
        body.setLinearVelocity(moveSpeed, body.getLinearVelocity().y);

        // Sync sprite
        setPosition(body.getPosition().x - getWidth()/2, body.getPosition().y - getHeight()/2);
    }
}
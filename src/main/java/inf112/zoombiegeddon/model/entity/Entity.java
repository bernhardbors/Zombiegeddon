package inf112.zoombiegeddon.model.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import inf112.zoombiegeddon.model.animations.AnimationHandler;

public abstract class Entity extends Sprite {
    public static final float PPM = 32.0f;
    protected World world;
    protected Body body;
    protected AnimationHandler animationHandler;

    // Shared state variables
    protected float stateTime = 0f;
    protected boolean facingRight = true;
    protected boolean isGrounded = false;

    public Entity(World world, Vector2 pos, AnimationHandler animationHandler) {
        this.world = world;
        this.animationHandler = animationHandler;

        // Initialize the body (defined by subclasses)
        defineEntity(pos);
    }

    /** Subclasses define their specific hitboxes here */
    protected abstract void defineEntity(Vector2 pos);

    public abstract void update(float dt);

    public void setGrounded(boolean grounded) {
        this.isGrounded = grounded;
    }

    public Body getBody() { return body; }
}
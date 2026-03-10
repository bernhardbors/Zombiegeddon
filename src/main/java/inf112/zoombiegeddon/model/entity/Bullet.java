package inf112.zoombiegeddon.model.entity;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import inf112.zoombiegeddon.model.animations.AnimationHandler;

/**
 * Represents a projectile fired by an entity.
 * Handles its own movement, lifespan, and marking for removal upon impact.
 */
public class Bullet extends Entity implements IProjectile {

    private static final float BULLET_SPEED = 10.0f;
    private static final float MAX_LIFESPAN = 2.0f; // Seconds before the bullet disappears automatically

    private boolean toRemove = false;
    private float lifeTimer = 0f;
    private final int damage = 1;

    /**
     * Constructs a new Bullet.
     * * @param world          The Box2D world.
     * @param position       The spawn position.
     * @param directionRight True if firing right, false if left.
     */
    public Bullet(World world, Vector2 position, boolean directionRight) {

        super(new PhysicsComponent(world, position, 0.1f, 0.1f, null));


        this.physics.getBody().setUserData(this);


        this.physics.getBody().setGravityScale(0);
        this.physics.getBody().setBullet(true);


        float velocityX = directionRight ? BULLET_SPEED : -BULLET_SPEED;
        this.physics.getBody().setLinearVelocity(new Vector2(velocityX, 0));

        this.pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(1, 0, 1, 1);
        pixmap.fill();
        this.blueTexture = new Texture(pixmap);
    }

    Pixmap pixmap;
    Texture blueTexture;
    /**
     * Updates the bullet's position based on physics and manages its lifespan.
     * * @param dt Time in seconds since last frame.
     */
    public void update(float dt) {
        lifeTimer += dt;
        if (lifeTimer >= MAX_LIFESPAN) {
            markForRemoval();
        }
    }

    /**
     * Bullets do not use walking/running animations.
     * @return Always returns IDLE.
     */
    @Override
    public EntityState determineMovingState() {
        return EntityState.IDLE;
    }

    @Override
    public boolean isToRemove() {
        return toRemove;
    }

    @Override
    public void markForRemoval() {
        this.toRemove = true;
    }

    @Override
    public int getDamage() {
        return damage;
    }

    /**
     * Sets the entity position
     *
     * @param x pos on the x-axis
     * @param y pos on the y-axis
     */
    @Override
    public void setPosition(float x, float y) {

    }

    /**
     * Gets the current position of the entity from the physics component.
     *
     * @return A Vector2 representing the world coordinates.
     */
    @Override
    public Vector2 getPosition() {
        return physics.getPosition();
    }

    /**
     * Provides access to the entity's physics component.
     *
     * @return The PhysicsComponent instance.
     */
    @Override
    public PhysicsComponent getPhysics() {
        return physics;
    }

    /**
     *
     * @return boolean true if on ground false if not.
     */
    @Override
    public boolean isGrounded() {
        return isGrounded;
    }

    /**
     * Sets the entity Width
     */
    @Override
    public void setWidth() {

    }

    /**
     * Sets the entity Height
     */
    @Override
    public void setHeight() {

    }

    /**
     * @return entity Width
     */
    @Override
    public float getWidth() {
        return 0;
    }

    /**
     * @return entity Height
     */
    @Override
    public float getHeight() {
        return 0;
    }

    /**
     * @return scaled Sprite Dimensions (sprite dim * worldScale)
     */
    @Override
    public float getScaledSpriteDimensions() {
        return 0;
    }

    @Override
    public AnimationHandler getAnimationHandler() {
        return null;
    }
}
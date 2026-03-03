package inf112.zoombiegeddon.model.entity;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

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


        setSize(0.2f, 0.1f);

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
    @Override
    public void update(float dt) {

        super.update(dt);

        setRegion(blueTexture);
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
    protected EntityState determineMovingState() {
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
}
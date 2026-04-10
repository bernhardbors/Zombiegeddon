package inf112.zoombiegeddon.model.entity.enemies;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import inf112.zoombiegeddon.model.animations.AnimationHandler;
import inf112.zoombiegeddon.model.entity.Entity;
import inf112.zoombiegeddon.model.entity.EntityState;
import inf112.zoombiegeddon.model.entity.IDamageable;
import inf112.zoombiegeddon.model.entity.PhysicsComponent;
import inf112.zoombiegeddon.view.levels.AbstractLevel;

/**
 * Represents an enemy entity in the game.
 * Handles simple patrol behavior, health, and physics-based interactions.
 */
public class Enemy extends Entity implements IDamageable {


    private static final float VELOCITY_THRESHOLD = 0.1f;

    AnimationHandler animationHandler;

    private float spriteDim;

    private float patrolSpeed;
    private int direction = Math.random() < 0.5 ? 1 : -1;

    private boolean isDead = false;
    private int HP;
    private final int maxHP;

    /**
     * Constructs a new enemy with the given spawn position, animation handler,
     * health, and patrol speed.
     *
     * @param world The Box2D world the enemy belongs to.
     * @param spawnPos The spawn position of the enemy.
     * @param animationHandler The animation handler used for this enemy.
     * @param hp The starting and maximum health of the enemy.
     * @param speed The patrol speed of the enemy.
     */
    public Enemy(World world, Vector2 spawnPos, AnimationHandler animationHandler, int hp, float speed) {

        this.spriteDim = 32 * AbstractLevel.UNIT_SCALE;
        float actualW = 11 * AbstractLevel.UNIT_SCALE;
        float actualH = 20 * AbstractLevel.UNIT_SCALE;

        super(new PhysicsComponent(world, spawnPos, actualW, actualH, "enemy"));

        this.physics.getBody().setUserData(this);
        this.animationHandler = animationHandler;
        this.HP = hp;
        this.maxHP = hp;

        this.patrolSpeed = speed;

        this.width = actualW;
        this.height = actualH;
    }

    /**
     * Updates the enemy's movement and animation state.
     *
     * @param dt The time in seconds since the last update.
     */
    @Override
    public void update(float dt) {
        Vector2 vel = physics.getVelocity();

        if (isGrounded()) {
            if (shouldTurnAround(direction)) {
                direction *= -1;
            }
        }

        physics.getBody().setLinearVelocity(direction * patrolSpeed, vel.y);

        updateAnimationState(dt, VELOCITY_THRESHOLD);
    }

    /**
     * Determines the animation state used while the enemy is moving on the ground.
     *
     * @return The movement state for this enemy.
     */
    @Override
    public EntityState determineMovingState() {
        return  EntityState.WALKING;
    }

    /**
     * Reduces the enemy's health by the given amount.
     * Marks the enemy as dead when health reaches zero or below.
     *
     * @param amount The amount of damage to deal.
     */
    @Override
    public void takeDamage(int amount) {
        this.HP -= amount;
        if (this.HP <= 0) {
            isDead = true;
        }
    }

    /**
     * Checks whether the enemy should turn around due to a wall ahead
     * or missing ground in front of it.
     *
     * @param dir The current horizontal direction, where 1 is right and -1 is left.
     * @return True if the enemy should turn around, false otherwise.
     */
    private boolean shouldTurnAround(int dir) {
        World world = physics.getBody().getWorld();
        Vector2 pos = physics.getPosition();

        float lookAheadX = pos.x + (this.width / 2f + 0.5f) * dir;
        float scanY = pos.y - (this.height / 2f) + (this.height * 0.2f);

        Vector2 wallStart = new Vector2(pos.x, scanY);
        Vector2 wallEnd = new Vector2(lookAheadX, scanY);

        Vector2 ledgeStart = new Vector2(lookAheadX, pos.y);
        Vector2 ledgeEnd = new Vector2(lookAheadX, pos.y - (this.height / 2f + 1.0f));

        final boolean[] hitWall = {false};
        final boolean[] hasGround = {false};

        RayCastCallback wallCallback = (fixture, point, normal, fraction) -> {
            if (fixture.isSensor()) {
                return 1;
            }
            if (fixture.getBody() == physics.getBody()) {
                return 1;
            }
            if ("player".equals(fixture.getUserData())) {
                return 1;
            }

            hitWall[0] = true;
            return 0;
        };

        RayCastCallback ledgeCallback = (fixture, point, normal, fraction) -> {
            if (!fixture.isSensor() && !"enemy".equals(fixture.getUserData())) {
                hasGround[0] = true;
                return 0;
            }
            return 1;
        };

        world.rayCast(wallCallback, wallStart, wallEnd);
        world.rayCast(ledgeCallback, ledgeStart, ledgeEnd);

        return hitWall[0] || !hasGround[0];
    }

    /**
     * @return The current health value of the entity.
     */
    @Override
    public int getHealth() {
        return this.HP;
    }

    /**
     * @return The maximum health capacity of the entity.
     */
    @Override
    public int getMaxHealth() {
        return this.maxHP;
    }

    /**
     * @return True if the entity's health is zero or below.
     */
    @Override
    public boolean isDead() {
        return isDead;
    }

    /**
     * Resets or increases health.
     *
     * @param amount The amount of health to restore.
     */
    @Override
    public void heal(int amount) {
        this.HP += amount;
        if (this.HP >= maxHP) {
            this.HP = maxHP;
        }
    }

    /**
     * Sets the entity position
     *
     * @param x pos on the x-axis
     * @param y pos on the y-axis
     */
    @Override
    public void setPosition(float x, float y) {
        physics.getBody().getPosition().set(x, y);
    }

    /**
     * Gets the current position of the entity from the physics component.
     *
     * @return A Vector2 representing the world coordinates.
     */
    @Override
    public Vector2 getPosition() {
        return physics.getBody().getPosition();
    }

    /**
     * Sets the movement direction of the enemy.
     * @param direction 1 for right, -1 for left.
     */
    public void setDirection(int direction) {
        this.direction = direction;
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
        //TODO: IMPLEMENT METHODE
    }

    /**
     * Sets the entity Height
     */
    @Override
    public void setHeight() {
        //TODO: IMPLEMENT METHODE
    }

    /**
     * @return entity Width
     */
    @Override
    public float getWidth() {
        return width;
    }

    /**
     * @return entity Height
     */
    @Override
    public float getHeight() {
        return height;
    }

    /**
     * @return scaled Sprite Dimensions (sprite dim * worldScale)
     */
    @Override
    public float getScaledSpriteDimensions() {
        return spriteDim;
    }

    /**
     * Returns the animation handler used by the enemy.
     *
     * @return The animation handler.
     */
    @Override
    public AnimationHandler getAnimationHandler() {
        return animationHandler;
    }
}
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
 * Representerer en fiende-entitet.
 * Håndterer enkel patruljerings-AI og fysiske interaksjoner.
 */
public class Enemy extends Entity implements IDamageable {


    private static final float VELOCITY_THRESHOLD = 0.1f;

    private AnimationHandler animationHandler;

    private float spriteDim;

    private float patrolSpeed = 1.0f;
    private int direction = 1;

    private boolean isDead = false;
    private int HP;
    private final int maxHP;

    public Enemy(World world, Vector2 spawnPos, AnimationHandler animationHandler, int Hp) {

        this.spriteDim = 32 * AbstractLevel.UNIT_SCALE;
        float actualW = 11 * AbstractLevel.UNIT_SCALE;
        float actualH = 20 * AbstractLevel.UNIT_SCALE;

        super(new PhysicsComponent(world, spawnPos, actualW, actualH, "enemy"));

        this.physics.getBody().setUserData(this);
        this.animationHandler = animationHandler;
        this.HP = Hp;
        this.maxHP = Hp;

        this.width = actualW;
        this.height = actualH;
    }

    /**
     *
     * @param dt Delta Time
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
     * Determines which state the entity should be in when it is moving on the ground.
     * This allows subclasses to choose between states like WALKING or RUNNING.
     *
     * @return The determined EntityState for horizontal movement.
     */
    @Override
    public EntityState determineMovingState() {
        return  EntityState.WALKING;
    }

    /**
     * Subtracts the specified amount from the entity's health.
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

    private boolean shouldTurnAround(int dir) {
        World world = physics.getBody().getWorld();
        Vector2 pos = physics.getPosition();

        float lookAheadX = pos.x + (this.width / 2f + 0.5f) * dir;

        Vector2 wallStart = new Vector2(pos.x, pos.y);
        Vector2 wallEnd = new Vector2(lookAheadX, pos.y);

        Vector2 ledgeStart = new Vector2(lookAheadX, pos.y);
        Vector2 ledgeEnd = new Vector2(lookAheadX, pos.y - (this.height / 2f + 1.0f));

        final boolean[] hitWall = {false};
        final boolean[] hasGround = {false};

        RayCastCallback wallCallback = (fixture, point, normal, fraction) -> {
            if (!fixture.isSensor() && !"enemy".equals(fixture.getUserData())) {
                hitWall[0] = true;
                return 0;
            }
            return 1;
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

    @Override
    public AnimationHandler getAnimationHandler() {
        return animationHandler;
    }
}
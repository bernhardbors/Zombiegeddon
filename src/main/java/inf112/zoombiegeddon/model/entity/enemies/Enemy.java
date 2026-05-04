package inf112.zoombiegeddon.model.entity.enemies;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import inf112.zoombiegeddon.events.GameEvent;
import inf112.zoombiegeddon.model.entity.*;
import inf112.zoombiegeddon.model.entity.Entity;
import inf112.zoombiegeddon.model.entity.EntityState;
import inf112.zoombiegeddon.model.entity.HealthComponent;
import inf112.zoombiegeddon.model.entity.PhysicsComponent;
import inf112.zoombiegeddon.model.levels.LevelModel;

/**
 * Represents an enemy entity in the game.
 * Handles patrol behavior and physics-based interactions.
 * Health management is delegated to {@link HealthComponent}.
 * Animation resources are managed by the View layer via EntityRenderer.
 */
public class Enemy extends Entity implements IEnemy {

    private static final float VELOCITY_THRESHOLD = 0.1f;

    private final HealthComponent health;
    private final float patrolSpeed;
    private int direction = Math.random() < 0.5 ? 1 : -1;

    /**
     * Constructs a new enemy.
     *
     * @param world    The Box2D world the enemy belongs to.
     * @param spawnPos The spawn position of the enemy.
     * @param hp       The starting and maximum health of the enemy.
     * @param speed    The patrol speed of the enemy.
     */
    public Enemy(World world, Vector2 spawnPos, int hp, float speed) {
        super(new PhysicsComponent(world, spawnPos,
                11 * LevelModel.UNIT_SCALE,
                20 * LevelModel.UNIT_SCALE,
                "enemy"));

        this.physics.setUserData(this);
        this.width = 11 * LevelModel.UNIT_SCALE;
        this.height = 20 * LevelModel.UNIT_SCALE;
        this.health = new HealthComponent(hp, GameEvent.ENEMY_KILLED);
        this.patrolSpeed = speed;
    }

    @Override
    public void update(float dt) {
        Vector2 vel = physics.getVelocity();

        if (isGrounded() && shouldTurnAround(direction)) {
            direction *= -1;
        }

        physics.setLinearVelocity(new Vector2(direction * patrolSpeed, vel.y));
        health.update(dt);
        updateAnimationState(dt, VELOCITY_THRESHOLD);
    }

    @Override
    public EntityState determineMovingState() {
        return EntityState.WALKING;
    }

    /**
     * Checks whether the enemy should turn around due to a wall ahead
     * or missing ground in front of it.
     *
     * @param dir The current horizontal direction: 1 for right, -1 for left.
     * @return True if the enemy should turn around.
     */
    private boolean shouldTurnAround(int dir) {
        World world = physics.getWorld();
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
            if (fixture.isSensor()) return 1;
            if (fixture.getBody() == physics.getBody()) return 1;
            if ("player".equals(fixture.getUserData())) return 1;
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

    @Override
    public void takeDamage(int amount) { health.takeDamage(amount); }

    @Override
    public void heal(int amount) { health.heal(amount); }

    @Override
    public int getHealth() { return health.getHealth(); }

    @Override
    public int getMaxHealth() { return health.getMaxHealth(); }

    @Override
    public boolean isDead() { return health.isDead(); }

    @Override
    public void setPosition(float x, float y) { physics.setTransform(x, y); }

    @Override
    public Vector2 getPosition() { return physics.getPosition(); }

    @Override
    public void setDirection(int direction) { this.direction = direction; }

    @Override
    public IPhysicsComponent getPhysics() { return physics; }

    @Override
    public boolean isGrounded() { return isGrounded; }

    @Override
    public float getWidth() { return width; }

    @Override
    public float getHeight() { return height; }

    @Override
    public float getSpeed() { return patrolSpeed; }
}
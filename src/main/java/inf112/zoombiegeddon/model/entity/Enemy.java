package inf112.zoombiegeddon.model.entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import inf112.zoombiegeddon.model.animations.AnimationHandler;
import inf112.zoombiegeddon.view.levels.AbstractLevel;

/**
 * Representerer en fiende-entitet.
 * Håndterer enkel patruljerings-AI og fysiske interaksjoner.
 */
public class Enemy extends Entity implements IDamageable {

    private static final float MOVE_SPEED = 2.0f;
    private static final float VELOCITY_THRESHOLD = 0.1f;

    private final AnimationHandler animationHandler;
    private boolean movingRight = true;

    private int HP;
    private final int maxHP;
    private float hitTimer;
    private final float InvulnerabilityCooldown = 0.0f;
    private boolean isDead = false;

    public Enemy(World world, Vector2 spawnPos, AnimationHandler animationHandler, int Hp) {

        float spriteDim = 32 * AbstractLevel.UNIT_SCALE;
        float actualW = 11 * AbstractLevel.UNIT_SCALE;
        float actualH = 20 * AbstractLevel.UNIT_SCALE;


        super(new PhysicsComponent(world, spawnPos, actualW, actualH, "enemy"));

        this.physics.getBody().setUserData(this);
        this.animationHandler = animationHandler;
        this.HP = Hp;
        this.maxHP = Hp;

        setSize(spriteDim, spriteDim);

    }

    @Override
    public void update(float dt) {

        applyPatrolMovement();

        if (Math.abs(physics.getVelocity().x) < 0.01f) {
            reverseDirection();
        }


        if (hitTimer > 0) hitTimer -= dt;

        updateAnimationState(dt, VELOCITY_THRESHOLD);

        setRegion(animationHandler.getFrame(currentState, stateTime, facingRight));
        super.update(dt);
    }

    /**
     * Setter den lineære hastigheten direkte basert på retning.
     */
    private void applyPatrolMovement() {
        float horizontalVelocity = movingRight ? MOVE_SPEED : -MOVE_SPEED;
        physics.getBody().setLinearVelocity(horizontalVelocity, physics.getVelocity().y);
    }

    @Override
    protected void updateAnimationState(float dt, float velocityThreshold) {
        stateTime += dt;

        facingRight = movingRight;

        if (Math.abs(physics.getVelocity().x) > velocityThreshold) {
            currentState = EntityState.WALKING;
        } else {
            currentState = EntityState.IDLE;
        }

        if (previousState != currentState) {
            stateTime = 0;
            previousState = currentState;
        }
    }

    @Override
    protected EntityState determineMovingState() {
        return EntityState.WALKING;
    }

    public void reverseDirection() {
        movingRight = !movingRight;
    }

    @Override
    public void takeDamage(int amount) {
        if (hitTimer <= 0) {
            this.HP -= amount;
            hitTimer = InvulnerabilityCooldown;
            System.out.println("Enemy hit! HP: " + HP);
        }
        if (this.HP <= 0 && !isDead) {
            isDead = true;
            this.HP = maxHP;
            // TODO: logic for removing when dead
        }
    }

    @Override
    public int getHealth() { return HP; }

    @Override
    public int getMaxHealth() { return maxHP; }

    @Override
    public boolean isDead() { return isDead; }

    @Override
    public void heal(int amount) {
        this.HP = Math.min(HP + amount, maxHP);
    }
}
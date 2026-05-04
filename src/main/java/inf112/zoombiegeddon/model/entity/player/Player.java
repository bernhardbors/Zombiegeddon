package inf112.zoombiegeddon.model.entity.player;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import inf112.zoombiegeddon.app.GameConstants;
import inf112.zoombiegeddon.events.GameEvent;
import inf112.zoombiegeddon.events.GameEventBus;
import inf112.zoombiegeddon.model.entity.*;
import inf112.zoombiegeddon.model.entity.enemies.IEnemy;

/**
 * Represents the player character in the game.
 * Delegates health management to {@link HealthComponent} and speed boost
 * tracking to {@link SpeedBoostComponent}, keeping this class focused on
 * player-specific movement, input response, and goal detection.
 */
public class Player extends Entity implements IPlayer, IPlayerVisuals {

    private static final float JUMP_IMPULSE = 13.0f;
    private static final float MOVE_IMPULSE = 1.0f;
    private static final float VELOCITY_THRESHOLD = 0.1f;
    private static final float COYOTE_TIME_MAX = 0.15f;
    private static final float JUMP_BUFFER_MAX = 0.10f;
    //private static final float POSITION_AUDIO_THRESHOLD = 2.0f;

    private float coyoteTimer = 0f;
    private float jumpBufferTimer = 0f;
    private boolean jumpHeld = false;
    private boolean isRunning = false;
    private boolean wasMovingHorizontally = false;
    private boolean wasRunningWhileMoving = false;

    private final HealthComponent health;
    private final SpeedBoostComponent speedBoost;

    private boolean inGoal = false;
    private final Rectangle bounds = new Rectangle();

    private float pickupVisualEffectRemaining = 0f;

    /**
     * Constructs a new player.
     *
     * @param world    The Box2D world where the player exists.
     * @param spawnPos The initial spawn position of the player.
     * @param hp       The starting and maximum health of the player.
     */
    public Player(World world, Vector2 spawnPos, int hp) {
        super(new PhysicsComponent(world, spawnPos,
                11 * GameConstants.UNIT_SCALE,
                20 * GameConstants.UNIT_SCALE,
                "player"));

        this.physics.setUserData(this);
        this.width = 11 * GameConstants.UNIT_SCALE;
        this.height = 20 * GameConstants.UNIT_SCALE;
        this.health = new HealthComponent(hp, GameEvent.PLAYER_DEATH, 1.0f);
        this.speedBoost = new SpeedBoostComponent();
    }

    /**
     * Updates the player's logic each frame.
     *
     * @param dt Time in seconds since the last update.
     */
    @Override
    public void update(float dt) {
        if (health.isDead()) {
            health.update(dt);
            return;
        }
        postHorizontalMoveStateEvents();
        updateAnimationState(dt, VELOCITY_THRESHOLD);
        health.update(dt);
        speedBoost.update(dt);
        updatePickupEffect(dt);
        updateJumpMechanics(dt);
        updateGravityScale();
    }

    private void updatePickupEffect(float dt) {
        if (pickupVisualEffectRemaining > 0f) {
            pickupVisualEffectRemaining = Math.max(0f, pickupVisualEffectRemaining - dt);
        }
    }

    private void updateJumpMechanics(float dt) {
        if (isGrounded()) {
            coyoteTimer = COYOTE_TIME_MAX;
        } else {
            coyoteTimer -= dt;
        }

        if (jumpBufferTimer > 0) jumpBufferTimer -= dt;

        if (jumpBufferTimer > 0 && coyoteTimer > 0) {
            executeJump();
        }
    }

    private void updateGravityScale() {
        Vector2 vel = physics.getVelocity();
        if (vel.y > 0.1f) {
            physics.setGravityScale(jumpHeld ? 1.5f : 5.0f);
        } else {
            physics.setGravityScale(2.0f);
        }
    }

    /**
     * Buffers a jump input. The jump executes on the next update if
     * coyote time allows it.
     */
    @Override
    public void jump() {
        jumpBufferTimer = JUMP_BUFFER_MAX;
    }

    private void executeJump() {
        physics.applyImpulse(new Vector2(0, JUMP_IMPULSE));
        jumpBufferTimer = 0;
        coyoteTimer = 0;
        GameEventBus.post(GameEvent.PLAYER_JUMP);
    }

    /**
     * Applies horizontal force to the player's physics body.
     *
     * @param left  True if the player should move left.
     * @param right True if the player should move right.
     */
    @Override
    public void applyHorizontalImpulse(boolean left, boolean right) {
        if (left)  physics.applyImpulse(new Vector2(-MOVE_IMPULSE, 0));
        if (right) physics.applyImpulse(new Vector2(MOVE_IMPULSE, 0));
    }

    @Override
    public void setRunning(boolean running) { this.isRunning = running; }

    @Override
    public boolean isRunning() { return isRunning; }

    @Override
    public void setJumpHeld(boolean held) { this.jumpHeld = held; }

    @Override
    public void applySpeedBoost(float multiplier, float durationSeconds) {
        speedBoost.apply(multiplier, durationSeconds);
    }

    @Override
    public float getActiveSpeedMultiplier() {
        return speedBoost.getMultiplier();
    }

    @Override
    public void triggerPickupEffect(float durationSeconds) {
        pickupVisualEffectRemaining = Math.max(0f, durationSeconds);
    }

    /**
     * Handles interaction between the player and an enemy.
     *
     * @param enemy       The enemy involved in the collision.
     * @param hitWithFeet True if the player hit the enemy from above.
     */
    @Override
    public void hitEnemy(IEnemy enemy, boolean hitWithFeet) {
        if (hitWithFeet) {
            enemy.takeDamage(1);
            physics.setLinearVelocity(new Vector2(physics.getVelocity().x, 0));
            executeJump();
        } else {
            health.takeDamage(1);
        }
    }

    @Override
    public boolean inGoal() { return inGoal; }

    /**
     * Posts {@link GameEvent#LEVEL_FINISHED} the first time the player enters the goal.
     *
     * @param bool true if the player is in the goal area
     */
    @Override
    public void setInGoal(boolean bool) {
        if (!this.inGoal && bool) {
            GameEventBus.post(GameEvent.LEVEL_FINISHED);
        }
        this.inGoal = bool;
    }

    @Override
    public Rectangle getBounds() {
        Vector2 p = physics.getPosition();
        bounds.set(p.x - width / 2f, p.y - height / 2f, width, height);
        return bounds;
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
    public boolean hasPickupVisualEffect() {
        return pickupVisualEffectRemaining > 0f;
    }

    @Override
    public float getPickupFxIntensity() {
        if (pickupVisualEffectRemaining <= 0f) return 0f;
        return Math.min(1f, pickupVisualEffectRemaining);
    }

    @Override
    public EntityState determineMovingState() {
        return isRunning ? EntityState.RUNNING : EntityState.WALKING;
    }

    /**
     * Teleports the player to the given position and resets velocity to zero.
     *
     * @param x the target x position in world units
     * @param y the target y position in world units
     */
    @Override
    public void setPosition(float x, float y) {
        physics.setTransform(x, y);
        physics.setLinearVelocity(new Vector2(0, 0));
    }

    @Override
    public Vector2 getPosition() { return physics.getPosition(); }

    @Override
    public IPhysicsComponent getPhysics() { return physics; }

    @Override
    public boolean isGrounded() { return isGrounded; }

    @Override
    public float getWidth() { return width; }

    @Override
    public float getHeight() { return height; }

    @Override
    public float getSpeed() { return physics.getVelocity().len(); }

    private void postHorizontalMoveStateEvents() {
        Vector2 vel = physics.getVelocity();
        boolean movingHorizontally = isGrounded() && Math.abs(vel.x) > VELOCITY_THRESHOLD;
        boolean runningNow = movingHorizontally && isRunning;

        postMoveTransition(movingHorizontally);
        postRunTransition(runningNow);
        if (wasRunningWhileMoving && !runningNow && movingHorizontally) {

            GameEventBus.post(GameEvent.PLAYER_MOVE_START);

        }

        wasRunningWhileMoving = runningNow;
        wasMovingHorizontally = movingHorizontally;
    }

    private void postMoveTransition(boolean movingHorizontally) {
        if (!wasMovingHorizontally && movingHorizontally) {
            GameEventBus.post(GameEvent.PLAYER_MOVE_START);
        } else if (wasMovingHorizontally && !movingHorizontally) {
            GameEventBus.post(GameEvent.PLAYER_MOVE_STOP);
        }
    }

    private void postRunTransition(boolean runningNow) {
        if (!wasRunningWhileMoving && runningNow) {
            GameEventBus.post(GameEvent.PLAYER_RUN_START);
        } else if (wasRunningWhileMoving && !runningNow) {
            GameEventBus.post(GameEvent.PLAYER_RUN_STOP);
        }
    }
}
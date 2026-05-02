package inf112.zoombiegeddon.model.entity;

import inf112.zoombiegeddon.events.GameEvent;
import inf112.zoombiegeddon.events.GameEventBus;

/**
 * Manages health, damage, healing, and invulnerability for an entity.
 * Posts the configured death event when the owner's health reaches zero.
 */
public class HealthComponent implements IDamageable {

    private int hp;
    private final int maxHp;
    private float hitTimer = 0f;
    private boolean isDead = false;

    private final float invulnerabilityCooldown;
    private static final float NO_COOLDOWN = 0f;

    private final GameEvent deathEvent;

    /**
     * Constructs a new HealthComponent.
     *
     * @param hp         The starting and maximum health.
     * @param deathEvent The event to post when health reaches zero.
     * @param invulnerabilityCooldown Seconds of invulnerability after taking damage.
     */
    public HealthComponent(int hp, GameEvent deathEvent, float invulnerabilityCooldown) {
        this.hp = hp;
        this.maxHp = hp;
        this.deathEvent = deathEvent;
        this.invulnerabilityCooldown = invulnerabilityCooldown;
    }

    /**
     * Constructs a HealthComponent with no invulnerability cooldown.
     *
     * @param hp         Starting and maximum health.
     * @param deathEvent Event posted when health reaches zero.
     */
    public HealthComponent(int hp, GameEvent deathEvent) {
        this(hp, deathEvent, NO_COOLDOWN);
    }

    /**
     * Updates the invulnerability timer.
     *
     * @param dt Time in seconds since the last update.
     */
    public void update(float dt) {
        if (hitTimer > 0) hitTimer -= dt;
    }

    @Override
    public void takeDamage(int amount) {
        if (isDead) return;
        if (hitTimer <= 0) {
            hp -= amount;
            hitTimer = invulnerabilityCooldown;
        }
        if (hp <= 0) {
            hp = 0;
            isDead = true;
            GameEventBus.post(deathEvent);
        }
    }

    @Override
    public void heal(int amount) {
        hp = Math.min(hp + amount, maxHp);
    }

    @Override
    public int getHealth() { return hp; }

    @Override
    public int getMaxHealth() { return maxHp; }

    @Override
    public boolean isDead() { return isDead; }
}

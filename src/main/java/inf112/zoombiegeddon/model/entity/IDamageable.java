package inf112.zoombiegeddon.model.entity;

/**
 * Interface for entities that possess health and can take damage.
 * Implementing this allows the entity to interact with combat systems
 * and environmental hazards.
 */
public interface IDamageable {

    /**
     * Subtracts the specified amount from the entity's health.
     * @param amount The amount of damage to deal.
     */
    void takeDamage(int amount);

    /**
     * @return The current health value of the entity.
     */
    int getHealth();

    /**
     * @return The maximum health capacity of the entity.
     */
    int getMaxHealth();

    /**
     * @return True if the entity's health is zero or below.
     */
    boolean isDead();

    /**
     * Resets or increases health.
     * @param amount The amount of health to restore.
     */
    void heal(int amount);
}


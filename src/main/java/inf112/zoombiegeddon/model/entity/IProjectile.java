package inf112.zoombiegeddon.model.entity;

/**
 * Interface for entities that act as projectiles.
 */
public interface IProjectile {
    /** @return true if the projectile should be removed from the world. */
    boolean isToRemove();

    /** Marks the projectile for deletion (e.g., after hitting a wall). */
    void markForRemoval();

    /** @return The damage value this projectile carries. */
    int getDamage();
}

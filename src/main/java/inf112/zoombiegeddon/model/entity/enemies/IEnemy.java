package inf112.zoombiegeddon.model.entity.enemies;

import inf112.zoombiegeddon.model.entity.IDamageable;
import inf112.zoombiegeddon.model.entity.IEntity;

/**
 * Interface for all enemies in the game.
 * Combines basic entity properties with health and damage mechanics.
 */
public interface IEnemy extends IEntity, IDamageable {

    /**
     * Sets the movement direction of the enemy.
     * @param direction 1 for right, -1 for left.
     */
    void setDirection(int direction);
}

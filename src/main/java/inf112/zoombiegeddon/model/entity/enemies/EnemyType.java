package inf112.zoombiegeddon.model.entity.enemies;

/**
 * Represents the different enemy types available in the game.
 */
public enum EnemyType {
    ZOMBIE,
    TANK,
    FAST_ZOMBIE;

    /**
     * Converts a string to an enemy type.
     * Returns {@link #ZOMBIE} if the input is null or does not match any valid type.
     *
     * @param type The string representation of the enemy type.
     * @return The matching enemy type, or {@link #ZOMBIE} as a default.
     */
    public static EnemyType fromString(String type) {
        if (type == null) return ZOMBIE;
        try {
            return EnemyType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ZOMBIE;
        }
    }
}

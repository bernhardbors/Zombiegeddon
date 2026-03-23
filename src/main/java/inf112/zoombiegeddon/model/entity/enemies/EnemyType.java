package inf112.zoombiegeddon.model.entity.enemies;

public enum EnemyType {
    ZOMBIE,
    TANK,
    FAST_ZOMBIE;

    public static EnemyType fromString(String type) {
        if (type == null) return ZOMBIE;
        try {
            return EnemyType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ZOMBIE; // Default
        }
    }
}

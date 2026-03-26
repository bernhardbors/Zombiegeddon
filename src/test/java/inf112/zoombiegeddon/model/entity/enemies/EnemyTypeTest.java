package inf112.zoombiegeddon.model.entity.enemies;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnemyTypeTest {

    @Test
    void testFromStringReturnsCorrectType() {
        assertEquals(EnemyType.TANK, EnemyType.fromString("TANK"));
        assertEquals(EnemyType.FAST_ZOMBIE, EnemyType.fromString("fast_zombie"));
    }

    @Test
    void testFromStringDefaultsToZombie() {
        assertEquals(EnemyType.ZOMBIE, EnemyType.fromString(null));
        assertEquals(EnemyType.ZOMBIE, EnemyType.fromString("NOT_A_TYPE"));
    }
}
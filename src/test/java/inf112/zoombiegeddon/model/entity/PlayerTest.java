package inf112.zoombiegeddon.model.entity;

import inf112.zoombiegeddon.model.entity.enemies.Enemy;
import inf112.zoombiegeddon.view.levels.AbstractLevel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest extends AbstractEntityTest {

    private Player player;

    /**
     * Before each test, a new Player object should be created
     */
    @BeforeEach
    void beforeEach() {
        player = new Player(mockedWorld, spawnPos, mockedAnims, 3);
    }

    @Test
    void testPlayerDoesNotTakeDamageInInvulnerabilityTime() {
        player.takeDamage(1);
        assertEquals(2, player.getHealth());

        //Player should not take damage if the time since the last update is < 1
        player.update(0.2f);
        player.takeDamage(1);
        assertEquals(2, player.getHealth());
    }

    @Test
    void testPlayerTakesDamageAfterInvulnerabilityTime() {
        player.takeDamage(1);
        assertEquals(2, player.getHealth());

        //Player should take damage if the time since the last update is > 1
        player.update(1.2f);
        player.takeDamage(2);
        assertEquals(0, player.getHealth());
    }

    @Test
    void testPlayerDies() {
        player.takeDamage(3);
        assertTrue(player.isDead());
    }

    @Test
    void takeDamageReturnsIfPlayerIsDead() {
        player.takeDamage(3);

        assertEquals(0, player.getHealth());
        assertTrue(player.isDead());

        player.update(1.2f);

        //Player should not take more damage after death
        player.takeDamage(3);
        assertEquals(0, player.getHealth());
    }

    @Test
    void testGetMaxHealth() {
        assertEquals(3, player.getMaxHealth());
    }

    @Test
    void testPlayerHeal() {
        player.takeDamage(2);
        player.heal(1);
        assertEquals(2, player.getHealth());

        player.update(1.2f);
        player.takeDamage(2);
        player.heal(3);
        assertEquals(3, player.getHealth());

    }

    @Test
    void testSetPlayerRunning() {
        player.setRunning(true);

        assertTrue(player.isRunning());
    }

    @Test
    void testPlayerInGoal() {
        player.setInGoal(true);
        assertTrue(player.inGoal());
    }

    @Test
    void testHitEnemy() {
        Enemy enemy = new Enemy(mockedWorld, spawnPos, mockedAnims, 3, 1.0f);

        //When the player does not hit enemy from above, the player should take damage
        player.hitEnemy(enemy, false);
        assertEquals(2, player.getHealth());

        //When the playerhits the enemy from above, the player should not take damage
        player.hitEnemy(enemy, true);
        assertEquals(2, player.getHealth());

    }

    @Test
    void testGetScaledSpriteDimensions() {
        float playerDim = player.getScaledSpriteDimensions();

        float expected = 32 * AbstractLevel.UNIT_SCALE;
        assertEquals(expected, playerDim);
    }

    @Test
    void testDetermineMovingState() {
        player.setRunning(true);
        assertEquals(EntityState.RUNNING, player.determineMovingState());

        player.setRunning(false);
        assertEquals(EntityState.WALKING, player.determineMovingState());
    }

    @Test
    void getWidthAndHeight() {
        float playerHeight = player.getHeight();
        float playerWidth = player.getWidth();

        assertEquals(11 * AbstractLevel.UNIT_SCALE, playerWidth);
        assertEquals(20 * AbstractLevel.UNIT_SCALE, playerHeight);
    }

    @Test
    void testGetAnimationHandler() {
        assertEquals(mockedAnims, player.getAnimationHandler());
    }

}

package inf112.zoombiegeddon.model.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import inf112.zoombiegeddon.model.entity.enemies.Enemy;
import inf112.zoombiegeddon.model.levels.LevelModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

        player.update(1.2f);
        player.takeDamage(2);
        player.heal(4);
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

        //When the player hits the enemy from above, the player should not take damage
        player.hitEnemy(enemy, true);
        assertEquals(2, player.getHealth());

    }

    @Test
    void testGetScaledSpriteDimensions() {
        float playerDim = player.getScaledSpriteDimensions();

        float expected = 32 * LevelModel.UNIT_SCALE;
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

        assertEquals(11 * LevelModel.UNIT_SCALE, playerWidth);
        assertEquals(20 * LevelModel.UNIT_SCALE, playerHeight);
    }

    @Test
    void testGetAnimationHandler() {
        assertEquals(mockedAnims, player.getAnimationHandler());
    }

    @Test
    void testApplySpeedBoost() {
        player.applySpeedBoost(2f, 4f);
        assertEquals(2f, player.getActiveSpeedMultiplier());


        player.applySpeedBoost(0.5f, 4f);
        assertEquals(1.0f, player.getActiveSpeedMultiplier());

        //After player has had the boost for given duration, the speedMultiplier
        //becomes 1.0f again.
        player.applySpeedBoost(2f, 1f);
        player.update(1.2f);
        assertEquals(1.0f, player.getActiveSpeedMultiplier());


    }

    @Test
    void testTriggerPickupHasVisualEffectColor() {
        player.triggerPickupVisualEffect(2f, null);

        //Yellow should be RGB: 1, 1, 0
        assertEquals(1.0f, player.getPickupFxColorR());
        assertEquals(1.0f, player.getPickupFxColorG());
        assertEquals(0.0f, player.getPickupFxColorB());

        //See if the color is set correctly
        player.triggerPickupVisualEffect(2f, Color.WHITE);
        assertEquals(1.0f, player.getPickupFxColorR());
        assertEquals(1.0f, player.getPickupFxColorG());
        assertEquals(1.0f, player.getPickupFxColorB());

        //See if the color does not get set back to yellow after you
        //have set it to something else
        player.triggerPickupVisualEffect(2f, null);
        assertEquals(1.0f, player.getPickupFxColorR());
        assertEquals(1.0f, player.getPickupFxColorG());
        assertEquals(1.0f, player.getPickupFxColorB());
    }

    @Test
    void testHasPickupVisualEffect() {
        player.triggerPickupVisualEffect(1f, null);
        player.update(0.2f);
        assertTrue(player.hasPickupVisualEffect());

        player.update(1.2f);
        assertFalse(player.hasPickupVisualEffect());
    }

    @Test
    void testGetPickupFxIntensity() {
        player.triggerPickupVisualEffect(1.0f, null);

        player.update(0.8f);
        //Added delta because of rounding issues
        assertEquals(0.2f, player.getPickupFxIntensity(), 0.001f);

        player.update(1.2f);
        assertEquals(0.0f, player.getPickupFxIntensity());

    }

    @Test
    void testGetBounds() {
        when(mockedBody.getPosition()).thenReturn(new Vector2(10, 10));

        Rectangle bounds = player.getBounds();

        assertEquals(10 - player.getWidth()/2f, bounds.x);
        assertEquals(10 - player.getHeight()/2f, bounds.y);
        assertEquals(player.getWidth(), bounds.width);
        assertEquals(player.getHeight(), bounds.height);
    }

    @Test
    void testApplyHorizontalImpulseLeft() {
        when(mockedBody.getWorldCenter()).thenReturn(new Vector2(0,0));
        player.applyHorizontalImpulse(true, false);
        verify(mockedBody).applyLinearImpulse(argThat(argument -> argument.x < 0 && argument.y == 0), any(Vector2.class), anyBoolean());
    }


    @Test
    void testApplyHorizontalImpulseRight() {
        when(mockedBody.getWorldCenter()).thenReturn(new Vector2(0,0));
        player.applyHorizontalImpulse(false, true);
        verify(mockedBody).applyLinearImpulse(argThat(argument -> argument.x > 0 && argument.y == 0), any(Vector2.class), anyBoolean());

    }

    @Test
    void testNotApplyHorizontalImpulse() {
        player.applyHorizontalImpulse(false, false);
        verify(mockedBody, never()).applyLinearImpulse(any(), any(), anyBoolean());
    }



}

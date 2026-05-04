package inf112.zoombiegeddon.model.entity.player;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import inf112.zoombiegeddon.events.GameEvent;
import inf112.zoombiegeddon.events.GameEventBus;
import inf112.zoombiegeddon.model.entity.AbstractEntityTest;
import inf112.zoombiegeddon.model.entity.EntityState;
import inf112.zoombiegeddon.model.entity.enemies.Enemy;
import inf112.zoombiegeddon.model.levels.LevelModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PlayerTest extends AbstractEntityTest {

    private Player player;

    @BeforeEach
    void beforeEach() {
        player = new Player(mockedWorld, spawnPos, 3);
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
    void testSetInGoalFalse() {
        GameEventBus.clear();
        int[] count = {0};
        GameEventBus.register(event -> {if (event == GameEvent.LEVEL_FINISHED) count[0]++;});
        player.setInGoal(false);
        assertEquals(0, count[0]);
        GameEventBus.clear();
    }

    @Test
    void testHitEnemy() {
        when(mockedBody.getWorldCenter()).thenReturn(new Vector2(0,0));
        Enemy enemy = new Enemy(mockedWorld, spawnPos, 3, 1.0f);

        //When the player does not hit enemy from above, the player should take damage
        player.hitEnemy(enemy, false);
        assertEquals(3, enemy.getHealth()); //Enemy should not take damage
        assertEquals(2, player.getHealth());

        //When the player hits the enemy from above, the player should not take damage
        player.hitEnemy(enemy, true);
        assertEquals(2, enemy.getHealth()); //Enemy should take damage
        assertEquals(2, player.getHealth());

        //Player should jump when hitting enemy from above
        verify(mockedBody).applyLinearImpulse(argThat(v -> v.x == 0 && v.y > 0), any(Vector2.class), anyBoolean());

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
    void testApplySpeedBoost() {
        player.applySpeedBoost(2f, 4f);
        assertEquals(2f, player.getActiveSpeedMultiplier());


        //Multiplier < 1 should be clamped to 1.0
        player.applySpeedBoost(0.5f, 4f);
        assertEquals(1.0f, player.getActiveSpeedMultiplier());

        //After player has had the boost for given duration, the speedMultiplier
        //becomes 1.0f again.
        player.applySpeedBoost(2f, 1f);
        player.update(1.2f);
        assertEquals(1.0f, player.getActiveSpeedMultiplier());
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

    @Test
    void takeDamageAllowsDamageExactlyWhenInvulnerabilityExpires() {
        player.takeDamage(1);   // hp = 2, hitTimer = 1.0f
        player.update(1.0f);   // hitTimer = 1.0f - 1.0f = 0.0f exactly
        player.takeDamage(1);  // hitTimer <= 0 → true → hp = 1
        assertEquals(1, player.getHealth());
    }

    @Test
    void healDoesNotExceedMaxHp() {
        player.heal(5);
        assertEquals(3, player.getHealth());
    }

    @Test
    void healWithZeroAmountDoesNotChangeHp() {
        player.takeDamage(1);  // hp = 2
        player.update(1.2f);
        player.heal(0);
        assertEquals(2, player.getHealth());
    }

    @Test
    void setInGoalTrueWhenAlreadyInGoalDoesNotFireEventAgain() {
        GameEventBus.clear();
        int[] count = {0};
        GameEventBus.register(e -> { if (e == GameEvent.LEVEL_FINISHED) count[0]++; });

        player.setInGoal(true);
        player.setInGoal(true);

        assertEquals(1, count[0]);
        GameEventBus.clear();
    }

    @Test
    void setInGoalCanBeResetToFalse() {
        player.setInGoal(true);
        player.setInGoal(false);
        assertFalse(player.inGoal());
    }

    @Test
    void speedBoostResetToOneMultiplierAtExactExpiryBoundary() {
        player.applySpeedBoost(2f, 1f);
        player.update(1.0f); // speedBoostRemaining = 1.0f - 1.0f = 0.0f → reset to 1.0f
        assertEquals(1.0f, player.getActiveSpeedMultiplier());
    }

    @Test
    void setGroundedFalseFromZeroContactsDoesNotBecomeGrounded() {
        player.setGrounded(false); // contactPoints: 0 → -1 → clamped to 0 → isGrounded = (0 > 0) = false
        assertFalse(player.isGrounded());
    }

    @Test
    void setGroundedRequiresBalancedTrueAndFalseCalls() {
        player.setGrounded(true);
        player.setGrounded(true);
        player.setGrounded(false);
        assertTrue(player.isGrounded()); // one contact still active
        player.setGrounded(false);
        assertFalse(player.isGrounded()); // all contacts released
    }

    @Test
    void getDefaultStateIsIdle() {
        assertEquals(EntityState.IDLE, player.getCurrentState());
    }


    @Test
    void testPlayerCanJumpWhenCoyoteTime() {
        when(mockedBody.getWorldCenter()).thenReturn(new Vector2(0,0));
        player.setGrounded(true);
        player.update(0.01f);
        player.setGrounded(false);
        player.jump();
        player.update(0.05f);
        verify(mockedBody).applyLinearImpulse(argThat(v -> v.x == 0 && v.y > 0), any(Vector2.class), anyBoolean());
    }

    @Test
    void testPlayerCanNotJumpWhenCoyoteTimerHasRanOut(){
        player.setGrounded(true);
        player.update(0.01f);
        player.setGrounded(false);
        player.update(0.2f);
        player.jump();
        player.update(0.01f);
        verify(mockedBody, never()).applyLinearImpulse(argThat(v -> v.x == 0 && v.y > 0), any(Vector2.class), anyBoolean());
    }

    @Test
    void testUpdateGravityScaleWhenJumpHeld() {
        player.setJumpHeld(true);
        when(mockedBody.getLinearVelocity()).thenReturn(new Vector2(0f, 2f));
        player.update(0.01f);
        verify(mockedBody).setGravityScale(1.5f);
    }

    @Test
    void testUpdateGravityScaleWhenJumpNotHeld() {
        player.setJumpHeld(false);
        when(mockedBody.getLinearVelocity()).thenReturn(new Vector2(0f, 2f));
        player.update(0.01f);
        verify(mockedBody).setGravityScale(5.0f);
    }

    @Test
    void testUpdateAnimationState() {
        player.setGrounded(true);
        when(mockedBody.getLinearVelocity()).thenReturn(new Vector2(2f, 0f));
        player.setRunning(false);
        player.update(0.01f);
        EntityState state = player.getCurrentState();
        assertEquals(EntityState.WALKING, state);
    }

    @Test
    void testUpdateAnimationStateWhenVelocityXIsLessThenVelocityThreshold() {
        player.setGrounded(true);
        when(mockedBody.getLinearVelocity()).thenReturn(new Vector2(-5f, 0f));
        player.update(0.01f);
        assertFalse(player.isFacingRight());
    }

    @Test
    void testPostMoveTransitionPlayerMoveStart() {
        GameEventBus.clear();
        int[] count = {0};
        GameEventBus.register(event -> {if (event == GameEvent.PLAYER_MOVE_START) count[0]++;});

        player.setGrounded(true);
        when(mockedBody.getLinearVelocity()).thenReturn(new Vector2(2f, 0f));
        player.update(0.01f);

        assertEquals(1, count[0]);
        GameEventBus.clear();
    }

    @Test
    void testPostMoveTransitionPlayerMoveStop() {
        GameEventBus.clear();
        int[] count = {0};
        GameEventBus.register(event -> {if (event == GameEvent.PLAYER_MOVE_STOP) count[0]++;});

        player.setGrounded(true);
        when(mockedBody.getLinearVelocity()).thenReturn(new Vector2(2f, 0f));
        player.update(0.01f);

        when(mockedBody.getLinearVelocity()).thenReturn(new Vector2(0f, 0f));
        player.update(0.01f);

        assertEquals(1, count[0]);
        GameEventBus.clear();
    }

    @Test
    void testPostRunTransitionsPlayerRunStart() {
        GameEventBus.clear();
        int[] count = {0};
        GameEventBus.register(event -> {if (event == GameEvent.PLAYER_RUN_START) count[0]++;});

        player.setGrounded(true);
        player.setRunning(true);
        when(mockedBody.getLinearVelocity()).thenReturn(new Vector2(2f, 0f));
        player.update(0.01f);

        assertEquals(1, count[0]);
        GameEventBus.clear();
    }

    @Test
    void testPostRunTransitionsPlayerRunStop() {
        GameEventBus.clear();
        int[] count = {0};
        GameEventBus.register(event -> {if (event == GameEvent.PLAYER_RUN_STOP) count[0]++;});

        player.setGrounded(true);
        player.setRunning(true);
        when(mockedBody.getLinearVelocity()).thenReturn(new Vector2(2f, 0f));
        player.update(0.01f);

        player.setRunning(false);
        player.update(0.01f);

        assertEquals(1, count[0]);
        GameEventBus.clear();
    }

    @Test
    void testPostRunTransitionsWhenContinueRunning() {
        //When wasRunningWhileMoving and runningNow is both true
        GameEventBus.clear();
        int[] count = {0};
        GameEventBus.register(event -> {if (event == GameEvent.PLAYER_RUN_START || event == GameEvent.PLAYER_RUN_STOP) count[0]++;});

        player.setGrounded(true);
        player.setRunning(true);
        when(mockedBody.getLinearVelocity()).thenReturn(new Vector2(2f, 0f));
        player.update(0.01f);
        player.update(0.01f);

        assertEquals(1, count[0]);
        GameEventBus.clear();

    }




}

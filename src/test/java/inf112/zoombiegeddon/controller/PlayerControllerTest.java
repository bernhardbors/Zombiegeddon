package inf112.zoombiegeddon.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import inf112.zoombiegeddon.AbstractLibGDXTest;
import inf112.zoombiegeddon.model.entity.player.IPlayer;
import inf112.zoombiegeddon.model.entity.PhysicsComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class PlayerControllerTest extends AbstractLibGDXTest {

    private IPlayer player;
    private PlayerController controller;

    @BeforeEach
    void beforeEach() {
        player = mock(IPlayer.class);
        controller = new PlayerController(player);
        PhysicsComponent physics = mock(PhysicsComponent.class);
        when(player.getPhysics()).thenReturn(physics);
        when(physics.getVelocity()).thenReturn(new Vector2(0, 0));
        when(player.getActiveSpeedMultiplier()).thenReturn(1.0f);
        Gdx.input = mock(Input.class);

    }

    @Test
    void testKeyDownSpace() {
        boolean keyDownWasPressed = controller.keyDown(Input.Keys.SPACE);
        verify(player).jump();
        assertTrue(keyDownWasPressed);
    }

    @Test
    void testKeyDownUp() {
        boolean keyDownWasPressed = controller.keyDown(Input.Keys.UP);
        verify(player).jump();
        assertTrue(keyDownWasPressed);

    }

    @Test
    void testKeyDownW() {
        boolean keyDownWasPressed = controller.keyDown(Input.Keys.W);
        verify(player).jump();
        assertTrue(keyDownWasPressed);

    }

    @Test
    void testKeyDownNotJumpingKey() {
        boolean keyDownWasPressed = controller.keyDown(Input.Keys.U);
        assertFalse(keyDownWasPressed);

    }

    @Test
    void testKeyUpSpace(){
        boolean keyUpWasPressed = controller.keyUp(Input.Keys.SPACE);
        assertTrue(keyUpWasPressed);
    }

    @Test
    void keyUpW() {
        boolean keyUpWasPressed = controller.keyUp(Input.Keys.W);
        assertTrue(keyUpWasPressed);
    }

    @Test
    void keyUpUp() {
        boolean keyUpWasPressed = controller.keyUp(Input.Keys.UP);
        assertTrue(keyUpWasPressed);
    }

    @Test
    void keyUpInvalidKey() {
        boolean keyUpWasPressed = controller.keyUp(Input.Keys.U);
        assertFalse(keyUpWasPressed);
    }

    @Test
    void testIsLeftPressedA() {
        when(Gdx.input.isKeyPressed(Input.Keys.A)).thenReturn(true);
        boolean result = controller.isLeftPressed();
        assertTrue(result);

    }
    @Test
    void testIsLeftPressedLeft() {
        when(Gdx.input.isKeyPressed(Input.Keys.LEFT)).thenReturn(true);
        boolean result = controller.isLeftPressed();
        assertTrue(result);
    }
    @Test
    void testIsRightPressedD() {
        when(Gdx.input.isKeyPressed(Input.Keys.D)).thenReturn(true);
        boolean result = controller.isRightPressed();
        assertTrue(result);
    }

    @Test
    void testIsRightPressedRight() {
        when(Gdx.input.isKeyPressed(Input.Keys.RIGHT)).thenReturn(true);
        boolean result = controller.isRightPressed();
        assertTrue(result);
    }

    @Test
    void testIsJumpPressedSpace() {
        when(Gdx.input.isKeyPressed(Input.Keys.SPACE)).thenReturn(true);
        boolean result = controller.isJumpPressed();
        assertTrue(result);
    }
    @Test
    void testIsJumpPressedW() {
        when(Gdx.input.isKeyPressed(Input.Keys.W)).thenReturn(true);
        boolean result = controller.isJumpPressed();
        assertTrue(result);
    }

    @Test
    void testIsJumpPressedUP() {
        when(Gdx.input.isKeyPressed(Input.Keys.UP)).thenReturn(true);
        boolean result = controller.isJumpPressed();
        assertTrue(result);
    }

    @Test
    void testHandlePlayerMovementLeft() {
        when(Gdx.input.isKeyPressed(Input.Keys.A)).thenReturn(true);
        controller.update(0.1f);
        verify(player).applyHorizontalImpulse(true, false);

    }

    @Test
    void testHandlePlayerMovementRight() {
        when(Gdx.input.isKeyPressed(Input.Keys.D)).thenReturn(true);
        controller.update(0.1f);
        verify(player).applyHorizontalImpulse(false, true);

    }

    @Test
    void testPlayerJumpOnlyGetsCalledOnce() {
        controller.keyDown(Input.Keys.UP);
        controller.keyDown(Input.Keys.SPACE);
        verify(player, times(1)).jump();
    }

    @Test
    void testPlayerCanJumpAgainAfterKeyUp(){
        when(Gdx.input.isKeyPressed(Input.Keys.SPACE)).thenReturn(false);
        controller.keyDown(Input.Keys.UP);
        controller.keyUp(Input.Keys.UP);

        controller.keyDown(Input.Keys.UP);
        verify(player, times(2)).jump();
    }

    @Test
    void testPlayerCanNotJumpAgainAfterWhenKeyIsPressed(){
        when(Gdx.input.isKeyPressed(Input.Keys.SPACE)).thenReturn(true);
        controller.keyDown(Input.Keys.UP);
        controller.keyUp(Input.Keys.UP);
        controller.keyDown(Input.Keys.UP);
        verify(player, times(1)).jump();
    }



}

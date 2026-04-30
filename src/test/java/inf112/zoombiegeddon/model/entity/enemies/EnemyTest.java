package inf112.zoombiegeddon.model.entity.enemies;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import inf112.zoombiegeddon.model.entity.AbstractEntityTest;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EnemyTest extends AbstractEntityTest {

    @Test
    void testEnemyTakeDamageAndDie() {
        Enemy enemy = new Enemy(mockedWorld, spawnPos, mockedAnims, 3, 1.0f);

        assertEquals(3, enemy.getHealth());
        assertFalse(enemy.isDead());

        enemy.takeDamage(2);
        assertEquals(1, enemy.getHealth());
        assertFalse(enemy.isDead());

        enemy.takeDamage(1);
        assertTrue(enemy.isDead());
    }

    @Test
    void testEnemyHeal() {
        Enemy enemy = new Enemy(mockedWorld, spawnPos, mockedAnims, 5, 1.0f);

        enemy.takeDamage(3);
        enemy.heal(2);
        assertEquals(4, enemy.getHealth());

        enemy.heal(10);
        assertEquals(5, enemy.getHealth());
    }

    @Test
    void testEnemyUpdatesDirectionOnUpdate() {
        Enemy enemy = new Enemy(mockedWorld, spawnPos, mockedAnims, 1, 2.0f);

        float dt = 0.1f;
        enemy.update(dt);

        verify(enemy.getPhysics().getBody(), atLeastOnce()).setLinearVelocity(anyFloat(), anyFloat());
    }

    @Test
    void testEnemyTurnsAroundWhenHittingWall() {
        Enemy enemy = new Enemy(mockedWorld, spawnPos, mockedAnims, 1, 2.0f);
        enemy.setDirection(1);
        enemy.setGrounded(true);

        doAnswer(invocation -> {
            RayCastCallback callback = invocation.getArgument(0);
            Vector2 start = invocation.getArgument(1);
            Vector2 end = invocation.getArgument(2);

            if (start.y == end.y) {
                Fixture wallFixture = mock(Fixture.class);
                when(wallFixture.isSensor()).thenReturn(false);
                when(wallFixture.getBody()).thenReturn(mock(Body.class));

                callback.reportRayFixture(wallFixture, new Vector2(), new Vector2(), 0.5f);
            } else {
                Fixture groundFixture = mock(Fixture.class);
                when(groundFixture.isSensor()).thenReturn(false);
                when(groundFixture.getUserData()).thenReturn("ground");
                callback.reportRayFixture(groundFixture, new Vector2(), new Vector2(), 0.5f);
            }
            return null;
        }).when(mockedWorld).rayCast(any(RayCastCallback.class), any(Vector2.class), any(Vector2.class));

        enemy.update(0.1f);

        verify(mockedBody).setLinearVelocity(eq(-2.0f), anyFloat());
    }

    @Test
    void testEnemyTurnsAroundWhenMissingLedge() {
        Enemy enemy = new Enemy(mockedWorld, spawnPos, mockedAnims, 1, 2.0f);
        enemy.setDirection(1);
        enemy.setGrounded(true);

        doAnswer(invocation -> {
            return null;
        }).when(mockedWorld).rayCast(any(RayCastCallback.class), any(Vector2.class), any(Vector2.class));

        enemy.update(0.1f);

        verify(mockedBody).setLinearVelocity(eq(-2.0f), anyFloat());
    }

    @Test
    void testEnemyWalksStraightWhenPathIsClear() {
        Enemy enemy = new Enemy(mockedWorld, spawnPos, mockedAnims, 1, 2.0f);
        enemy.setDirection(1);
        enemy.setGrounded(true);

        doAnswer(invocation -> {
            RayCastCallback callback = invocation.getArgument(0);
            Vector2 start = invocation.getArgument(1);
            Vector2 end = invocation.getArgument(2);

            if (start.x == end.x) {
                Fixture groundFixture = mock(Fixture.class);
                when(groundFixture.isSensor()).thenReturn(false);
                when(groundFixture.getUserData()).thenReturn("ground");
                callback.reportRayFixture(groundFixture, new Vector2(), new Vector2(), 0.5f);
            }
            return null;
        }).when(mockedWorld).rayCast(any(RayCastCallback.class), any(Vector2.class), any(Vector2.class));

        enemy.update(0.1f);

        verify(mockedBody).setLinearVelocity(eq(2.0f), anyFloat());
    }

    @Test
    void testSetAndGetPosition() {
        Enemy enemy = new Enemy(mockedWorld, spawnPos, mockedAnims, 3, 2.0f);

        Vector2 position = new Vector2(2f, 5f);
        when(mockedBody.getPosition()).thenReturn(position);

        enemy.setPosition(10f, 20f);

        assertEquals(10f, enemy.getPosition().x);
        assertEquals(20F, enemy.getPosition().y);
    }

    @Test
    void testEnemyHealCannotExceedMaxHp() {
        Enemy enemy = new Enemy(mockedWorld, spawnPos, mockedAnims, 5, 1.0f);
        enemy.takeDamage(2); // hp = 3
        enemy.heal(100);     // should clamp to maxHp = 5
        assertEquals(5, enemy.getHealth());
    }

    @Test
    void testEnemyHealAtMaxHpDoesNotChange() {
        Enemy enemy = new Enemy(mockedWorld, spawnPos, mockedAnims, 5, 1.0f);
        enemy.heal(3); // already at max
        assertEquals(5, enemy.getHealth());
    }
}
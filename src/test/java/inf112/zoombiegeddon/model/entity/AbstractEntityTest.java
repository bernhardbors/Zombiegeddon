package inf112.zoombiegeddon.model.entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import inf112.zoombiegeddon.AbstractLibGDXTest;
import inf112.zoombiegeddon.model.animations.AnimationHandler;
import org.junit.jupiter.api.BeforeEach;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class AbstractEntityTest extends AbstractLibGDXTest {
    protected World mockedWorld;
    protected Body mockedBody;
    protected Fixture mockedFixture;
    protected AnimationHandler mockedAnims;
    protected Vector2 spawnPos;

    @BeforeEach
    public void setUpPhysics() {
        mockedWorld = mock(World.class);
        mockedBody = mock(Body.class);
        mockedFixture = mock(Fixture.class);
        mockedAnims = mock(AnimationHandler.class);
        spawnPos = new Vector2(10, 10);

        when(mockedWorld.createBody(any(BodyDef.class))).thenReturn(mockedBody);
        when(mockedBody.createFixture(any(FixtureDef.class))).thenReturn(mockedFixture);
        when(mockedBody.createFixture(any(Shape.class), anyFloat())).thenReturn(mockedFixture);

        when(mockedBody.getPosition()).thenReturn(new Vector2(0, 0));
        when(mockedBody.getLinearVelocity()).thenReturn(new Vector2(0, 0));
        when(mockedBody.getWorld()).thenReturn(mockedWorld);

    }
}

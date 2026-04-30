package inf112.zoombiegeddon.utils;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import inf112.zoombiegeddon.AbstractLibGDXTest;
import inf112.zoombiegeddon.model.entity.IEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CameraUtilsTest extends AbstractLibGDXTest {

    private IEntity entity;
    private OrthographicCamera camera;

    @BeforeEach
    void beforeEach() {
        entity = mock(IEntity.class);
        camera = new OrthographicCamera();
        camera.position.y = 0;
        camera.position.x = 0;
        camera.position.z = 0;

    }

    @Test
    void testCameraDoesNotMoveWhenPlayerInDeadZone() {
        //Entity is inside the deadzone [-2, 2], so the camera should not move.
        //CameraUtils adds a vertical offset of +3.5, so this results in playerY = 0 in
        //CameraUtils.

        when(entity.getPosition()).thenReturn(new Vector2(1f, -3.5f));

        CameraUtils.updateCamera(entity, camera);

        assertEquals(0, camera.position.x);
        assertEquals(0, camera.position.y);

    }

    @Test
    void testCheckThatCameraMovesToRightWhenThePlayerMoves() {
        when(entity.getPosition()).thenReturn(new Vector2(3f, 0));

        CameraUtils.updateCamera(entity, camera);

        assertEquals(1, camera.position.x);
        assertEquals(1.5, camera.position.y);
    }

    @Test
    void testCheckThatCameraMovesToLeftWhenThePlayerMoves() {
        when(entity.getPosition()).thenReturn(new Vector2(-3f, -6f));

        CameraUtils.updateCamera(entity,camera);
        assertEquals(-1, camera.position.x);
        assertEquals(-0.5, camera.position.y);

    }

}

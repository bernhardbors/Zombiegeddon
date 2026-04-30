package inf112.zoombiegeddon.view.utils;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;

/**
 * Utility class for camera manipulation and behavior.
 * Provides specialized logic for following the player with specific
 * constraints like deadzones and vertical offsets.
 */
public final class CameraUtils {

    private static final float DEADZONE_WIDTH = 4f;
    private static final float DEADZONE_HEIGHT = 4f;
    private static final float VERTICAL_OFFSET = 3.5f;

    private CameraUtils() {}

    /**
     * Moves the camera to keep {@code position} within the deadzone,
     * then updates the camera's projection.
     *
     * @param position the target position to follow, typically the player's position
     * @param camera   the camera to update
     */
    public static void updateCamera(Vector2 position, OrthographicCamera camera) {

        float positionX = position.x;
        float positionY = position.y + VERTICAL_OFFSET;

        if (positionX < camera.position.x - DEADZONE_WIDTH / 2) {
            camera.position.x = positionX + DEADZONE_WIDTH / 2;
        } else if (positionX > camera.position.x + DEADZONE_WIDTH / 2) {
            camera.position.x = positionX - DEADZONE_WIDTH / 2;
        }

        if (positionY < camera.position.y - DEADZONE_HEIGHT / 2) {
            camera.position.y = positionY + DEADZONE_HEIGHT / 2;
        } else if (positionY > camera.position.y + DEADZONE_HEIGHT / 2) {
            camera.position.y = positionY - DEADZONE_HEIGHT / 2;
        }

        camera.update();
    }
}
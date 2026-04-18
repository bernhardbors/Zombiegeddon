package inf112.zoombiegeddon.utils;

import com.badlogic.gdx.graphics.OrthographicCamera;
import inf112.zoombiegeddon.model.entity.IEntity;

/**
 * Utility class for camera manipulation and behavior.
 * Provides specialized logic for following the player with specific
 * constraints like deadzones and vertical offsets.
 */
public class CameraUtils {

    private static final float DEADZONE_WIDTH = 4f;
    private static final float DEADZONE_HEIGHT = 4f;
    private static final float VERTICAL_OFFSET = 5.5f;
    /**
     * Updates the camera position to follow the player using a deadzone approach.
     * The camera only moves when the player exceeds the horizontal or vertical
     * boundaries (deadzones) defined within the method. This creates a smoother
     * following effect compared to a locked camera.
     *
     * @param entity The entity instance whose position the camera should track.
     * @param camera The {@link OrthographicCamera} to be updated.
     */
    public static void updateCamera(IEntity entity, OrthographicCamera camera) {

        float playerX = entity.getPosition().x;
        float playerY = entity.getPosition().y + VERTICAL_OFFSET;

        if (playerX < camera.position.x - DEADZONE_WIDTH / 2) {
            camera.position.x = playerX + DEADZONE_WIDTH / 2;
        } else if (playerX > camera.position.x + DEADZONE_WIDTH / 2) {
            camera.position.x = playerX - DEADZONE_WIDTH / 2;
        }

        if (playerY < camera.position.y - DEADZONE_HEIGHT / 2) {
            camera.position.y = playerY + DEADZONE_HEIGHT / 2;
        } else if (playerY > camera.position.y + DEADZONE_HEIGHT / 2) {
            camera.position.y = playerY - DEADZONE_HEIGHT / 2;
        }

        camera.update();
    }
}
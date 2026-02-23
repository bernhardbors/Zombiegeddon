package inf112.zoombiegeddon.model;

import com.badlogic.gdx.graphics.OrthographicCamera;

public class CameraUtil {

    public static void updateCamera(Player player, OrthographicCamera camera) {
        
        float DEADZONE_WIDTH = 4f;
        float DEADZONE_HEIGHT = 3f;
        float VERTICAL_OFFSET = 1.5f;

        float playerX = player.playerBody.getPosition().x;
        float playerY = player.playerBody.getPosition().y + VERTICAL_OFFSET;

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
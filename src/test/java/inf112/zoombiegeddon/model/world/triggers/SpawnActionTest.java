package inf112.zoombiegeddon.model.world.triggers;
import inf112.zoombiegeddon.model.entity.AbstractEntityTest;
import inf112.zoombiegeddon.model.entity.player.Player;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.verify;

class SpawnActionTest extends AbstractEntityTest {

    @Test
    void testSpawnActionSetsCorrectPosition() {
        SpawnAction spawnAction = new SpawnAction(10, 10);
        Player player = new Player(mockedWorld, spawnPos, 3);

        spawnAction.onTrigger(player);

        verify(mockedBody).setTransform(10.0f, 10.0f, 0.0f);
    }
}

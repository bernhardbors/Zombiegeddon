package inf112.zoombiegeddon.model.world.triggers;
import inf112.zoombiegeddon.model.entity.AbstractEntityTest;
import inf112.zoombiegeddon.model.entity.Player;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.verify;

public class SpawnActionTest extends AbstractEntityTest {

    private SpawnAction spawnAction;
    private Player player;

    @Test
    public void testSpawnActionSetsCorrectPosition() {
        spawnAction = new SpawnAction(10, 10);
        player = new Player(mockedWorld, spawnPos, mockedAnims, 3);

        spawnAction.onTrigger(player);

        verify(mockedBody).setTransform(10.0f, 10.0f, 0.0f);
    }
}

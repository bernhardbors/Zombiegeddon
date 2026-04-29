package inf112.zoombiegeddon;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import org.junit.jupiter.api.BeforeAll;


/**
 * Every test class that uses LibGDX should extend this class.
 */
public abstract class AbstractLibGDXTest {

    /**
     * Method that makes it possible to use parts of LibGDX in the tests
     */
    //Tatt utgangspunkt i expempelkode "FlockingTest.java"
    @BeforeAll
    static void beforeAll() {
        HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
        ApplicationListener listener = new ApplicationAdapter() {
        };
        new HeadlessApplication(listener, config);
        com.badlogic.gdx.physics.box2d.Box2D.init();
    }
}

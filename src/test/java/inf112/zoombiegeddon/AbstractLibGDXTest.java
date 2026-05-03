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
    //Based on example code "FlockingTest.java"
    //Link: https://git.app.uib.no/inf112/25v/inf112.25v.lectures/-/blob/main/src/test/java/inf112/skeleton/app/FlockingTest.java?ref_type=heads
    @BeforeAll
    static void beforeAll() {
        HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
        ApplicationListener listener = new ApplicationAdapter() {
        };
        new HeadlessApplication(listener, config);
        com.badlogic.gdx.physics.box2d.Box2D.init();
    }
}

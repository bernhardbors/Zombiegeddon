package inf112.zoombiegeddon;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.utils.Os;
import com.badlogic.gdx.utils.SharedLibraryLoader;
import inf112.zoombiegeddon.app.Main;
import org.junit.jupiter.api.Test;
import org.lwjgl.system.Configuration;
import org.mockito.MockedConstruction;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class MainAppCreationTest {

    @Test
    void testMainLaunchSequence() {

        try (var _ = mockStatic(SharedLibraryLoader.class);
             MockedConstruction<Lwjgl3Application> appMock = mockConstruction(Lwjgl3Application.class);
             MockedConstruction<Lwjgl3ApplicationConfiguration> cfgMock = mockConstruction(Lwjgl3ApplicationConfiguration.class)) {

            SharedLibraryLoader.os = Os.Windows;

            // Execute main
            Main.main(new String[]{});


            Lwjgl3ApplicationConfiguration usedCfg = cfgMock.constructed().get(0);
            verify(usedCfg).setTitle("Zoombiegeddon");
            verify(usedCfg).setWindowedMode(1280, 768);

            assertEquals(1, appMock.constructed().size(), "Lwjgl3Application should be initialized");
        }
    }

    @Test
    void testMacSpecificWorkaround() {
        try (var _ = mockStatic(SharedLibraryLoader.class);
             var _ = mockStatic(Configuration.class);
             var _ = mockConstruction(Lwjgl3Application.class)) {

            SharedLibraryLoader.os = Os.MacOsX;

            Main.main(new String[]{});

            assertEquals("glfw_async", Configuration.GLFW_LIBRARY_NAME.get(),
                    "GLFW_LIBRARY_NAME should be set to glfw_async on macOS");
        } finally {
            Configuration.GLFW_LIBRARY_NAME.set(null);
        }
    }
}
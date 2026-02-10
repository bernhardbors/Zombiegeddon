package inf112.skeleton.app;

import org.lwjgl.system.Configuration;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.utils.Os;
import com.badlogic.gdx.utils.SharedLibraryLoader;

public class Main {
	public static void main(String[] args) {
		// enable assertion checking by default
		Main.class.getClassLoader().setDefaultAssertionStatus(true);

		// Workaround for macOS problem – alternatively, we could start the JVM with
		// the -XstartOnFirstThread option.
		// See https://javadoc.lwjgl.org/org/lwjgl/glfw/package-summary.html for
		// explanation
		if (SharedLibraryLoader.os == Os.MacOsX) {
			Configuration.GLFW_LIBRARY_NAME.set("glfw_async");
		}

		Lwjgl3ApplicationConfiguration cfg = new Lwjgl3ApplicationConfiguration();
		cfg.setTitle("Zoombies");
		cfg.setWindowedMode(1280, 768);

		ApplicationListener appListener = new GameApp();

		// Start the application framework. The constructor call won't return until
		// Gdx.app.exit() is called.
		new Lwjgl3Application(appListener, cfg);
	}
}

package inf112.skeleton.app;

import org.lwjgl.system.Configuration;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.utils.Os;
import com.badlogic.gdx.utils.SharedLibraryLoader;

import inf112.skeleton._example.controller.AppController;

public class MvcMain {
	public static void main(String[] args) {
		// enable assertion checking by default
		MvcMain.class.getClassLoader().setDefaultAssertionStatus(true);

		// Workaround for Mac OS problem – alternatively, we could start the JVM with
		// the -XstartOnFirstThread option.
		// See https://javadoc.lwjgl.org/org/lwjgl/glfw/package-summary.html for
		// explanation
		if (SharedLibraryLoader.os == Os.MacOsX) {
			Configuration.GLFW_LIBRARY_NAME.set("glfw_async");
		}

		Lwjgl3ApplicationConfiguration cfg = new Lwjgl3ApplicationConfiguration();
		cfg.setTitle("MVC Example");
		cfg.setWindowedMode(1280, 768); // change default window size if this is too large or small
		// (there are lots of other interesting configuration options

		// This object is our application. It'll "listen" to events from
		// the LibGDX main loop.
		ApplicationListener appListener = new AppController(); // Replace this with your own

		// Start the application framework. The constructor call won't return until
		// Gdx.app.exit() is called.
		new Lwjgl3Application(appListener, cfg);
	}
}

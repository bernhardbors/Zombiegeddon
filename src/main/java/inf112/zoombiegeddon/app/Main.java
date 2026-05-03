package inf112.zoombiegeddon.app;

import org.lwjgl.system.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.utils.Os;
import com.badlogic.gdx.utils.SharedLibraryLoader;

/**
 * Launches the desktop version of the game.
 * Configures the application window and applies platform-specific setup
 * before starting the LibGDX application.
 */
public class Main {

	private static final Logger logger = LoggerFactory.getLogger(Main.class);

	/**
	 * Starts the game application.
	 *
	 * @param args Command-line arguments passed to the application.
	 */
	public static void main(String[] args) {
		// enable assertion checking by default
		Main.class.getClassLoader().setDefaultAssertionStatus(true);

		// Workaround for macOS problem – alternatively, we could start the JVM with
		// the -XstartOnFirstThread option.
		// See https://javadoc.lwjgl.org/org/lwjgl/glfw/package-summary.html for
		// explanation
		if (SharedLibraryLoader.os == Os.MacOsX) {
			Configuration.GLFW_LIBRARY_NAME.set("glfw_async");
			setMacIcon();
		}

		Lwjgl3ApplicationConfiguration cfg = new Lwjgl3ApplicationConfiguration();
		cfg.setTitle("Zoombiegeddon");
		cfg.setWindowedMode(1280, 768);

		ApplicationListener appListener = new GameApp();

		// Start the application framework. The constructor call won't return until
		// Gdx.app.exit() is called.
		new Lwjgl3Application(appListener, cfg);
	}

	/**
	 * Attempts to set the application icon on macOS systems that support taskbar icons.
	 */
	private static void setMacIcon() {
		if (java.awt.Taskbar.isTaskbarSupported()) {
			try {
				java.awt.Taskbar taskbar = java.awt.Taskbar.getTaskbar();

				java.net.URL iconURL = Main.class.getClassLoader().getResource("assets/sprites/appicons/icon_lg.png");
				logger.info("{}", iconURL);
				if (iconURL != null) {
					java.awt.Image image = java.awt.Toolkit.getDefaultToolkit().getImage(iconURL);
					taskbar.setIconImage(image);
				} else {
					logger.warn("Kunne ikke finne ikon-filen i resources!");
				}
			} catch (Exception e) {
				logger.error("Kunne ikke sette ikon-filen!", e);
			}
		}
	}
}

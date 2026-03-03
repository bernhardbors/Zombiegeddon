package inf112.zoombiegeddon.app;

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
			setMacIcon();
		}

		Lwjgl3ApplicationConfiguration cfg = new Lwjgl3ApplicationConfiguration();
		cfg.setTitle("Zoombiegeddon: Krill's Army of the Undead");
		cfg.setWindowedMode(1280, 768);

		ApplicationListener appListener = new GameApp();

		// Start the application framework. The constructor call won't return until
		// Gdx.app.exit() is called.
		new Lwjgl3Application(appListener, cfg);
	}

	private static void setMacIcon() {
		if (java.awt.Taskbar.isTaskbarSupported()) {
			try {
				java.awt.Taskbar taskbar = java.awt.Taskbar.getTaskbar();

				java.net.URL iconURL = Main.class.getClassLoader().getResource("assets/sprites/appicons/icon_lg.png");
				System.out.println(iconURL);
				if (iconURL != null) {
					java.awt.Image image = java.awt.Toolkit.getDefaultToolkit().getImage(iconURL);
					taskbar.setIconImage(image);
				} else {
					System.out.println("Kunne ikke finne ikon-filen i resources!");
				}
			} catch (Exception e) {
				// Ignorerer feil på systemer som ikke støtter Taskbar
			}
		}
	}
}

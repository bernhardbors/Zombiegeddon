package inf112.zoombiegeddon.view.screens;

import com.badlogic.gdx.Screen;

/**
 * Represents a screen in the game.
 *
 * <p>Extends LibGDX {@link Screen} and adds the requirement that all
 * game screens must be able to build or rebuild their UI/content
 * through {@link #buildScreen()}.</p>
 */
public interface IScreen extends Screen {

    /**
     * Builds or rebuilds the content of the screen.
     *
     * <p>This method is typically used when the screen is first shown or
     * when it needs to be updated after resizing or other layout changes.</p>
     */
    public void buildScreen();

}

package inf112.zoombiegeddon.view.screens.loading;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.ScreenUtils;
import inf112.zoombiegeddon.app.GameApp;
import inf112.zoombiegeddon.events.GameEvent;
import inf112.zoombiegeddon.events.GameEventBus;
import inf112.zoombiegeddon.view.screens.AbstractScreen;
import inf112.zoombiegeddon.view.screens.menus.MainMenuScreen;

/**
 * The SplashScreen is the first screen shown when the game starts.
 * It displays animated text presenting the project and
 * handles the transition to the Main Menu.
 */
public class SplashScreen extends AbstractScreen {

    private static final float FADE_IN_DURATION = 1.5f;
    private static final float FADE_OUT_DURATION = 1.5f;
    private static final float DELAY_DURATION = 1f;

    /**
     * Constructor for the SplashScreen.
     *
     * @param app The main application instance used to switch screens and access assets.
     */
    public SplashScreen(GameApp app) {
        super(app);
    }

    /**
     * Builds the screen layout and defines the animation sequence for the text.
     * Uses LibGDX Actions to control fading effects and text replacement.
     */
    @Override
    public void buildScreen() {
        clearTable();

        GameEventBus.post(GameEvent.MENU_STARTED);

        Runnable toMainMenu = () -> app.setScreen(new MainMenuScreen(app));

        final Label text = formatText("A- Laget Presenter", Color.WHITE, 2.0f);

        text.getColor().a = 0;
        text.addAction(Actions.sequence(
                Actions.fadeIn(FADE_IN_DURATION),
                Actions.delay(DELAY_DURATION),
                Actions.fadeOut(FADE_OUT_DURATION),
                Actions.run(() -> text.setText("Et INF112 Semester Prosjekt")),
                Actions.fadeIn(FADE_IN_DURATION),
                Actions.delay(DELAY_DURATION),
                Actions.fadeOut(FADE_OUT_DURATION),
                Actions.run(toMainMenu)));

        skipToMainMenu(toMainMenu);
    }

    /**
     * Adds a listener to the stage that allows the user to skip the
     * splash screen animations by pressing any key or clicking the mouse.
     *
     * @param toMainMenu The logic to execute for switching to the main menu.
     */
    private void skipToMainMenu(Runnable toMainMenu) {
        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                toMainMenu.run();
                return true;
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                toMainMenu.run();
                return true;
            }
        });
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        super.render(delta);
    }
}
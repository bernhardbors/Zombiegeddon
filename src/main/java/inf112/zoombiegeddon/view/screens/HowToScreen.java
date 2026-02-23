package inf112.zoombiegeddon.view.screens;

import com.badlogic.gdx.graphics.Color;
import inf112.zoombiegeddon.app.GameApp;
import inf112.zoombiegeddon.utils.UIUtils;

public class HowToScreen extends AbstractMenuScreen {

    public HowToScreen(GameApp app) {
        super(app);

    }

    @Override
    public void buildScreen() {
        super.buildScreen();

        DrawHeading("HOW TO PLAY THE GAME", Color.WHITE, 4);

        DrawHeading("Welcome to Zoombies, To play the game \n" +
                            "Press the buttons or somthing. I´m sure \n" +
                            "you will figure it out, :-) ", Color.WHITE, 2);

        UIUtils.addButton("Back", skin, table, buttonWidth, buttonHeight, () -> app.setScreen(new MainMenuScreen(app))).row();
    }
}

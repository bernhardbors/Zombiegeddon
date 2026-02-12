package inf112.skeleton.view.screens;

import com.badlogic.gdx.graphics.Color;
import inf112.skeleton.app.GameApp;
import inf112.skeleton.utils.UIUtils;

public class HowToScreen extends AbstractMenuScreen {

    HowToScreen(GameApp app) {
        super(app);

    }

    @Override
    public void buildScreen() {
        super.buildScreen();

        DrawHeading("HOW TO PLAY THE GAME", Color.WHITE, 4);

        DrawHeading("\"Welcome to Zoombies, To play the game \\n \" + \n" +
                            "\"Press the buttons or somthing. I´m sure \\n\" + \n" +
                            "\"you will figure it out, :-) \"", Color.WHITE, 2);

        UIUtils.addButton("Back", skin, table, buttonWidth, buttonHeight, () -> app.setScreen(new MainMenuScreen(app))).row();
    }
}

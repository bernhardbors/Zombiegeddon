package inf112.zoombiegeddon.view.screens;

import com.badlogic.gdx.graphics.Color;
import inf112.zoombiegeddon.app.GameApp;
import inf112.zoombiegeddon.utils.UIUtils;

public class HowToScreen extends AbstractScreen {

    public HowToScreen(GameApp app) {
        super(app);

    }

    @Override
    public void buildScreen() {
       clearTable();

        formatText("HOW TO PLAY", Color.WHITE, 2f);

        formatText(
                "Move: W, A, S, D or Arrow Keys\n" +
                "Jump: Space or Up\n" +
                "Sprint: Shift\n" +
                "Attack: K\n" +
                "Quit: Escape",
                Color.WHITE, 1f);

        UIUtils.addButton("Back", skin, table, buttonWidth, buttonHeight, () -> app.setScreen(new MainMenuScreen(app))).row();
    }
}

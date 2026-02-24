package inf112.zoombiegeddon.view.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import inf112.zoombiegeddon.app.GameApp;

public class SplashScreen extends AbstractScreen {

    private float displayTime = 0;
    private final float MAX_DISPLAY_TIME = 3.0f; // 3 sekunder

    public SplashScreen(GameApp app) {
        super(app);
    }

    @Override
    public void buildScreen() {
        // Vi tømmer tabellen (gjort i super, men greit å være sikker)
        table.clear();

        // 1. Hent logo/bilde fra assets
        // Tips: Bruk en sti som matcher resources-mappen din
        Texture logoTexture = new Texture("background/background1.png");
        Image logo = new Image(logoTexture);

        // 2. Legg logoen i tabellen (denne er sentrert pga table.setFillParent(true))
        table.add(logo).center();

        // Valgfritt: Legg til en tittel under logoen ved å bruke din egen metode
        // DrawHeading("ZOOMBIEGEDDON", Color.RED, 2);
    }

    @Override
    public void render(float delta) {
        // Bruker super for å tegne stage (logoen vår)
        super.render(delta);

        displayTime += delta;

        // Logikk for å bytte skjerm
        if (displayTime >= MAX_DISPLAY_TIME) {
            app.setScreen(new MainMenuScreen(app));
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        // Husk å dispose ting som ikke er i skin/stage hvis du lager nye her
    }
}
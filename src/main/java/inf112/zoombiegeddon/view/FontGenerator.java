package inf112.zoombiegeddon.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

/**
 * Generates and stores bitmap fonts used by the game UI.
 */
public class FontGenerator {
    public BitmapFont mainFont;
    public BitmapFont titleFont;


    /**
     * Creates the fonts used by the game from the given font file.
     *
     * @param filePath The path to the font file.
     */
    public void create(String filePath) {
        if (!Gdx.files.internal(filePath).exists()) {
            Gdx.app.error("FontGenerator", "Font file not found: " + filePath);
            return;
        }
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(filePath));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 24;
        mainFont = generator.generateFont(parameter);

        parameter.size = 74;
        parameter.borderWidth = 2;
        titleFont = generator.generateFont(parameter);
        generator.dispose();
    }

    /**
     * Releases the font resources used by this generator.
     */
    public void dispose() {
        if (mainFont != null) mainFont.dispose();
        if (titleFont != null) titleFont.dispose();
    }
}

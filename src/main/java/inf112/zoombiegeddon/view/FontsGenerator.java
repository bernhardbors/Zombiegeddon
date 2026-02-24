package inf112.zoombiegeddon.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class FontsGenerator {
    public BitmapFont mainFont;
    public  BitmapFont titleFont;

    //https://libgdxinfo.wordpress.com/basic-label/ 23.02.2026
    public void create() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/pixbobMiniLiteFont.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        //Buttons
        parameter.size = 24;
        mainFont = generator.generateFont(parameter);

        //Titles
        parameter.size = 30;
        parameter.borderWidth = 2;
        titleFont = generator.generateFont(parameter);
        generator.dispose();
    }

    public void dispose() {
        mainFont.dispose();
        titleFont.dispose();
    }
}

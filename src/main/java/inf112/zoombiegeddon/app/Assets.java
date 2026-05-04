package inf112.zoombiegeddon.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import inf112.zoombiegeddon.view.animations.AnimationHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class for handling all game resources.
 * Relieves responsibility from {@link GameApp} and gathers all resource logic in one place
 */
public class Assets {
    private final AssetManager manager;
    private BitmapFont font;
    private Skin skin;

    private AnimationHandler playerMaleAnimations;
    private AnimationHandler zombieAnimations;
    private AnimationHandler chonkerAnimations;

    private ShaderProgram pickupEffectShader;

    private static final Logger logger = LoggerFactory.getLogger(Assets.class);


    public Assets() {
        this.manager = new AssetManager();
    }

    /**
     * Starts asynchronous loading of the game's assets.
     * <p>
     * This method queues assets for loading, but does not guarantee that they
     * are ready to use immediately. Call {@link #finishLoading()} before using
     * assets that must be fully loaded.
     */
    public void load() {
        // Loading of UI and fonts
        manager.load(AssetConstants.UI_SKIN, Skin.class);
        manager.load(AssetConstants.FONT_PATH, BitmapFont.class);

        // Loading of player and enemies
        manager.load(AssetConstants.CHARACTER_MALE_SHEET, Texture.class);
        manager.load(AssetConstants.CHARACTER_FEMALE_SHEET, Texture.class);
        manager.load(AssetConstants.ZOMBIE_SHEET, Texture.class);
        manager.load(AssetConstants.CHONKER_SHEET, Texture.class);
        manager.load(AssetConstants.HEALTH_BAR, Texture.class);
        manager.load(AssetConstants.PICKUP_SPEED, Texture.class);
        manager.load(AssetConstants.PICKUP_HEAL, Texture.class);

        // Loading of backgrounds
        manager.load(AssetConstants.BG_SKY, Texture.class);
        manager.load(AssetConstants.BG_CITY, Texture.class);
        manager.load(AssetConstants.BG_L1, Texture.class);
        manager.load(AssetConstants.BG_L2, Texture.class);
        manager.load(AssetConstants.BG_GAME, Texture.class);
        manager.load(AssetConstants.BG_SNOW_1, Texture.class);
        manager.load(AssetConstants.BG_SNOW_2, Texture.class);
        manager.load(AssetConstants.BG_SNOW_3, Texture.class);
        manager.load(AssetConstants.BG_NATURE_1, Texture.class);
        manager.load(AssetConstants.BG_NATURE_2, Texture.class);
        manager.load(AssetConstants.BG_NATURE_3, Texture.class);
    }

    /**
     * Finishes asset loading and prepares loaded resources for use.
     * <p>
     * This method blocks until all queued assets are loaded. It also creates
     * animation handlers, applies the loaded font to the UI skin and compiles
     * the pickup effect shader.
     */
    public void finishLoading() {
        manager.finishLoading();
        skin = manager.get(AssetConstants.UI_SKIN, Skin.class);
        font = manager.get(AssetConstants.FONT_PATH, BitmapFont.class);

        playerMaleAnimations = new AnimationHandler(manager.get(AssetConstants.CHARACTER_MALE_SHEET, Texture.class), 32, 32);
        zombieAnimations = new AnimationHandler(manager.get(AssetConstants.ZOMBIE_SHEET, Texture.class), 32, 32);
        chonkerAnimations = new AnimationHandler(manager.get(AssetConstants.CHONKER_SHEET, Texture.class), 32, 32);

        skin.add("default-font", font, BitmapFont.class);
        skin.get(TextButton.TextButtonStyle.class).font = font;
        skin.get(Label.LabelStyle.class).font = font;

        String vertexShader = Gdx.files.internal(AssetConstants.SHADER_VERTEX).readString();
        String fragmentShader = Gdx.files.internal(AssetConstants.SHADER_FRAGMENT_PICKUP).readString();

        pickupEffectShader = new ShaderProgram(vertexShader, fragmentShader);

        if (!pickupEffectShader.isCompiled()) {
            logger.error("Error compiling shader: {}", pickupEffectShader.getLog());
            pickupEffectShader.dispose();
            pickupEffectShader = null;
        }

        for (BitmapFont.Glyph[] page : font.getData().glyphs) {
            if (page == null) continue;
            for (BitmapFont.Glyph g : page) {
                if (g != null) g.xoffset = 0;
            }
        }
    }

    public Skin getSkin() { return skin; }
    public BitmapFont getFont() { return font; }
    public AssetManager getManager() { return manager; }
    public ShaderProgram getPickupEffectShader() { return pickupEffectShader; }

    /**
     * Releases all loaded assets and disposable resources owned by this class.
     */
    public void dispose() {
        manager.dispose();
        if (pickupEffectShader != null) pickupEffectShader.dispose();
    }

    public AnimationHandler getPlayerMaleAnimations() { return playerMaleAnimations; }
    public AnimationHandler getZombieAnimations() { return zombieAnimations; }
    public AnimationHandler getChonkerAnimations() { return chonkerAnimations; }
}

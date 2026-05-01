package inf112.zoombiegeddon.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import inf112.zoombiegeddon.model.animations.AnimationHandler;
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
    private AnimationHandler playerFemaleAnimations;
    private AnimationHandler zombieAnimations;
    private AnimationHandler chonkerAnimations;

    private ShaderProgram pickupEffectShader;

    private static final Logger logger = LoggerFactory.getLogger(Assets.class);

    public Assets() {
        this.manager = new AssetManager();
    }

    /**
     * Called to start asynchronous loading of resources.
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
    }

    /**
     * Called when loading is completed (or to force synchronous loading).
     * Unpacks resources so they are ready to be used.
     */
    public void finishLoading() {
        manager.finishLoading();
        skin = manager.get(AssetConstants.UI_SKIN, Skin.class);
        font = manager.get(AssetConstants.FONT_PATH, BitmapFont.class);

        playerMaleAnimations = new AnimationHandler(manager.get(AssetConstants.CHARACTER_MALE_SHEET, Texture.class), 32, 32);
        playerFemaleAnimations = new AnimationHandler(manager.get(AssetConstants.CHARACTER_FEMALE_SHEET, Texture.class), 32, 32);
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
    }

    public Skin getSkin() { return skin; }
    public BitmapFont getFont() { return font; }
    public AssetManager getManager() { return manager; }
    public ShaderProgram getPickupEffectShader() { return pickupEffectShader; }

    public void dispose() {
        manager.dispose();
        if (pickupEffectShader != null) pickupEffectShader.dispose();
    }

    public AnimationHandler getPlayerMaleAnimations() { return playerMaleAnimations; }
    public AnimationHandler getZombieAnimations() { return zombieAnimations; }
    public AnimationHandler getChonkerAnimations() { return chonkerAnimations; }
}

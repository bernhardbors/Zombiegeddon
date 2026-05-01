package inf112.zoombiegeddon.app;

/**
 * Central registry of asset file paths used throughout the application.
 */
public class AssetConstants {

    private AssetConstants() {
    }

    public static final String UI_SKIN = "ui/skin/uiskin.json";
    public static final String FONT_PATH = "fonts/WhitePeaberryOutline.fnt";

    // Karakterer og animasjoner
    public static final String CHARACTER_MALE_SHEET = "assets/sprites/entity/player/playerMale_sheet.png";
    public static final String CHARACTER_FEMALE_SHEET = "assets/sprites/entity/player/playerFemale_sheet.png";
    public static final String ZOMBIE_SHEET = "assets/sprites/entity/enemies/zombie/Zombie-sheet.png";
    public static final String CHONKER_SHEET = "assets/sprites/entity/enemies/chonker/chonker_run.png";
    public static final String HEALTH_BAR = "assets/sprites/Sprites/HealthBar.png";
    public static final String PICKUP_SPEED = "assets/sprites/entity/pickups/pickup_speed.png";
    public static final String PICKUP_HEAL  = "assets/sprites/entity/pickups/pickup_heal.png";
    // Bakgrunner
    public static final String BG_SKY = "assets/backgrounds/CityBackgroundSky.png";
    public static final String BG_CITY = "assets/backgrounds/CityBackground.png";
    public static final String BG_L1 = "assets/backgrounds/CityBackgroundLayer1.png";
    public static final String BG_L2 = "assets/backgrounds/CityBackgroundLayer2.png";
    public static final String BG_GAME = "assets/backgrounds/bg.png";
    // Shaders
    public static final String SHADER_VERTEX = "assets/shaders/vertex.glsl";
    public static final String SHADER_FRAGMENT_PICKUP = "assets/shaders/pickup_effect_fragment.glsl";

    // Preferanser
    public static final String PREFS_NAME = "Prefs";
    public static final String PREF_WIDTH = "width";
    public static final String PREF_HEIGHT = "height";
    public static final String PREF_RES_STRING = "resString";
    public static final String PREF_FULLSCREEN = "fullscreen";

    //Story Images
    public static final String STORY_IMAGE_01 = "assets/Images/StoryImage1.png";
    public static final String STORY_IMAGE_02 = "assets/Images/StoryImage2.png";
    public static final String STORY_IMAGE_03 = "assets/Images/StoryImage3.png";
    public static final String STORY_IMAGE_04 = "assets/Images/StoryImage4.png";

}



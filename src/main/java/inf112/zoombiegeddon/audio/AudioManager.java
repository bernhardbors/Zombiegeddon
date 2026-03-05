package inf112.zoombiegeddon.audio;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import inf112.zoombiegeddon.events.GameEvent;
import inf112.zoombiegeddon.events.GameEventBus;

import java.util.Random;

/**
 * Manages audio playback and resource handling.
 * This class relies on an {@link AssetManager} to provide access to loaded sound effects
 * and manages individual playback logic, volume control, and music streaming.
 */
public class AudioManager implements AudioService, AudioListener{
    private static final String PREF_SFX_VOL = "audio_sfx_volume";
    private static final String PREF_MUTED = "audio_muted";

    private final AssetManager assets;
    private final Preferences prefs;
    private final Random random = new Random();

    private float sfxVolume;
    private boolean muted;

    /**
     * Constructs a new AudioManager with preference persistence.
     * @param assets The central manager responsible for asset lifecycle.
     * @param prefs  The preferences instance used to save/load audio settings.
     */
    public AudioManager(AssetManager assets, Preferences prefs) {
        this.assets = assets;
        this.prefs = prefs;

        this.sfxVolume = clamp01(prefs.getFloat(PREF_SFX_VOL, 1.0f));
        this.muted = prefs.getBoolean(PREF_MUTED, false);

        GameEventBus.register(this);
    }

    /**
     * Constructs a new AudioManager without persistence.
     * @param assets The central manager responsible for asset lifecycle.
     */
    public AudioManager(AssetManager assets) {
        this.assets = assets;
        this.prefs = null;
        this.sfxVolume = 1.0f;
        this.muted = false;

        GameEventBus.register(this);
    }

    @Override
    public void playSfx(Sfx sfx) {
        if (muted || sfx == null || !assets.isLoaded(sfx.path(), Sound.class)) return;
        Sound sound = assets.get(sfx.path(), Sound.class);
        sound.play(sfxVolume);
    }

    @Override
    public void playRandom(Sfx... options) {
        if (muted || options == null || options.length == 0) return;
        playSfx(options[random.nextInt(options.length)]);
    }

    @Override
    public void setSfxVolume(float volume01) {
        this.sfxVolume = clamp01(volume01);
        save();
    }

    @Override
    public void setMuted(boolean muted) {
        this.muted = muted;
        save();
    }

    @Override
    public float getSfxVolume() {
        return sfxVolume;
    }

    @Override
    public boolean isMuted() {
        return muted;
    }

    @Override
    public void onEvent(GameEvent event) {
        switch (event) {
            case PLAYER_JUMP:
                playRandom(Sfx.JUMP1, Sfx.JUMP2);
                break;
            case UI_BUTTON_CLICK:
                playSfx(Sfx.BUTTON_CLICK);
                break;
            default:
                break;
        }
    }
    /**
     * Disposes of the background music. Note: AssetManager handles Sound disposal.
     */
    public void dispose() {
        GameEventBus.unregister(this);
    }

    private void save() {
        if (prefs == null) return;
        prefs.putFloat(PREF_SFX_VOL, sfxVolume);
        prefs.putBoolean(PREF_MUTED, muted);
        prefs.flush();
    }

    private static float clamp01(float v) {
        return Math.max(0f, Math.min(1f, v));
    }
}

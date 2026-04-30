package inf112.zoombiegeddon.audio;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import inf112.zoombiegeddon.events.GameEvent;
import inf112.zoombiegeddon.events.GameEventBus;
import inf112.zoombiegeddon.events.IEventListener;
import java.util.Random;

/**
 * Manages audio playback and resource handling.
 * This class relies on an {@link AssetManager} to provide access to loaded sound effects
 * and manages individual playback logic, volume control, and music streaming.
 */
public class AudioManager implements IAudioService, IEventListener {
    private static final String PREF_SFX_VOL = "audio_sfx_volume";
    private static final String PREF_MUTED = "audio_muted";


    private static final String PREF_MUSIC_VOL = "audio_music_volume";

    private Music currentMusic;
    private MusicTrack currentTrack;
    private float musicVolume;

    private final AssetManager assets;
    private final Preferences prefs;
    private final Random random = new Random();
    private boolean footstepsLooping = false;

    private float sfxVolume;
    private boolean muted;
    private boolean runningFootsteps = false;

    /**
     * Constructs a new AudioManager with preference persistence.
     * @param assets The central manager responsible for asset lifecycle.
     * @param prefs  The preferences instance used to save/load audio settings.
     */
    public AudioManager(AssetManager assets, Preferences prefs) {
        this.assets = assets;
        this.prefs = prefs;
        this.musicVolume = clamp01(prefs.getFloat(PREF_MUSIC_VOL, 1.0f));

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
        this.musicVolume = 1.0f;
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

        if (currentMusic != null) {
            currentMusic.setVolume(muted ? 0f : musicVolume);
        }

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
    public long loopSfx(Sfx sfx) {
        if (muted) return -1L;
        if (sfx == null) return -1L;

        // Make sure the Sound is loaded
        if (!assets.isLoaded(sfx.path(), Sound.class)) return -1L;

        Sound sound = assets.get(sfx.path(), Sound.class);
        return sound.loop(sfxVolume);
    }

    @Override
    public void stopSfx(Sfx sfx) {
        if (sfx == null) return;
        if (!assets.isLoaded(sfx.path(), Sound.class)) return;
        assets.get(sfx.path(), Sound.class).stop();
    }

    /**
     * Starts looping the normal footsteps sound effect.
     * Does nothing if that loop is already active.
     */
    private void startFootstepsLoop() {
        if (footstepsLooping && !runningFootsteps) return;

        stopFootstepsLoop();
        loopSfx(Sfx.FOOTSTEPS);
        footstepsLooping = true;
        runningFootsteps = false;
    }

    /**
     * Starts looping the running footsteps sound effect.
     * Does nothing if that loop is already active.
     */
    private void startRunningFootstepsLoop() {
        if (footstepsLooping && runningFootsteps) return;

        stopFootstepsLoop();
        loopSfx(Sfx.FOOTSTEPS_RUN);
        footstepsLooping = true;
        runningFootsteps = true;
    }

    /**
     * Stops any currently active footsteps loop.
     */
    private void stopFootstepsLoop() {
        if (!footstepsLooping) return;

        stopSfx(Sfx.FOOTSTEPS);
        stopSfx(Sfx.FOOTSTEPS_RUN);
        footstepsLooping = false;
        runningFootsteps = false;
    }

    /**
     * Handles game events and plays matching audio feedback when relevant.
     *
     * @param event The game event to respond to.
     */
    @Override
    public void onEvent(GameEvent event) {
        switch (event) {
            case PLAYER_JUMP:
                playRandom(Sfx.JUMP1, Sfx.JUMP2);
                break;
            case UI_BUTTON_CLICK:
                playSfx(Sfx.BUTTON_CLICK);
                break;
            case PICKUP_COLLECTED:
                playSfx(Sfx.SPEED);
                break;
            case PLAYER_MOVE_START:
                startFootstepsLoop();
                break;
            case PLAYER_RUN_START:
                startRunningFootstepsLoop();
                break;
            case PLAYER_RUN_STOP:
                startFootstepsLoop();
                break;
            case PLAYER_MOVE_STOP:
               stopFootstepsLoop();
               break;
            case PLAYER_DEATH:
                stopFootstepsLoop();
                playSfx(Sfx.PLAYER_DEATH);
                break;
            case LEVEL_FINISHED:
                stopFootstepsLoop();
                playSfx(Sfx.VICTORY);
                break;
            case TOGGLE_PAUSE:
                stopFootstepsLoop();
                break;
            case PICKUP_HEALTH:
                playSfx(Sfx.HEAL_PICKUP);
                break;
            case PICKUP_SPEED:
                playSfx(Sfx.SPEED);
                break;
            default:
                break;
        }
    }

    @Override
    public void playMusic(MusicTrack track, boolean loop) {
        if (track == null) return;
        if (!assets.isLoaded(track.path(), Music.class)) return;

        if (currentTrack == track && currentMusic != null) return;

        stopMusic();

        currentMusic = assets.get(track.path(), Music.class);
        currentMusic.setLooping(loop);
        currentMusic.setVolume(muted ? 0f : musicVolume);
        currentMusic.play();
        currentTrack = track;
    }

    @Override
    public void stopMusic() {
        if (currentMusic != null) {
            currentMusic.stop();
            currentMusic = null;
            currentTrack = null;
        }
    }

    @Override
    public void setMusicVolume(float volume01) {
        this.musicVolume = clamp01(volume01);
        if (currentMusic != null) {
            currentMusic.setVolume(muted ? 0f : musicVolume);
        }
        save();
    }

    @Override
    public float getMusicVolume() {
        return musicVolume;
    }

    /**
     * Disposes of the background music. Note: AssetManager handles Sound disposal.
     */
    public void dispose() {
        GameEventBus.unregister(this);
    }

    /**
     * Saves current audio settings if preferences are available.
     */
    private void save() {
        if (prefs == null) return;
        prefs.putFloat(PREF_SFX_VOL, sfxVolume);
        prefs.putFloat(PREF_MUSIC_VOL, musicVolume);
        prefs.putBoolean(PREF_MUTED, muted);
        prefs.flush();

    }

    /**
     * Clamps a value to the range from 0.0 to 1.0.
     *
     * @param v The value to clamp.
     * @return The clamped value.
     */
    private static float clamp01(float v) {
        return Math.clamp(v, 0, 1);
    }
}

package inf112.zoombiegeddon.audio;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import inf112.zoombiegeddon.events.GameEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AudioManagerTest {

    private AssetManager assetManager;
    private AudioManager audioManager;
    private Sound sound;
    private Music music;
    private Preferences prefs;
    private AudioManager audioManagerWithPrefs;


    @BeforeEach
    void beforeEach() {
        assetManager = mock(AssetManager.class);
        sound = mock(Sound.class);
        audioManager = new AudioManager(assetManager);
        music = mock(Music.class);
        prefs = mock(Preferences.class);
        audioManagerWithPrefs = new AudioManager(assetManager, prefs);

    }

    @Test
    void testSetAndGetSFXVolume() {
        audioManager.setSfxVolume(1.0f);
        assertEquals(1.0f, audioManager.getSfxVolume());

        //Volume can not be higher than 1.0f
        audioManager.setSfxVolume(1.2f);
        assertEquals(1.0f, audioManager.getSfxVolume());

        //Volume can not be lower than 0.
        audioManager.setSfxVolume(-0.2f);
        assertEquals(0f, audioManager.getSfxVolume());

    }

    @Test
    void testSetAndGetMusicVolume() {
        audioManager.setMusicVolume(1.0f);
        assertEquals(1.0f, audioManager.getMusicVolume());

        audioManager.setMusicVolume(1.2f);
        assertEquals(1.0f, audioManager.getMusicVolume());

        audioManager.setMusicVolume(-0.2f);
        assertEquals(0f, audioManager.getMusicVolume());
    }

    @Test
    void testSetMuted() {
        audioManager.setMuted(true);
        assertTrue(audioManager.isMuted());

        audioManager.setMuted(false);
        assertFalse(audioManager.isMuted());
    }

    @Test
    void testSetVolumeToZeroWhenMuted() {
        MusicTrack track = MusicTrack.GAMEPLAY;

        when(assetManager.isLoaded(track.path(), Music.class)).thenReturn(true);
        when(assetManager.get(track.path(), Music.class)).thenReturn(music);

        audioManager.setMusicVolume(0.5f);
        audioManager.playMusic(track, true);
        audioManager.setMuted(true);

        verify(music, times(1)).setVolume(0f);
    }

    @Test
    void testSetVolumeBackToNormalAfterUnMuted() {
        MusicTrack track = MusicTrack.GAMEPLAY;

        when(assetManager.isLoaded(track.path(), Music.class)).thenReturn(true);
        when(assetManager.get(track.path(), Music.class)).thenReturn(music);

        audioManager.setMusicVolume(0.5f);
        audioManager.playMusic(track, true);
        audioManager.setMuted(true);

        audioManager.setMuted(false);

        verify(music, atLeastOnce()).setVolume(0.5f);
    }

    @Test
    void testNotPlaySfxWhenMuted() {
        //Sfx should not be played when muted
        Sfx sfx = Sfx.FOOTSTEPS;

        when(assetManager.isLoaded(sfx.path(), Sound.class)).thenReturn(true);
        when(assetManager.get(sfx.path(), Sound.class)).thenReturn(sound);

        audioManager.setMuted(true);
        audioManager.playSfx(sfx);

        verify(sound, never()).play(anyFloat());

    }

    @Test
    void testPlaySfxWhenNotMuted() {
        //Sfx should be played when the game is not muted
        Sfx sfx = Sfx.FOOTSTEPS;

        when(assetManager.isLoaded(sfx.path(), Sound.class)).thenReturn(true);
        when(assetManager.get(sfx.path(), Sound.class)).thenReturn(sound);

        audioManager.setSfxVolume(0.5f);
        audioManager.playSfx(sfx);
        verify(sound, times(1)).play(0.5f);
    }

    @Test
    void testNotPlaySfxWhenNotLoaded() {
        //When the sfx is not loaded, .play() should never be called.
        Sfx sfx = Sfx.FOOTSTEPS;

        when(assetManager.isLoaded(sfx.path(), Sound.class)).thenReturn(false);

        audioManager.playSfx(sfx);
        verify(sound, never()).play(anyFloat());

    }

    @Test
    void testNotPlaySfxWhenNull() {
        //When sfx is null, .play() should never be called.
        audioManager.playSfx(null);
        verify(sound, never()).play(anyFloat());
    }

    @Test
    void testStopSfxWhenLoaded() {
        //When sfx is loaded, stopSfx() should call .stop() on the sound.
        Sfx sfx = Sfx.FOOTSTEPS;

        when(assetManager.isLoaded(sfx.path(), Sound.class)).thenReturn(true);
        when(assetManager.get(sfx.path(), Sound.class)).thenReturn(sound);

        audioManager.stopSfx(sfx);
        verify(sound, atLeastOnce()).stop();
    }

    @Test
    void testStopSfxReturnsWhenSfxIsNull(){
        //When sfx is null, .stop() should never be called.
        audioManager.stopSfx(null);
        verify(sound, never()).stop();
    }

    @Test
    void testStopSfxReturnsWhenNotLoaded() {
        //When sfx is not loaded, .stop() should never be called.
        Sfx sfx = Sfx.FOOTSTEPS;

        when(assetManager.isLoaded(sfx.path(), Sound.class)).thenReturn(false);

        audioManager.stopSfx(sfx);
        verify(sound, never()).stop();
    }

    @Test
    void testPlayMusicWhenMuted() {

        MusicTrack track = MusicTrack.GAMEPLAY;

        when(assetManager.isLoaded(track.path(), Music.class)).thenReturn(true);
        when(assetManager.get(track.path(), Music.class)).thenReturn(music);

        audioManager.setMuted(true);

        audioManager.playMusic(track, true);

        verify(music, atLeastOnce()).setLooping(true);
        verify(music, atLeastOnce()).setVolume(0f);
        verify(music, atLeastOnce()).play();
    }

    @Test
    void testStopMusic() {
        MusicTrack track = MusicTrack.GAMEPLAY;
        when(assetManager.isLoaded(track.path(), Music.class)).thenReturn(true);
        when(assetManager.get(track.path(), Music.class)).thenReturn(music);

        audioManager.playMusic(track, true);

        audioManager.stopMusic();

        verify(music, atLeastOnce()).stop();
    }

    @Test
    void testOnEventPlayerJump() {
        GameEvent event = GameEvent.PLAYER_JUMP;
        Sfx sfx1 = Sfx.JUMP1;
        Sfx sfx2 = Sfx.JUMP2;

        when(assetManager.isLoaded(sfx1.path(), Sound.class)).thenReturn(true);
        when(assetManager.get(sfx1.path(), Sound.class)).thenReturn(sound);

        when(assetManager.isLoaded(sfx2.path(), Sound.class)).thenReturn(true);
        when(assetManager.get(sfx2.path(), Sound.class)).thenReturn(sound);


        audioManager.onEvent(event);
        verify(sound, atLeastOnce()).play(anyFloat());

    }

    @Test
    void testOnEventButtonClick() {
        GameEvent event = GameEvent.UI_BUTTON_CLICK;

        Sfx sfx = Sfx.BUTTON_CLICK;

        when(assetManager.isLoaded(sfx.path(), Sound.class)).thenReturn(true);
        when(assetManager.get(sfx.path(), Sound.class)).thenReturn(sound);

        audioManager.onEvent(event);
        verify(sound, atLeastOnce()).play(anyFloat());
    }

    @Test
    void testOnEventPlayerMoveStart() {
        GameEvent event = GameEvent.PLAYER_MOVE_START;

        Sfx sfx = Sfx.FOOTSTEPS;

        when(assetManager.isLoaded(sfx.path(), Sound.class)).thenReturn(true);
        when(assetManager.get(sfx.path(), Sound.class)).thenReturn(sound);

        audioManager.onEvent(event);

        verify(sound, atLeastOnce()).loop(anyFloat());

    }

    @Test
    void testOnEventPlayerRunStart() {
        GameEvent event = GameEvent.PLAYER_RUN_START;

        Sfx sfx = Sfx.FOOTSTEPS_RUN;

        when(assetManager.isLoaded(sfx.path(), Sound.class)).thenReturn(true);
        when(assetManager.get(sfx.path(), Sound.class)).thenReturn(sound);

        audioManager.onEvent(event);
        verify(sound, atLeastOnce()).loop(anyFloat());
    }

    @Test
    void testOnEventPlayerRunStop() {
        GameEvent event = GameEvent.PLAYER_RUN_STOP;

        Sfx sfx = Sfx.FOOTSTEPS;

        when(assetManager.isLoaded(sfx.path(), Sound.class)).thenReturn(true);
        when(assetManager.get(sfx.path(), Sound.class)).thenReturn(sound);

        audioManager.onEvent(event);
        verify(sound, atLeastOnce()).loop(anyFloat());
    }

    @Test
    void testOnEventPlayerMoveStop() {
        GameEvent event1 = GameEvent.PLAYER_MOVE_START;
        GameEvent event = GameEvent.PLAYER_MOVE_STOP;

        Sfx sfx1 = Sfx.FOOTSTEPS;
        Sfx sfx2 = Sfx.FOOTSTEPS_RUN;



        when(assetManager.isLoaded(sfx1.path(), Sound.class)).thenReturn(true);
        when(assetManager.get(sfx1.path(), Sound.class)).thenReturn(sound);

        when(assetManager.isLoaded(sfx2.path(), Sound.class)).thenReturn(true);
        when(assetManager.get(sfx2.path(), Sound.class)).thenReturn(sound);

        audioManager.onEvent(event1);
        audioManager.onEvent(event);
        verify(sound, atLeastOnce()).stop();

    }

    @Test
    void testOnEventPickupCollected() {
        GameEvent event = GameEvent.PICKUP_COLLECTED;
        Sfx sfx = Sfx.SPEED;

        when(assetManager.isLoaded(sfx.path(), Sound.class)).thenReturn(true);
        when(assetManager.get(sfx.path(), Sound.class)).thenReturn(sound);

        audioManager.onEvent(event);
        verify(sound, times(1)).play(anyFloat());
    }

    @Test
    void testLoopSfxReturnsMinus1LWhenMuted() {
        Sfx sfx = Sfx.FOOTSTEPS;

        when(assetManager.isLoaded(sfx.path(), Sound.class)).thenReturn(true);
        when(assetManager.get(sfx.path(), Sound.class)).thenReturn(sound);

        audioManager.setMuted(true);
        long muted = audioManager.loopSfx(sfx);
        assertEquals(-1L, muted);
    }

    @Test
    void testWhenSfxIsNullLoopSfxReturnsMinus1(){
        long muted = audioManager.loopSfx(null);
        assertEquals(-1L, muted);
    }

    @Test
    void testWhenSfxISNotLoadedLoopSfxReturnsNull() {
        Sfx sfx = Sfx.FOOTSTEPS;
        when(assetManager.isLoaded(sfx.path(), Sound.class)).thenReturn(false);

        long muted = audioManager.loopSfx(sfx);
        assertEquals(-1L,muted );


    }

    @Test
    void testSavePrefs() {
        audioManagerWithPrefs.setMuted(true);

        verify(prefs, atLeastOnce()).putFloat(anyString(), anyFloat());
        verify(prefs, atLeastOnce()).putBoolean(anyString(), anyBoolean());
        verify(prefs, atLeastOnce()).flush();
    }


}

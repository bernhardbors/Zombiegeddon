package inf112.zoombiegeddon.audio;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import inf112.zoombiegeddon.events.GameEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.ArgumentMatchers.anyString;
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
        music = mock(Music.class);
        prefs = mock(Preferences.class);

        audioManager = new AudioManager(assetManager);
        audioManagerWithPrefs = new AudioManager(assetManager, prefs);
    }

    @Test
    void testSetAndGetSfxVolume() {
        audioManager.setSfxVolume(1.0f);
        assertEquals(1.0f, audioManager.getSfxVolume());

        audioManager.setSfxVolume(1.2f);
        assertEquals(1.0f, audioManager.getSfxVolume());

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

        verify(music, atLeastOnce()).setVolume(0f);
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
        Sfx sfx = Sfx.FOOTSTEPS;

        when(assetManager.isLoaded(sfx.path(), Sound.class)).thenReturn(true);
        when(assetManager.get(sfx.path(), Sound.class)).thenReturn(sound);

        audioManager.setMuted(true);
        audioManager.playSfx(sfx);

        verify(sound, never()).play(anyFloat());
    }

    @Test
    void testPlaySfxWhenNotMuted() {
        Sfx sfx = Sfx.FOOTSTEPS;

        when(assetManager.isLoaded(sfx.path(), Sound.class)).thenReturn(true);
        when(assetManager.get(sfx.path(), Sound.class)).thenReturn(sound);

        audioManager.setSfxVolume(0.5f);
        audioManager.playSfx(sfx);

        verify(sound, times(1)).play(0.5f);
    }

    @Test
    void testNotPlaySfxWhenNotLoaded() {
        Sfx sfx = Sfx.FOOTSTEPS;

        when(assetManager.isLoaded(sfx.path(), Sound.class)).thenReturn(false);

        audioManager.playSfx(sfx);

        verify(sound, never()).play(anyFloat());
    }

    @Test
    void testNotPlaySfxWhenNull() {
        audioManager.playSfx(null);
        verify(sound, never()).play(anyFloat());
    }

    @Test
    void testStopSfxWhenLoaded() {
        Sfx sfx = Sfx.FOOTSTEPS;

        when(assetManager.isLoaded(sfx.path(), Sound.class)).thenReturn(true);
        when(assetManager.get(sfx.path(), Sound.class)).thenReturn(sound);

        audioManager.stopSfx(sfx);

        verify(sound, atLeastOnce()).stop();
    }

    @Test
    void testStopSfxReturnsWhenSfxIsNull() {
        audioManager.stopSfx(null);
        verify(sound, never()).stop();
    }

    @Test
    void testStopSfxReturnsWhenNotLoaded() {
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
    void testOnEventPlayerJumpStopsFootstepsAndPlaysJumpSound() {
        when(assetManager.isLoaded(Sfx.FOOTSTEPS.path(), Sound.class)).thenReturn(true);
        when(assetManager.get(Sfx.FOOTSTEPS.path(), Sound.class)).thenReturn(sound);

        when(assetManager.isLoaded(Sfx.FOOTSTEPS_RUN.path(), Sound.class)).thenReturn(true);
        when(assetManager.get(Sfx.FOOTSTEPS_RUN.path(), Sound.class)).thenReturn(sound);

        when(assetManager.isLoaded(Sfx.JUMP1.path(), Sound.class)).thenReturn(true);
        when(assetManager.get(Sfx.JUMP1.path(), Sound.class)).thenReturn(sound);

        when(assetManager.isLoaded(Sfx.JUMP2.path(), Sound.class)).thenReturn(true);
        when(assetManager.get(Sfx.JUMP2.path(), Sound.class)).thenReturn(sound);

        audioManager.onEvent(GameEvent.PLAYER_MOVE_START);
        audioManager.onEvent(GameEvent.PLAYER_JUMP);

        verify(sound, atLeastOnce()).stop();
        verify(sound, atLeastOnce()).play(anyFloat());
    }

    @Test
    void testOnEventButtonClick() {
        Sfx sfx = Sfx.BUTTON_CLICK;

        when(assetManager.isLoaded(sfx.path(), Sound.class)).thenReturn(true);
        when(assetManager.get(sfx.path(), Sound.class)).thenReturn(sound);

        audioManager.onEvent(GameEvent.UI_BUTTON_CLICK);

        verify(sound, atLeastOnce()).play(anyFloat());
    }

    @Test
    void testOnEventPlayerMoveStart() {
        when(assetManager.isLoaded(Sfx.FOOTSTEPS.path(), Sound.class)).thenReturn(true);
        when(assetManager.get(Sfx.FOOTSTEPS.path(), Sound.class)).thenReturn(sound);

        when(assetManager.isLoaded(MusicTrack.GAMEPLAY.path(), Music.class)).thenReturn(true);
        when(assetManager.get(MusicTrack.GAMEPLAY.path(), Music.class)).thenReturn(music);

        audioManager.onEvent(GameEvent.PLAYER_MOVE_START);

        verify(sound, atLeastOnce()).loop(anyFloat());
        verify(music, atLeastOnce()).play();
    }

    @Test
    void testOnEventPlayerRunStart() {
        when(assetManager.isLoaded(Sfx.FOOTSTEPS_RUN.path(), Sound.class)).thenReturn(true);
        when(assetManager.get(Sfx.FOOTSTEPS_RUN.path(), Sound.class)).thenReturn(sound);

        audioManager.onEvent(GameEvent.PLAYER_RUN_START);

        verify(sound, atLeastOnce()).loop(anyFloat());
    }

    @Test
    void testOnEventPlayerRunStopStopsFootsteps() {
        when(assetManager.isLoaded(Sfx.FOOTSTEPS.path(), Sound.class)).thenReturn(true);
        when(assetManager.get(Sfx.FOOTSTEPS.path(), Sound.class)).thenReturn(sound);

        when(assetManager.isLoaded(Sfx.FOOTSTEPS_RUN.path(), Sound.class)).thenReturn(true);
        when(assetManager.get(Sfx.FOOTSTEPS_RUN.path(), Sound.class)).thenReturn(sound);

        audioManager.onEvent(GameEvent.PLAYER_RUN_START);
        audioManager.onEvent(GameEvent.PLAYER_RUN_STOP);

        verify(sound, atLeastOnce()).stop();
    }

    @Test
    void testOnEventPlayerMoveStop() {
        when(assetManager.isLoaded(Sfx.FOOTSTEPS.path(), Sound.class)).thenReturn(true);
        when(assetManager.get(Sfx.FOOTSTEPS.path(), Sound.class)).thenReturn(sound);

        when(assetManager.isLoaded(Sfx.FOOTSTEPS_RUN.path(), Sound.class)).thenReturn(true);
        when(assetManager.get(Sfx.FOOTSTEPS_RUN.path(), Sound.class)).thenReturn(sound);

        audioManager.onEvent(GameEvent.PLAYER_MOVE_START);
        audioManager.onEvent(GameEvent.PLAYER_MOVE_STOP);

        verify(sound, atLeastOnce()).stop();
    }

    @Test
    void testOnEventPickupCollected() {
        Sfx sfx = Sfx.SPEED;

        when(assetManager.isLoaded(sfx.path(), Sound.class)).thenReturn(true);
        when(assetManager.get(sfx.path(), Sound.class)).thenReturn(sound);

        audioManager.onEvent(GameEvent.PICKUP_COLLECTED);

        verify(sound, times(1)).play(anyFloat());
    }

    @Test
    void testLoopSfxReturnsMinus1LWhenMuted() {
        Sfx sfx = Sfx.FOOTSTEPS;

        when(assetManager.isLoaded(sfx.path(), Sound.class)).thenReturn(true);
        when(assetManager.get(sfx.path(), Sound.class)).thenReturn(sound);

        audioManager.setMuted(true);
        long result = audioManager.loopSfx(sfx);

        assertEquals(-1L, result);
    }

    @Test
    void testWhenSfxIsNullLoopSfxReturnsMinus1() {
        long result = audioManager.loopSfx(null);
        assertEquals(-1L, result);
    }

    @Test
    void testWhenSfxIsNotLoadedLoopSfxReturnsMinus1() {
        Sfx sfx = Sfx.FOOTSTEPS;

        when(assetManager.isLoaded(sfx.path(), Sound.class)).thenReturn(false);

        long result = audioManager.loopSfx(sfx);

        assertEquals(-1L, result);
    }

    @Test
    void testSavePrefs() {
        audioManagerWithPrefs.setMuted(true);

        verify(prefs, atLeastOnce()).putFloat(anyString(), anyFloat());
        verify(prefs, atLeastOnce()).putBoolean(anyString(), anyBoolean());
        verify(prefs, atLeastOnce()).flush();
    }
}
package inf112.zoombiegeddon.audio;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import inf112.zoombiegeddon.events.GameEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.Mockito.*;

class AudioManagerTest {

    private AssetManager assetManager;
    private AudioManager audioManager;
    private AudioManager audioManagerWithPrefs;
    private Sound sound;
    private Music music;
    private Preferences prefs;

    @BeforeEach
    void beforeEach() {
        assetManager = mock(AssetManager.class);
        sound = mock(Sound.class);
        music = mock(Music.class);
        prefs = mock(Preferences.class);

        when(prefs.getFloat("audio_music_volume", 0.3f)).thenReturn(0.3f);
        when(prefs.getFloat("audio_sfx_volume", 1.0f)).thenReturn(1.0f);
        when(prefs.getBoolean("audio_muted", false)).thenReturn(false);

        audioManager = new AudioManager(assetManager);
        audioManagerWithPrefs = new AudioManager(assetManager, prefs);
    }

    @Test
    void testDefaultMusicVolumeWithoutPrefsIsPointThree() {
        assertEquals(0.3f, audioManager.getMusicVolume(), 0.0001f);
    }

    @Test
    void testMusicVolumeLoadedFromPrefsDefaultsToPointThree() {
        assertEquals(0.3f, audioManagerWithPrefs.getMusicVolume(), 0.0001f);
    }

    @Test
    void testSetAndGetSfxVolume() {
        audioManager.setSfxVolume(1.0f);
        assertEquals(1.0f, audioManager.getSfxVolume(), 0.0001f);

        audioManager.setSfxVolume(1.2f);
        assertEquals(1.0f, audioManager.getSfxVolume(), 0.0001f);

        audioManager.setSfxVolume(-0.2f);
        assertEquals(0f, audioManager.getSfxVolume(), 0.0001f);
    }

    @Test
    void testSetAndGetMusicVolumeCapsAtPointThree() {
        audioManager.setMusicVolume(0.2f);
        assertEquals(0.2f, audioManager.getMusicVolume(), 0.0001f);

        audioManager.setMusicVolume(1.0f);
        assertEquals(0.3f, audioManager.getMusicVolume(), 0.0001f);

        audioManager.setMusicVolume(-0.2f);
        assertEquals(0f, audioManager.getMusicVolume(), 0.0001f);
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

        audioManager.setMusicVolume(0.3f);
        audioManager.playMusic(track, true);
        audioManager.setMuted(true);

        verify(music, atLeastOnce()).setVolume(0f);
    }

    @Test
    void testSetVolumeBackToNormalAfterUnMuted() {
        MusicTrack track = MusicTrack.GAMEPLAY;

        when(assetManager.isLoaded(track.path(), Music.class)).thenReturn(true);
        when(assetManager.get(track.path(), Music.class)).thenReturn(music);

        audioManager.setMusicVolume(0.3f);
        audioManager.playMusic(track, true);
        audioManager.setMuted(true);
        audioManager.setMuted(false);

        verify(music, atLeastOnce()).setVolume(0.3f);
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
    void testPlayMusicWhenNotMutedUsesPointThreeVolume() {
        MusicTrack track = MusicTrack.GAMEPLAY;

        when(assetManager.isLoaded(track.path(), Music.class)).thenReturn(true);
        when(assetManager.get(track.path(), Music.class)).thenReturn(music);

        audioManager.playMusic(track, true);

        verify(music).setLooping(true);
        verify(music).setVolume(0.3f);
        verify(music).play();
    }

    @Test
    void testPlayMusicReturnsWhenTrackIsNull() {
        audioManager.playMusic(null, true);
        verifyNoInteractions(music);
    }

    @Test
    void testPlayMusicReturnsWhenTrackNotLoaded() {
        MusicTrack track = MusicTrack.GAMEPLAY;
        when(assetManager.isLoaded(track.path(), Music.class)).thenReturn(false);

        audioManager.playMusic(track, true);

        verify(assetManager, never()).get(track.path(), Music.class);
        verifyNoInteractions(music);
    }

    @Test
    void testPlayMusicSameTrackDoesNotRestart() {
        MusicTrack track = MusicTrack.GAMEPLAY;

        when(assetManager.isLoaded(track.path(), Music.class)).thenReturn(true);
        when(assetManager.get(track.path(), Music.class)).thenReturn(music);

        audioManager.playMusic(track, true);
        audioManager.playMusic(track, true);

        verify(music, times(1)).play();
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
    void testOnEventPickupHealth() {
        Sfx sfx = Sfx.HEAL_PICKUP;

        when(assetManager.isLoaded(sfx.path(), Sound.class)).thenReturn(true);
        when(assetManager.get(sfx.path(), Sound.class)).thenReturn(sound);

        audioManager.onEvent(GameEvent.PICKUP_HEALTH);

        verify(sound, times(1)).play(anyFloat());
    }

    @Test
    void testOnEventPickupSpeed() {
        Sfx sfx = Sfx.SPEED;

        when(assetManager.isLoaded(sfx.path(), Sound.class)).thenReturn(true);
        when(assetManager.get(sfx.path(), Sound.class)).thenReturn(sound);

        audioManager.onEvent(GameEvent.PICKUP_SPEED);

        verify(sound, times(1)).play(anyFloat());
    }

    @Test
    void testOnEventEnemyKilled() {
        Sfx sfx = Sfx.ENEMY_KILLED;

        when(assetManager.isLoaded(sfx.path(), Sound.class)).thenReturn(true);
        when(assetManager.get(sfx.path(), Sound.class)).thenReturn(sound);

        audioManager.onEvent(GameEvent.ENEMY_KILLED);

        verify(sound, times(1)).play(anyFloat());
    }

    @Test
    void testOnEventPlayerDeathStopsFootstepsAndPlaysDeathSound() {
        when(assetManager.isLoaded(Sfx.FOOTSTEPS.path(), Sound.class)).thenReturn(true);
        when(assetManager.get(Sfx.FOOTSTEPS.path(), Sound.class)).thenReturn(sound);

        when(assetManager.isLoaded(Sfx.FOOTSTEPS_RUN.path(), Sound.class)).thenReturn(true);
        when(assetManager.get(Sfx.FOOTSTEPS_RUN.path(), Sound.class)).thenReturn(sound);

        when(assetManager.isLoaded(Sfx.PLAYER_DEATH.path(), Sound.class)).thenReturn(true);
        when(assetManager.get(Sfx.PLAYER_DEATH.path(), Sound.class)).thenReturn(sound);

        audioManager.onEvent(GameEvent.PLAYER_MOVE_START);
        audioManager.onEvent(GameEvent.PLAYER_DEATH);

        verify(sound, atLeastOnce()).stop();
        verify(sound, atLeastOnce()).play(anyFloat());
    }

    @Test
    void testOnEventLevelFinishedStopsFootstepsStopsMusicAndPlaysVictorySound() {
        when(assetManager.isLoaded(Sfx.FOOTSTEPS.path(), Sound.class)).thenReturn(true);
        when(assetManager.get(Sfx.FOOTSTEPS.path(), Sound.class)).thenReturn(sound);

        when(assetManager.isLoaded(Sfx.FOOTSTEPS_RUN.path(), Sound.class)).thenReturn(true);
        when(assetManager.get(Sfx.FOOTSTEPS_RUN.path(), Sound.class)).thenReturn(sound);

        when(assetManager.isLoaded(Sfx.VICTORY.path(), Sound.class)).thenReturn(true);
        when(assetManager.get(Sfx.VICTORY.path(), Sound.class)).thenReturn(sound);

        when(assetManager.isLoaded(MusicTrack.GAMEPLAY.path(), Music.class)).thenReturn(true);
        when(assetManager.get(MusicTrack.GAMEPLAY.path(), Music.class)).thenReturn(music);

        audioManager.onEvent(GameEvent.PLAYER_MOVE_START);
        audioManager.onEvent(GameEvent.LEVEL_FINISHED);

        verify(sound, atLeastOnce()).stop();
        verify(sound, atLeastOnce()).play(anyFloat());
        verify(music, atLeastOnce()).stop();
    }

    @Test
    void testOnEventTogglePauseStopsFootsteps() {
        when(assetManager.isLoaded(Sfx.FOOTSTEPS.path(), Sound.class)).thenReturn(true);
        when(assetManager.get(Sfx.FOOTSTEPS.path(), Sound.class)).thenReturn(sound);

        when(assetManager.isLoaded(Sfx.FOOTSTEPS_RUN.path(), Sound.class)).thenReturn(true);
        when(assetManager.get(Sfx.FOOTSTEPS_RUN.path(), Sound.class)).thenReturn(sound);

        audioManager.onEvent(GameEvent.PLAYER_MOVE_START);
        audioManager.onEvent(GameEvent.TOGGLE_PAUSE);

        verify(sound, atLeastOnce()).stop();
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
    void testLoopSfxReturnsLoopIdWhenLoadedAndNotMuted() {
        Sfx sfx = Sfx.FOOTSTEPS;

        when(assetManager.isLoaded(sfx.path(), Sound.class)).thenReturn(true);
        when(assetManager.get(sfx.path(), Sound.class)).thenReturn(sound);
        when(sound.loop(1.0f)).thenReturn(42L);

        long result = audioManager.loopSfx(sfx);

        assertEquals(42L, result);
    }

    @Test
    void testSetMutesSavePrefs() {
        audioManagerWithPrefs.setMuted(true);

        verify(prefs).putFloat("audio_sfx_volume", 1.0f);
        verify(prefs).putFloat("audio_music_volume", 0.3f);
        verify(prefs).putBoolean("audio_muted", true);
        verify(prefs).flush();
    }

    @Test
    void testSetSfxVolumeSavesPrefs() {
        audioManagerWithPrefs.setSfxVolume(0.5f);

        verify(prefs).putFloat("audio_sfx_volume", 0.5f);
        verify(prefs).putFloat("audio_music_volume", 0.3f);
        verify(prefs).putBoolean("audio_muted", false);
        verify(prefs).flush();
    }

    @Test
    void testSetMusicSavesPrefs() {
        audioManagerWithPrefs.setMusicVolume(0.2f);

        verify(prefs).putFloat("audio_sfx_volume", 1.0f);
        verify(prefs).putFloat("audio_music_volume", 0.2f);
        verify(prefs).putBoolean("audio_muted", false);
        verify(prefs).flush();
    }

    @Test
    void testPlayerMoveStartTwiceDoesNotRestartFootstepsLoop() {
        Sfx sfx = Sfx.FOOTSTEPS;
        MusicTrack track = MusicTrack.GAMEPLAY;

        when(assetManager.isLoaded(sfx.path(), Sound.class)).thenReturn(true);
        when(assetManager.get(sfx.path(), Sound.class)).thenReturn(sound);

        when(assetManager.isLoaded(track.path(), Music.class)).thenReturn(true);
        when(assetManager.get(track.path(), Music.class)).thenReturn(music);

        audioManager.onEvent(GameEvent.PLAYER_MOVE_START);
        audioManager.onEvent(GameEvent.PLAYER_MOVE_START);
        verify(sound, times(1)).loop(anyFloat());
        verify(music, times(1)).play();
    }

    @Test
    void testPlayerMoveStartAfterPlayerRunStartStopsRunningSound() {
        Sfx sfxWalk = Sfx.FOOTSTEPS;
        Sfx sfxRun = Sfx.FOOTSTEPS_RUN;

        MusicTrack track = MusicTrack.GAMEPLAY;

        when(assetManager.isLoaded(sfxWalk.path(), Sound.class)).thenReturn(true);
        when(assetManager.get(sfxWalk.path(), Sound.class)).thenReturn(sound);

        when(assetManager.isLoaded(sfxRun.path(), Sound.class)).thenReturn(true);
        when(assetManager.get(sfxRun.path(), Sound.class)).thenReturn(sound);

        when(assetManager.isLoaded(track.path(), Music.class)).thenReturn(true);
        when(assetManager.get(track.path(), Music.class)).thenReturn(music);

        audioManager.onEvent(GameEvent.PLAYER_RUN_START);
        audioManager.onEvent(GameEvent.PLAYER_MOVE_START);

        verify(sound, atLeastOnce()).stop();
        verify(sound, times(2)).loop(anyFloat());

    }

    @Test
    void testPlayerRunStartTwiceDoesNotRestartFootstepsLoop() {
        Sfx sfx = Sfx.FOOTSTEPS_RUN;
        MusicTrack track = MusicTrack.GAMEPLAY;

        when(assetManager.isLoaded(sfx.path(), Sound.class)).thenReturn(true);
        when(assetManager.get(sfx.path(), Sound.class)).thenReturn(sound);

        when(assetManager.isLoaded(track.path(), Music.class)).thenReturn(true);
        when(assetManager.get(track.path(), Music.class)).thenReturn(music);

        audioManager.onEvent(GameEvent.PLAYER_RUN_START);
        audioManager.onEvent(GameEvent.PLAYER_RUN_START);
        verify(sound, times(1)).loop(anyFloat());
    }

    @Test
    void testPlayerRunStartAfterPlayerMoveStartStopsRunningSound() {
        Sfx sfxWalk = Sfx.FOOTSTEPS;
        Sfx sfxRun = Sfx.FOOTSTEPS_RUN;

        MusicTrack track = MusicTrack.GAMEPLAY;

        when(assetManager.isLoaded(sfxWalk.path(), Sound.class)).thenReturn(true);
        when(assetManager.get(sfxWalk.path(), Sound.class)).thenReturn(sound);

        when(assetManager.isLoaded(sfxRun.path(), Sound.class)).thenReturn(true);
        when(assetManager.get(sfxRun.path(), Sound.class)).thenReturn(sound);

        when(assetManager.isLoaded(track.path(), Music.class)).thenReturn(true);
        when(assetManager.get(track.path(), Music.class)).thenReturn(music);

        audioManager.onEvent(GameEvent.PLAYER_MOVE_START);
        audioManager.onEvent(GameEvent.PLAYER_RUN_START);

        verify(sound, atLeastOnce()).stop();
        verify(sound, times(2)).loop(anyFloat());

    }







}
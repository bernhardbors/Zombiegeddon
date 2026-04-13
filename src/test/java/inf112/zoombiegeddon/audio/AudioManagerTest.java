package inf112.zoombiegeddon.audio;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AudioManagerTest {

    private AssetManager assetManager;
    private AudioManager audioManager;
    private Sound sound;
    private Music music;


    @BeforeEach
    void setUpMusic() {
        assetManager = mock(AssetManager.class);
        sound = mock(Sound.class);
        audioManager = new AudioManager(assetManager);
        music = mock(Music.class);

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
        verify(sound, atLeastOnce()).play(0.5f);
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
    void testPlayMusic() {

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
    void stopMusic() {
        MusicTrack track = MusicTrack.GAMEPLAY;
        when(assetManager.isLoaded(track.path(), Music.class)).thenReturn(true);
        when(assetManager.get(track.path(), Music.class)).thenReturn(music);

        audioManager.playMusic(track, true);

        audioManager.stopMusic();

        verify(music, atLeastOnce()).stop();
    }
}

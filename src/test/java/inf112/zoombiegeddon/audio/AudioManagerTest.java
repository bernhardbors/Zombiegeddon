package inf112.zoombiegeddon.audio;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AudioManagerTest {

    AssetManager assetManager = new AssetManager();

    AudioManager manager = new AudioManager(assetManager);


    @Test
    void testSetAndGetSFXVolume() {
        manager.setSfxVolume(1.0f);
        assertEquals(1.0f, manager.getSfxVolume());

        //Volume can not be higher than 1.0f
        manager.setSfxVolume(1.2f);
        assertEquals(1.0f, manager.getSfxVolume());

        //Volume can not be lower than 0.
        manager.setSfxVolume(-0.2f);
        assertEquals(0f, manager.getSfxVolume());

    }

    @Test
    void testSetAndGetMusicVolume() {
        manager.setMusicVolume(1.0f);
        assertEquals(1.0f, manager.getMusicVolume());

        manager.setMusicVolume(1.2f);
        assertEquals(1.0f, manager.getMusicVolume());

        manager.setMusicVolume(-0.2f);
        assertEquals(0f, manager.getMusicVolume());
    }

    @Test
    void testSetMuted() {


    }
}

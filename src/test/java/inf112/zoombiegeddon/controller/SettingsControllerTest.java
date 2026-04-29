package inf112.zoombiegeddon.controller;

import com.badlogic.gdx.Preferences;
import inf112.zoombiegeddon.AbstractLibGDXTest;
import inf112.zoombiegeddon.app.AssetConstants;
import inf112.zoombiegeddon.app.GameApp;
import inf112.zoombiegeddon.audio.IAudioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class SettingsControllerTest extends AbstractLibGDXTest {

    private GameApp app;
    private Preferences preferences;
    private IAudioService audio;
    private SettingsController controller;

    @BeforeEach
    void beforeEach() {
        app = mock(GameApp.class);
        preferences = mock(Preferences.class);
        audio = mock(IAudioService.class);
        when(app.getPrefs()).thenReturn(preferences);
        when(app.getAudio()).thenReturn(audio);
        controller = new SettingsController(app);
    }

    @Test
    void testApplyResolution(){
        controller.applyResolution("1920 x 1080");
        verify(preferences).putInteger(AssetConstants.PREF_HEIGHT, 1080);
        verify(preferences).putInteger(AssetConstants.PREF_WIDTH, 1920);
        verify(preferences).putString(AssetConstants.PREF_RES_STRING, "1920 x 1080");
        verify(preferences).flush();
    }

    @Test
    void testToggleFullScreenTrue() {
        controller.toggleFullscreen(true);
        verify(preferences).putBoolean(AssetConstants.PREF_FULLSCREEN, true);
        verify(preferences).flush();
    }

    @Test
    void testToggleFullScreenFalse() {
        controller.toggleFullscreen(false);
        verify(preferences).putBoolean(AssetConstants.PREF_FULLSCREEN, false);
        verify(preferences).getInteger(AssetConstants.PREF_WIDTH, 1280);
        verify(preferences).getInteger(AssetConstants.PREF_HEIGHT, 720);

    }
}

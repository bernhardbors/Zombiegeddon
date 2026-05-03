package inf112.zoombiegeddon.model.animations;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import inf112.zoombiegeddon.model.entity.EntityState;
import inf112.zoombiegeddon.AbstractLibGDXTest;
import inf112.zoombiegeddon.view.animations.AnimationHandler;
import inf112.zoombiegeddon.view.animations.IAnimationLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class AnimationHandlerTest extends AbstractLibGDXTest {

    private AnimationHandler animationHandler;

    private IAnimationLoader fullLoader() {
        return (w, h) -> {
            TextureRegion[][] frames = new TextureRegion[5][3];
            for (int r = 0; r < 5; r++)
                for (int c = 0; c < 3; c++)
                    frames[r][c] = new TextureRegion();
            return frames;
        };
    }

    private IAnimationLoader limitedLoader(int rows) {
        return (w, h) -> {
            TextureRegion[][] frames = new TextureRegion[rows][3];
            for (int r = 0; r < rows; r++)
                for (int c = 0; c < 3; c++)
                    frames[r][c] = mock(TextureRegion.class);
            return frames;
        };
    }

    @BeforeEach
    void setUp() {
        animationHandler = new AnimationHandler(fullLoader(), 32, 32);
    }

    @Test
    void testAllStatesExist() {
        assertNotNull(animationHandler.getAnimations().get(EntityState.IDLE));
        assertNotNull(animationHandler.getAnimations().get(EntityState.WALKING));
        assertNotNull(animationHandler.getAnimations().get(EntityState.RUNNING));
        assertNotNull(animationHandler.getAnimations().get(EntityState.JUMPING));
        assertNotNull(animationHandler.getAnimations().get(EntityState.FALLING));
        assertNotNull(animationHandler.getAnimations().get(EntityState.PUNCHING));
    }

    @Test
    void testIdleAnimationHasThreeFrames() {
        assertEquals(3, animationHandler.getAnimations().get(EntityState.IDLE).getKeyFrames().length);
    }

    @Test
    void testGetFrameReturnsPunchingFrame() {
        assertNotNull(animationHandler.getFrame(EntityState.PUNCHING, 0.1f, true));
    }

    @Test
    void testMissingStateReturnsFallbackToIdle() {
        AnimationHandler handler = new AnimationHandler(limitedLoader(1), 32, 32);
        assertNull(handler.getAnimations().get(EntityState.PUNCHING));

        TextureRegion result = handler.getFrame(EntityState.PUNCHING, 0.1f, true);
        TextureRegion expectedIdle = handler.getAnimations().get(EntityState.IDLE).getKeyFrame(0.1f, true);
        assertEquals(expectedIdle, result);
    }

    @Test
    void testFacingRightIsNotFlipped() {
        TextureRegion frame = animationHandler.getFrame(EntityState.IDLE, 0.1f, true);
        assertFalse(frame.isFlipX());
    }

    @Test
    void testJumpingDoesNotLoop() {
        AnimationHandler handler = new AnimationHandler(fullLoader(), 32, 32);
        TextureRegion lastFrame = handler.getAnimations()
                .get(EntityState.JUMPING).getKeyFrame(10.0f, false);
        assertEquals(lastFrame, handler.getFrame(EntityState.JUMPING, 10.0f, true));
    }
    @Test
    void testFallingDoesNotLoop() {
        TextureRegion lastFrame = animationHandler.getAnimations()
                .get(EntityState.FALLING).getKeyFrame(10.0f, false);
        assertEquals(lastFrame, animationHandler.getFrame(EntityState.FALLING, 10.0f, true));
    }
}
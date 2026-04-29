package inf112.zoombiegeddon.model.animations;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import inf112.zoombiegeddon.model.entity.EntityState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class AnimationHandlerTest {
    private AnimationHandler animationHandler;
    private IAnimationLoader mockLoader;
    private TextureRegion[][] frames;

    @BeforeEach
    void setUp() {
        this.mockLoader = (width, height) -> {
        this.frames = new TextureRegion[5][3];
        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 3; c++) {
                frames[r][c] = new TextureRegion();
            }
        }
        return frames;
    };
        this.animationHandler = new AnimationHandler(mockLoader, 32, 32);
}

    @Test
    void testGetFrameReturnsIdleAsFallback() {
        TextureRegion frame = animationHandler.getFrame(EntityState.PUNCHING, 0.1f, true);
        assertNotNull(frame, "Should return frame even if state animation is missing");
    }

    @Test
    void testGetFrameFlipping() {
        TextureRegion frame = animationHandler.getFrame(EntityState.IDLE, 0.1f, true);
        assertFalse(frame.isFlipX(), "Should not be flipped when facingRight is true");
    }
    @Test
    void testAnimationHandlerStates(){
         assertNotNull(animationHandler.getAnimations().get(EntityState.IDLE), "Idle animation should exist");
         assertNotNull(animationHandler.getAnimations().get(EntityState.WALKING), "Walking animation should exist");
         assertNotNull(animationHandler.getAnimations().get(EntityState.RUNNING), "Running animation should exist");
        assertNotNull(animationHandler.getAnimations().get(EntityState.FALLING), "Falling animation should exist");
         assertNotNull(animationHandler.getAnimations().get(EntityState.PUNCHING), "Punching animation should exist");
         assertEquals(3, animationHandler.getAnimations().get(EntityState.IDLE).getKeyFrames().length);
     }

     @Test
     void testLoopingLogic() {
         IAnimationLoader jumpLoader = (w, h) -> {
             TextureRegion[][] frames = new TextureRegion[3][3];
             for (int r = 0; r < 3; r++) {
                 for (int c = 0; c < 3; c++) {
                     frames[r][c] = new TextureRegion();
                 }
             }
             return frames;
         };

         AnimationHandler jumpHandler = new AnimationHandler(jumpLoader, 32, 32);
         assertDoesNotThrow(() -> jumpHandler.getFrame(EntityState.JUMPING, 10.0f, true));
     }

     @Test
    void testAnimationNullReturnsIdleState(){
         IAnimationLoader limitedLoader = (w, h) -> {
             TextureRegion[][] frames = new TextureRegion[1][3];
             frames[0][0] = mock(TextureRegion.class);
             frames[0][1] = mock(TextureRegion.class);
             frames[0][2] = mock(TextureRegion.class);
             return frames;
         };

         AnimationHandler handler = new AnimationHandler(limitedLoader, 32, 32);
         EntityState missingState = EntityState.PUNCHING;

         assertNull(handler.getAnimations().get(missingState));

         TextureRegion result = handler.getFrame(missingState, 0.1f, true);

         TextureRegion expectedIdle = handler.getAnimations().get(EntityState.IDLE).getKeyFrame(0.1f);
         assertEquals(expectedIdle, result, "Should return IDLE when Punching state is missing");

    }
}

package inf112.zoombiegeddon.model.animations;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import inf112.zoombiegeddon.model.entity.Player;

import java.util.EnumMap;
import java.util.Map;

public class AnimationHandler {

    Texture characterSpriteSheet;


    private final Map<Player.State, Animation<TextureRegion>> playerAnimations = new EnumMap<>(Player.State.class);

    public AnimationHandler(Texture characterSpriteSheet) {
        this.characterSpriteSheet = characterSpriteSheet;
        createPlayerAnimationMap();
    }

    public Map<Player.State, Animation<TextureRegion>> getPlayerAnimations() {
        return playerAnimations;
    }

    private void createPlayerAnimationMap(){
        TextureRegion[][] characterFrames = TextureRegion.split(characterSpriteSheet, 80, 64);

        playerAnimations.put(Player.State.IDLE ,createAnimation(characterFrames[0],4, 0.3f));
        playerAnimations.put(Player.State.WALKING ,createAnimation(characterFrames[1],7, 0.06f));
        playerAnimations.put(Player.State.RUNNING ,createAnimation(characterFrames[2],7, 0.06f));
        playerAnimations.put(Player.State.JUMPING ,createAnimation(characterFrames[3],3, 0.1f));
        playerAnimations.put(Player.State.FALLING ,createAnimation(characterFrames[4],3, 0.1f));
        playerAnimations.put(Player.State.PUNCHING ,createAnimation(characterFrames[5],5, 0.1f));
        playerAnimations.put(Player.State.DEAD ,createAnimation(characterFrames[6],9, 0.1f));
    }

    private Animation<TextureRegion> createAnimation(TextureRegion[] row, int frameCount, float frameDuration) {
        TextureRegion[] frames = new TextureRegion[frameCount];
        for (int i = 0; i < frameCount; i++) {
            frames[i] = row[i];
        }
        return new Animation<>(frameDuration, frames);
    }
}

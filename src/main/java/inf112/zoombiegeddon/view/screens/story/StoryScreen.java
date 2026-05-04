package inf112.zoombiegeddon.view.screens.story;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Scaling;
import inf112.zoombiegeddon.app.AssetConstants;
import inf112.zoombiegeddon.app.GameApp;
import inf112.zoombiegeddon.audio.MusicTrack;
import inf112.zoombiegeddon.audio.Sfx;
import inf112.zoombiegeddon.model.story.StorySlide;
import inf112.zoombiegeddon.view.screens.AbstractScreen;
import inf112.zoombiegeddon.view.screens.level.PlayableLevel;

/**
 * Plays through a sequence of story slides with fade animations before
 * starting the first level. Any key or mouse click advances to the next
 * slide; Escape or right-click skips directly to the level.
 */
public class StoryScreen extends AbstractScreen {

    private static final float FADE_IN_DURATION = 1.5f;
    private static final float FADE_OUT_DURATION = 1.5f;
    private static final float DELAY_DURATION = 2f;

    private static final StorySlide[] SLIDES = {
            new StorySlide(AssetConstants.STORY_IMAGE_01, MusicTrack.STORY_CHILL_BACKGROUND, null, false),
            new StorySlide(AssetConstants.STORY_IMAGE_02, null, null, false),
            new StorySlide(AssetConstants.STORY_IMAGE_03, null, Sfx.GLASSBREAK, true),
            new StorySlide(AssetConstants.STORY_IMAGE_04, null, Sfx.WARSOUNDS, false)
    };

    private Image storyImage;
    private int currentStoryIndex;
    private boolean finished;

    /**
     * Creates a story screen for the given game application.
     *
     * @param app the game application
     */
    public StoryScreen(GameApp app) {
        super(app);
    }

    @Override
    public void buildScreen() {
        clearTable();

        stage.clear();
        stage.addActor(table);

        finished = false;
        currentStoryIndex = 0;

        storyImage = new Image();
        storyImage.setScaling(Scaling.fit);
        storyImage.getColor().a = 0f;

        table.add(storyImage).center();

        if (SLIDES.length == 0) {
            goToLevel1();
            return;
        }

        showSlide(currentStoryIndex);
        addStoryInput();
    }

    private void showSlide(int storyIndex) {
        if (storyIndex >= SLIDES.length) {
            goToLevel1();
            return;
        }

        StorySlide currentSlide = SLIDES[storyIndex];

        if (!app.getAssets().getManager().isLoaded(currentSlide.imagePath())) {
            goToLevel1();
            return;
        }

        if (currentSlide.stopPreviousMusic()) {
            app.getAudio().stopMusic();
        }
        if (currentSlide.musicTrack() != null) {
            app.getAudio().playMusic(currentSlide.musicTrack(), false);
        }
        if (currentSlide.sfxTrack() != null) {
            app.getAudio().playSfx(currentSlide.sfxTrack());
        }

        Texture texture = app.getAssets().getManager().get(currentSlide.imagePath(), Texture.class);
        storyImage.setDrawable(new TextureRegionDrawable(new TextureRegion(texture)));

        storyImage.clearActions();
        storyImage.getColor().a = 0f;
        storyImage.addAction(Actions.sequence(
                Actions.fadeIn(FADE_IN_DURATION),
                Actions.delay(DELAY_DURATION),
                Actions.fadeOut(FADE_OUT_DURATION),
                Actions.run(this::advanceStory)));
    }

    private void advanceStory() {
        if (finished) {
            return;
        }
        currentStoryIndex++;
        if (currentStoryIndex >= SLIDES.length) {
            goToLevel1();
            return;
        }
        showSlide(currentStoryIndex);
    }

    private void manualAdvance() {
        if (finished) {
            return;
        }

        storyImage.clearActions();
        advanceStory();
    }

    private void addStoryInput() {
        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    goToLevel1();
                } else {
                    manualAdvance();
                }
                return true;
            }
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (button == Input.Buttons.RIGHT) {
                    goToLevel1();
                } else {
                    manualAdvance();
                }
                return true;
            }
        });
    }

    /**
     * Transitions to the first story level, stopping any active audio.
     * Guards against being called more than once via the {@code finished} flag.
     */
    private void goToLevel1() {
        if (finished) {
            return;
        }

        finished = true;
        if (storyImage != null) {
            storyImage.clearActions();
        }

        app.getStoryManager().startTimer();
        app.setScreen(new PlayableLevel(
                app,
                app.getStoryManager().getCurrentMapPath(),
                GameApp.STORY_LEADERBOARD_ID
        ));
    }

    /**
     * Stops music and war sound effects when the screen is hidden,
     * since they should not carry over to the next screen.
     */
    @Override
    public void hide() {
        super.hide();
        app.getAudio().stopMusic();
        app.getAudio().stopSfx(Sfx.WARSOUNDS);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        super.render(delta);
    }
}

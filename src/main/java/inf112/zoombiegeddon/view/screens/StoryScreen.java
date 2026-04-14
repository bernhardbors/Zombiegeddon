package inf112.zoombiegeddon.view.screens;

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
import inf112.zoombiegeddon.view.levels.Level1;

public class StoryScreen extends AbstractScreen {

    private static final float fadeInDuration = 1.5f;
    private static final float fadeOutDuration = 1.5f;
    private static final float delayDuration = 2f;

    private static final String[] STORY_IMAGES = {
            AssetConstants.STORY_IMAGE_01,
            AssetConstants.STORY_IMAGE_02,
            AssetConstants.STORY_IMAGE_03,
            AssetConstants.STORY_IMAGE_04
    };

    private Image storyImage;
    private int currentStoryIndex;
    private boolean finished;

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

        if (STORY_IMAGES.length == 0) {
            goToLevel1();
            return;
        }

        showSlide(currentStoryIndex);
        addStoryInput();
    }

    private void showSlide(int storyIndex) {
        String storyImagePath = STORY_IMAGES[storyIndex];

        if (!app.getAssets().getManager().isLoaded(storyImagePath)) {
            goToLevel1();
            return;
        }

        switch(storyIndex) {
            case 0 ->
                    app.getAudio().playMusic(MusicTrack.STORY_CHILL_BACKGROUND, false);
            case 2 -> {
                app.getAudio().stopMusic();
                app.getAudio().playSfx(Sfx.GLASSBREAK);
            }
            case 3  ->
                    app.getAudio().playSfx(Sfx.WARSOUNDS);
        }

        Texture texture = app.getAssets().getManager().get(storyImagePath, Texture.class);
        storyImage.setDrawable(new TextureRegionDrawable(new TextureRegion(texture)));

        storyImage.clearActions();
        storyImage.getColor().a = 0f;
        storyImage.addAction(Actions.sequence(
                Actions.fadeIn(fadeInDuration),
                Actions.delay(delayDuration),
                Actions.fadeOut(fadeOutDuration),
                Actions.run(this::advanceStory)));
    }

    private void advanceStory() {
        if (finished) {
            return;
        }
        currentStoryIndex++;
        if (currentStoryIndex >= STORY_IMAGES.length) {
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

    private void goToLevel1() {
        if (finished) {
            return;
        }

        finished = true;
        if (storyImage != null) {
            storyImage.clearActions();
        }
        app.startStoryTimer();
        app.setScreen(new Level1(app));
    }

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

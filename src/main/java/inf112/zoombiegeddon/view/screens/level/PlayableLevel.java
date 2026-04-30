package inf112.zoombiegeddon.view.screens.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.ScreenUtils;
import inf112.zoombiegeddon.app.GameApp;
import inf112.zoombiegeddon.audio.MusicTrack;
import inf112.zoombiegeddon.controller.LevelController;
import inf112.zoombiegeddon.controller.PlayerController;
import inf112.zoombiegeddon.events.GameEvent;
import inf112.zoombiegeddon.events.GameEventBus;
import inf112.zoombiegeddon.events.IEventListener;
import inf112.zoombiegeddon.model.levels.LevelModel;
import inf112.zoombiegeddon.view.renderer.LevelRenderer;
import inf112.zoombiegeddon.view.screens.menus.LeaderboardScreen;

public class PlayableLevel extends ScreenAdapter implements IEventListener {

    private final GameApp app;
    private final LevelModel model;
    private final LevelRenderer renderer;
    private final PlayerController playerController;
    private final LevelController levelController;
    private final TiledMap map;

    public PlayableLevel(GameApp app, String mapPath) {
        this.app = app;

        this.map = new TmxMapLoader().load(mapPath);
        this.model = new LevelModel(app, map);
        this.renderer = new LevelRenderer(app, model, map);
        this.playerController = new PlayerController(model.getPlayer());
        this.levelController = new LevelController();
    }

    @Override
    public void show() {
        GameEventBus.register(this);
        app.getAudio().playMusic(MusicTrack.GAMEPLAY, true);

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(levelController);
        multiplexer.addProcessor(playerController);
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        playerController.update(delta);
        model.update(delta);
        renderer.render();
    }

    @Override
    public void onEvent(GameEvent event) {
        if (event == GameEvent.TOGGLE_PAUSE) {
            app.setScreen(new PauseScreen(app, this));
        }

        else if (event == GameEvent.PLAYER_DEATH) {
            if (app.getStoryManager().isActive()) {
                app.getStoryManager().finishRun();
            }
            app.setScreen(new GameOverScreen(app));
        }

        else if (event == GameEvent.LEVEL_FINISHED) {
            if (app.getStoryManager().isActive()) {
                app.getStoryManager().carryTime(model.getLevelTime());
                app.getStoryManager().advanceLevel();

                if (app.getStoryManager().hasNextLevel()) {
                    app.setScreen(new PlayableLevel(
                            app,
                            app.getStoryManager().getCurrentMapPath()
                    ));
                } else {
                    app.getLeaderboardController().registerLevelCompletion(
                            GameApp.STORY_LEADERBOARD_ID,
                            app.getStoryManager().getCurrentPlayerName(),
                            0,
                            model.getPlayer().getHealth(),
                            app.getStoryManager().getElapsedSeconds(0)
                    );
                    app.getStoryManager().finishRun();
                    app.setScreen(new LeaderboardScreen(app, GameApp.STORY_LEADERBOARD_ID, LeaderboardScreen.ScreenType.MAIN_MENU));
                }
            } else {
                app.setScreen(new NextLevelScreen(app));
            }
        }
    }

    @Override
    public void hide() {
        GameEventBus.unregister(this);
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void resize(int width, int height) {
        renderer.resize(width, height);
    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
    }

    /**
     * Draws the game without updating physics or logic.
     * Used in PauseScreen to display a frozen game in the background.
     */
    public void renderVisuals() {
        ScreenUtils.clear(0, 0, 0, 1);
        renderer.render();
    }
}

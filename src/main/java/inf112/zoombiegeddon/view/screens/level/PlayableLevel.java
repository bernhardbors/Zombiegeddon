package inf112.zoombiegeddon.view.screens.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.ScreenUtils;
import inf112.zoombiegeddon.app.GameApp;
import inf112.zoombiegeddon.controller.IPlayerController;
import inf112.zoombiegeddon.controller.LevelController;
import inf112.zoombiegeddon.controller.PlayerController;
import inf112.zoombiegeddon.events.GameEvent;
import inf112.zoombiegeddon.events.GameEventBus;
import inf112.zoombiegeddon.events.IEventListener;
import inf112.zoombiegeddon.model.levels.ILevelModel;
import inf112.zoombiegeddon.model.levels.LevelModel;
import inf112.zoombiegeddon.view.animations.AnimationHandler;
import inf112.zoombiegeddon.view.renderer.LevelRenderer;
import inf112.zoombiegeddon.view.screens.menus.LeaderboardScreen;

/**
 * Represents a running game level, wiring together the model, renderer,
 * and controllers. Owns animation resources and routes pickup events to
 * the renderer for visual feedback.
 */
public class PlayableLevel extends ScreenAdapter implements IEventListener {

    private final GameApp app;
    private final ILevelModel model;
    private final LevelRenderer renderer;
    private final IPlayerController playerController;
    private final LevelController levelController;
    private final TiledMap map;
    private final String mapPath;
    private final String boardId;

    private static final float VICTORY_DURATION = 4.25f;

    private boolean levelFinished = false;
    private float finishTimer = 0;
    private Runnable pendingTransition;

    public PlayableLevel(GameApp app, String mapPath, String boardId) {
        this.app = app;
        this.mapPath = mapPath;
        this.boardId = boardId;

        this.map = new TmxMapLoader().load(mapPath);
        this.model = new LevelModel(map);

        AnimationHandler playerAnims   = app.getAssets().getPlayerMaleAnimations();
        AnimationHandler zombieAnims   = app.getAssets().getZombieAnimations();
        AnimationHandler chonkerAnims  = app.getAssets().getChonkerAnimations();

        this.renderer = new LevelRenderer(app, model, map, playerAnims);
        this.renderer.registerEnemies(zombieAnims, chonkerAnims);

        this.playerController = new PlayerController(model.getPlayer());
        this.levelController = new LevelController();
    }

    /**
     * Registers this level as an event listener and starts gameplay music.
     */
    @Override
    public void show() {
        GameEventBus.register(this);
        GameEventBus.post(GameEvent.GAMEPLAY_STARTED);

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(levelController);
        multiplexer.addProcessor(playerController);
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        if (levelFinished) {
            finishTimer -= delta;
            if (pendingTransition != null && finishTimer <= 0) {
                pendingTransition.run();
                return;
            }
        } else {
            playerController.update(delta);
            model.update(delta);
        }
        renderer.render();
    }

    /**
     * Handles pause, player death, level completion, and pickup events.
     */
    @Override
    public void onEvent(GameEvent event) {
        switch (event) {
            case TOGGLE_PAUSE    -> app.setScreen(new PauseScreen(app, this));
            case PLAYER_DEATH    -> handlePlayerDeath();
            case LEVEL_FINISHED  -> handleLevelFinished();
            case PICKUP_SPEED    -> renderer.onPickupCollected("speed");
            case PICKUP_HEALTH   -> renderer.onPickupCollected("heal");
            default              -> {}
        }
    }

    private void handlePlayerDeath() {
        if (app.getStoryManager().isActive()) {
            app.getStoryManager().finishRun();
            app.setScreen(new GameOverScreen(
                    app, GameOverScreen.RetryMode.STORY, null, null));
        } else {
            app.setScreen(new GameOverScreen(
                    app, GameOverScreen.RetryMode.LEVEL, mapPath, boardId));
        }
    }

    private void handleLevelFinished() {
        levelFinished = true;
        finishTimer = VICTORY_DURATION;
        pendingTransition = app.getStoryManager().isActive()
                ? this::handleStoryLevelFinished
                : this::handleSingleLevelFinished;
    }

    private void handleStoryLevelFinished() {
        app.getStoryManager().carryTime(model.getLevelTime());
        app.getStoryManager().advanceLevel();

        if (app.getStoryManager().hasNextLevel()) {
            app.setScreen(new PlayableLevel(
                    app,
                    app.getStoryManager().getCurrentMapPath(),
                    GameApp.STORY_LEADERBOARD_ID));
            return;
        }

        app.getLeaderboardController().registerLevelCompletion(
                GameApp.STORY_LEADERBOARD_ID,
                app.getStoryManager().getCurrentPlayerName(),
                0,
                model.getPlayer().getHealth(),
                app.getStoryManager().getElapsedSeconds(0));

        app.getStoryManager().finishRun();
        app.setScreen(new LeaderboardScreen(
                app,
                GameApp.STORY_LEADERBOARD_ID,
                LeaderboardScreen.ScreenType.MAIN_MENU));
    }

    private void handleSingleLevelFinished() {
        app.getLeaderboardController().registerLevelCompletion(
                boardId,
                app.getCurrentPlayerName(),
                0,
                model.getPlayer().getHealth(),
                model.getLevelTime());

        app.setScreen(new LeaderboardScreen(
                app, boardId, LeaderboardScreen.ScreenType.MAIN_MENU));
    }

    /**
     * Unregisters this level as an event listener and clears the input processor.
     */
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
     * Used by PauseScreen to display a frozen game in the background.
     */
    public void renderVisuals() {
        ScreenUtils.clear(0, 0, 0, 1);
        renderer.render();
    }
}
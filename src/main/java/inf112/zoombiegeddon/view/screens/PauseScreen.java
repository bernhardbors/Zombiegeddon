package inf112.zoombiegeddon.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import inf112.zoombiegeddon.app.GameApp;
import inf112.zoombiegeddon.utils.UIUtils;

public class PauseScreen extends AbstractScreen {

    private final PlayableLevel previousLevel;

    public PauseScreen(final GameApp app, PlayableLevel previousLevel) {
        super(app);
        this.previousLevel = previousLevel;
    }

    @Override
    public void buildScreen() {
        clearTable();

        formatText("PAUSED", Color.WHITE, 4f);
        table.row().pad(20);

        UIUtils.addButton("Continue", app.getAssets().getSkin(), table, buttonWidth, buttonHeight,
                () -> app.setScreen(previousLevel));

        UIUtils.addButton("Settings", app.getAssets().getSkin(), table, buttonWidth, buttonHeight,
                () -> app.setScreen(new SettingsScreen(app, this)));

        UIUtils.addButton("Main Menu", app.getAssets().getSkin(), table, buttonWidth, buttonHeight,
                () -> {
                    previousLevel.dispose();
                    app.setScreen(new MainMenuScreen(app));
                });
    }

    @Override
    public void render(float delta) {
        previousLevel.renderVisuals();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        app.getShapeRenderer().begin(ShapeRenderer.ShapeType.Filled);
        app.getShapeRenderer().setColor(new Color(0f, 0f, 0f, 0.6f));
        app.getShapeRenderer().rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        app.getShapeRenderer().end();

        Gdx.gl.glDisable(GL20.GL_BLEND);

        stage.act(Math.min(delta, 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        if (previousLevel != null) {
            previousLevel.resize(width, height);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
package inf112.skeleton.view;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import inf112.skeleton.app.GameApp;
import inf112.skeleton.utils.UIUtils;

public class SettingsView implements Screen {
    private final Stage stage;
    private final Skin skin;
    private final GameApp app;
    private final Table table;

    public SettingsView(final GameApp app) {
        this.app = app;
        this.stage = new Stage(new ScreenViewport());
        this.skin = app.skin;
        this.table = new Table();

        table.setFillParent(true);
        stage.addActor(table);
    }

    @Override
    public void show() {

        float buttonWidth = (float) Gdx.graphics.getWidth() / 5;
        float buttonHeight = (float) Gdx.graphics.getHeight() / 15;

        Gdx.input.setInputProcessor(stage);
        table.clear();

        String[] resolutions = {"1280 x 720",
                "1366 x 768",
                "1600 x 900",
                "1920 x 1080",
                "2560 x 1440",
                "3840 x 2160"};


        com.badlogic.gdx.Preferences prefs = Gdx.app.getPreferences("Preferences");
        String savedRes = prefs.getString("resString", resolutions[0]);
        SelectBox<String> resSelect = UIUtils.addSelectBox("Resolutions:", resolutions, skin, table, buttonWidth, buttonHeight, this::changeResolution);
        resSelect.setSelected(savedRes);

        UIUtils.addButton("Back", skin, table, buttonWidth, buttonHeight, () -> app.setScreen(new MainMenuView(app)));
    }

    public void changeResolution(String resolution) {
        String[] parts = resolution.split("x");
        int width = Integer.parseInt(parts[0].trim());
        int height = Integer.parseInt(parts[1].trim());

        Gdx.graphics.setWindowedMode(width, height);

        // Save to Preferences
        com.badlogic.gdx.Preferences prefs = Gdx.app.getPreferences("Preferences");
        prefs.putInteger("width", width);
        prefs.putInteger("height", height);
        prefs.putString("resString", resolution);
        prefs.flush();

        resize(width, height);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Math.min(delta, 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }
}

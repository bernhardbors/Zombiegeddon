package inf112.zoombiegeddon.utils;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import inf112.zoombiegeddon.events.GameEvent;
import inf112.zoombiegeddon.events.GameEventBus;

public class UIUtils {

    /**
     * Creates a button and adds it to the provided table.
     */
    public static TextButton addButton(String name, Skin skin, Table table, Float width, Float height, Runnable action) {
        TextButton button = new TextButton(name, skin);

        button.getLabelCell().padBottom(height / 2);

        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameEventBus.post(GameEvent.UI_BUTTON_CLICK);
                action.run();

            }
        });

        table.add(button).fillX().uniformX().size(width, height).pad(10).row();

        return button;
    }

    /**
     * Creates a DropdownMenu and adds it to the provided table.
     */
    public static <T> SelectBox<T> addSelectBox(String name, T[] items, Skin skin, Table table, Float width, Float height, java.util.function.Consumer<T> action) {
        Label label = new Label(name, skin);
        SelectBox<T> selectBox = new SelectBox<>(skin);
        selectBox.setItems(items);

        selectBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                action.accept(selectBox.getSelected());
            }
        });

        table.add(label).padRight(10);
        table.add(selectBox).size(width, height).row();
        return selectBox;
    }
}
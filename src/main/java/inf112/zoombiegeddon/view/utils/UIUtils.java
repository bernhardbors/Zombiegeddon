package inf112.zoombiegeddon.view.utils;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import inf112.zoombiegeddon.events.GameEvent;
import inf112.zoombiegeddon.events.GameEventBus;

/**
 * Utility class for building and adding UI components to a {@link Table}.
 * Uses a builder pattern for clean and readable UI construction.
 */
public class UIUtils {

    private UIUtils() {}

    /**
     * Creates a builder for a {@link TextButton}.
     * @param name The button label.
     * @param skin The skin to apply.
     */
    public static ButtonBuilder button(String name, Skin skin) {
        return new ButtonBuilder(name, skin);
    }

    /**
     * Creates a builder for a {@link SelectBox}.
     * @param name  The label shown next to the dropdown.
     * @param items The items to display in the dropdown.
     * @param skin  The skin to apply.
     */
    public static <T> SelectBoxBuilder<T> selectBox(String name, T[] items, Skin skin) {
        return new SelectBoxBuilder<>(name, items, skin);
    }

    /**
     * Creates a builder for a {@link CheckBox}.
     * @param name The label shown next to the checkbox.
     * @param skin The skin to apply.
     */
    public static CheckBoxBuilder checkBox(String name, Skin skin) {
        return new CheckBoxBuilder(name, skin);
    }

    /**
     * Creates a builder for a {@link Slider}.
     * @param name The label shown above the slider.
     * @param skin The skin to apply.
     */
    public static SliderBuilder slider(String name, Skin skin) {
        return new SliderBuilder(name, skin);
    }

    /** Builder for creating and adding a {@link TextButton} to a table. */
    public static class ButtonBuilder {
        private final String name;
        private final Skin skin;
        private float width = 200f;
        private float height = 60f;
        private Runnable action = () -> {};

        private ButtonBuilder(String name, Skin skin) {
            this.name = name;
            this.skin = skin;
        }

        /**
         * Sets the button size.
         * @param width  Button width.
         * @param height Button height.
         */
        public ButtonBuilder size(float width, float height) {
            this.width = width;
            this.height = height;
            return this;
        }

        /**
         * Sets the action to run when the button is clicked.
         * @param action The click handler.
         */
        public ButtonBuilder action(Runnable action) {
            this.action = action;
            return this;
        }

        /**
         * Builds the button and adds it to the given table.
         * @param table The table to add the button to.
         * @return The created {@link TextButton}.
         */
        public TextButton addTo(Table table) {
            TextButton button = new TextButton(name, skin);
            button.getLabelCell().padBottom(height / 2);
            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    GameEventBus.post(GameEvent.UI_BUTTON_CLICK);
                    action.run();
                }
            });
            table.add(button).fillX().uniformX().size(width, height).pad(5).row();
            return button;
        }
    }

    /** Builder for creating and adding a {@link SelectBox} to a table. */
    public static class SelectBoxBuilder<T> {
        private final String name;
        private final T[] items;
        private final Skin skin;
        private float width = 200f;
        private float height = 40f;
        private java.util.function.Consumer<T> action = t -> {};

        private SelectBoxBuilder(String name, T[] items, Skin skin) {
            this.name = name;
            this.items = items;
            this.skin = skin;
        }

        /**
         * Sets the dropdown size.
         * @param width  Dropdown width.
         * @param height Dropdown height.
         */
        public SelectBoxBuilder<T> size(float width, float height) {
            this.width = width;
            this.height = height;
            return this;
        }

        /**
         * Sets the action to run when the selected item changes.
         * @param action The selection handler.
         */
        public SelectBoxBuilder<T> action(java.util.function.Consumer<T> action) {
            this.action = action;
            return this;
        }

        /**
         * Builds the select box and adds it to the given table.
         * @param table The table to add the select box to.
         * @return The created {@link SelectBox}.
         */
        public SelectBox<T> addTo(Table table) {
            Label label = new Label(name, skin);
            SelectBox<T> selectBox = new SelectBox<>(skin);
            selectBox.setItems(items);
            selectBox.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    action.accept(selectBox.getSelected());
                }
            });
            table.add(label).width(150f).left();
            table.add(selectBox).size(width, height).growX().row();
            return selectBox;
        }
    }

    /** Builder for creating and adding a {@link CheckBox} to a table. */
    public static class CheckBoxBuilder {
        private final String name;
        private final Skin skin;
        private boolean checked = false;
        private java.util.function.Consumer<Boolean> action = b -> {};

        private CheckBoxBuilder(String name, Skin skin) {
            this.name = name;
            this.skin = skin;
        }

        /**
         * Sets the initial checked state.
         * @param checked True if the checkbox should start checked.
         */
        public CheckBoxBuilder checked(boolean checked) {
            this.checked = checked;
            return this;
        }

        /**
         * Sets the action to run when the checkbox state changes.
         * @param action The change handler.
         */
        public CheckBoxBuilder action(java.util.function.Consumer<Boolean> action) {
            this.action = action;
            return this;
        }

        /**
         * Builds the checkbox and adds it to the given table.
         * @param table The table to add the checkbox to.
         * @return The created {@link CheckBox}.
         */
        public CheckBox addTo(Table table) {
            Label label = new Label(name, skin);
            CheckBox checkBox = new CheckBox("", skin);
            checkBox.setChecked(checked);
            checkBox.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    action.accept(checkBox.isChecked());
                }
            });
            table.add(label).width(150f).left();
            table.add(checkBox).left().row();
            return checkBox;
        }
    }

    /** Builder for creating and adding a {@link Slider} to a table. */
    public static class SliderBuilder {
        private final String name;
        private final Skin skin;
        private float value = 0f;
        private float min = 0f;
        private float max = 1f;
        private float step = 0.01f;
        private java.util.function.Consumer<Float> action = f -> {};

        private SliderBuilder(String name, Skin skin) {
            this.name = name;
            this.skin = skin;
        }

        /**
         * Sets the initial value of the slider.
         * @param value The starting value.
         */
        public SliderBuilder value(float value) {
            this.value = value;
            return this;
        }

        /**
         * Sets the range and step size of the slider.
         * @param min  Minimum value.
         * @param max  Maximum value.
         * @param step Step increment.
         */
        public SliderBuilder range(float min, float max, float step) {
            this.min = min;
            this.max = max;
            this.step = step;
            return this;
        }

        /**
         * Sets the action to run when the slider value changes.
         * @param action The change handler.
         */
        public SliderBuilder action(java.util.function.Consumer<Float> action) {
            this.action = action;
            return this;
        }

        /**
         * Builds the slider and adds it to the given table.
         * @param table The table to add the slider to.
         * @return The created {@link Slider}.
         */
        public Slider addTo(Table table) {
            Label label = new Label(name, skin);
            Slider slider = new Slider(min, max, step, false, skin);
            slider.setValue(value);
            slider.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    action.accept(slider.getValue());
                }
            });
            table.add(label).left().row();
            table.add(slider).growX().height(30f).padBottom(12f).row();
            return slider;
        }
    }
}
package inf112.zoombiegeddon.utils;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import inf112.zoombiegeddon.events.GameEvent;
import inf112.zoombiegeddon.events.GameEventBus;

public class UIUtils {

    public static ButtonBuilder button(String name, Skin skin) {
        return new ButtonBuilder(name, skin);
    }

    public static <T> SelectBoxBuilder<T> selectBox(String name, T[] items, Skin skin) {
        return new SelectBoxBuilder<>(name, items, skin);
    }

    public static CheckBoxBuilder checkBox(String name, Skin skin) {
        return new CheckBoxBuilder(name, skin);
    }

    public static SliderBuilder slider(String name, Skin skin) {
        return new SliderBuilder(name, skin);
    }

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

        public ButtonBuilder size(float width, float height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public ButtonBuilder action(Runnable action) {
            this.action = action;
            return this;
        }

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

        public SelectBoxBuilder<T> size(float width, float height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public SelectBoxBuilder<T> action(java.util.function.Consumer<T> action) {
            this.action = action;
            return this;
        }

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

    public static class CheckBoxBuilder {
        private final String name;
        private final Skin skin;
        private boolean checked = false;
        private java.util.function.Consumer<Boolean> action = b -> {};

        private CheckBoxBuilder(String name, Skin skin) {
            this.name = name;
            this.skin = skin;
        }

        public CheckBoxBuilder checked(boolean checked) {
            this.checked = checked;
            return this;
        }

        public CheckBoxBuilder action(java.util.function.Consumer<Boolean> action) {
            this.action = action;
            return this;
        }

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

        public SliderBuilder value(float value) {
            this.value = value;
            return this;
        }

        public SliderBuilder range(float min, float max, float step) {
            this.min = min;
            this.max = max;
            this.step = step;
            return this;
        }

        public SliderBuilder action(java.util.function.Consumer<Float> action) {
            this.action = action;
            return this;
        }

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
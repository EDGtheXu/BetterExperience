package com.github.edg_thexu.better_experience.client.gui.editor;

import net.minecraft.client.gui.components.AbstractWidget;

import java.lang.reflect.Field;
import java.util.function.Consumer;

public interface PropertyEditor<T> {
    String getFieldName();

    AbstractWidget createWidget(int x, int y, int width, Consumer<T> onValueChanged);

    T getValue();

    void setValue(T value);

    Field getField();

    ObjectEditor getEditor();

    void saveChanges();

    T copyValue();

    default void clear(){

    }
}

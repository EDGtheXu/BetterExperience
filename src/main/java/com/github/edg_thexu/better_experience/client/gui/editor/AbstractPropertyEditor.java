package com.github.edg_thexu.better_experience.client.gui.editor;

import java.lang.reflect.Field;

public abstract class AbstractPropertyEditor<T> implements PropertyEditor<T> {
    protected final Field field;
    protected T value;
    protected ObjectEditor editor;
    protected T oriValue;

    public AbstractPropertyEditor(Field field, T value, ObjectEditor editor) {
        this.field = field;
        this.editor = editor;
        this.oriValue = value;
        this.value = this.copyValue();
    }

    @Override
    public String getFieldName() {
        return field.getName();
    }

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public void setValue(T value) {
        this.value = value;
    }

    public Field getField() {
        return field;
    }

    @Override
    public ObjectEditor getEditor() {
        return editor;
    }

    @Override
    public void saveChanges(){

    }

    public T copyValue(){
        return oriValue;
    }

    public ObjectEditorScreen getScreen(){
        return editor.screen;
    }

}

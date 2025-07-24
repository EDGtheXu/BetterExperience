package com.github.edg_thexu.better_experience.client.gui.editor;

import java.util.function.Consumer;

public class HelperEditor extends ObjectEditor {
    public HelperEditor(Object targetObject, Consumer<Object> onSave, ObjectEditorScreen screen, Class<?> targetClass) {
        super(targetObject, onSave, screen, targetClass);
    }

    protected void initializeFields() {

    }

}

package com.github.edg_thexu.better_experience.client.gui.editor;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.*;
import net.minecraft.network.chat.Component;

import java.lang.reflect.Field;
import java.util.function.Consumer;

public class ObjectEditors {

    /**
     * 字符串属性编辑器
      */
    public static class StringPropertyEditor extends AbstractPropertyEditor<String> {

        public StringPropertyEditor(Field field, String value, ObjectEditor editor) {
            super(field, value, editor);
        }

        @Override
        public AbstractWidget createWidget(int x, int y, int width, Consumer<String> onValueChanged) {
            EditBox textField = new EditBox(
                    Minecraft.getInstance().font, x, y, width, 20, Component.literal(getFieldName())
            );
            textField.setValue(value);
            textField.setResponder(s -> {
                value = s;
                onValueChanged.accept(s);
            });
            return textField;
        }
    }

    /**
     * 整数属性编辑器
      */
    public static class IntPropertyEditor extends AbstractPropertyEditor<Integer> {

        public IntPropertyEditor(Field field, int value, ObjectEditor editor) {
            super(field, value, editor);
        }
        @Override
        public AbstractWidget createWidget(int x, int y, int width, Consumer<Integer> onValueChanged) {
            EditBox textField = new EditBox(
                    Minecraft.getInstance().font, x, y, width, 20, Component.literal(getFieldName())
            );
            textField.setValue(String.valueOf(value));
            textField.setResponder(s -> {
                try {
                    value = Integer.parseInt(s);
                    onValueChanged.accept(value);
                } catch (NumberFormatException e) {
                    // 忽略无效输入
                }
            });
            return textField;
        }
    }

    /**
     * 布尔属性编辑器
      */
    public static class BooleanPropertyEditor extends AbstractPropertyEditor<Boolean> {

        public BooleanPropertyEditor(Field field, Boolean value, ObjectEditor editor) {
            super(field, value, editor);
        }

        @Override
        public AbstractWidget createWidget(int x, int y, int width, Consumer<Boolean> onValueChanged) {
            return Checkbox.builder(Component.literal(getFieldName() + ": " + (value ? "ON" : "OFF")),
                    Minecraft.getInstance().font).pos(x, y).selected(value).onValueChange(
                    (b, c) -> {
                        value = !value;
                        b.setMessage(Component.literal(getFieldName() + ": " + (value ? "ON" : "OFF")));
                        onValueChanged.accept(value);
                    }).selected(value)
                    .build();
        }
    }

    // 集合属性编辑器
//    public static class CollectionPropertyEditor<T> implements ObjectEditor.PropertyEditor<Collection<T>> {
//        private final Field field;
//        private Collection<T> collection;
//        private <T> listWidget;
//
//        public CollectionPropertyEditor(Field field) {
//            this.field = field;
//            try {
//                this.collection = (Collection<T>) field.get(null); // 静态字段处理
//            } catch (Exception e) {
//                try {
//                    this.collection = (Collection<T>) field.get(field.getDeclaringClass());
//                } catch (Exception ex) {
//                    this.collection = new ArrayList<>();
//                }
//            }
//        }
//
//        @Override
//        public String getFieldName() {
//            return field.getName();
//        }
//
//        @Override
//        public Widget createWidget(int x, int y, int width, Consumer<Collection<T>> onValueChanged) {
//            // 简化实现 - 实际需要更复杂的列表编辑器
//            Button button = new Button(x, y, width, 20,
//                    new StringTextComponent(getFieldName() + " (Collection)"),
//                    b -> {
//                        // 这里应该打开一个子编辑器
//                        System.out.println("Editing collection: " + collection);
//                    }
//            );
//            return button;
//        }
//
//        @Override
//        public Collection<T> getValue() {
//            return collection;
//        }
//
//        @Override
//        public void setValue(Collection<T> value) {
//            this.collection = value;
//        }
//    }

    /**
     * 对象属性编辑器
     */
    public static class ObjectPropertyEditor<T> extends AbstractPropertyEditor<T> {
        MultiLineEditBox textField;
        MultiLineEditBox errorField;
        Button button;
        HelperEditor helper;
        Button helperButton;
        public ObjectPropertyEditor(Field field, T value, ObjectEditor editor) {
            super(field, value, editor);

        }

        @Override
        public AbstractWidget createWidget(int x, int y, int width, Consumer<T> onValueChanged) {
            this.helper = new HelperEditor(this.value, (v)->{}, this.getScreen(), this.getField().getType());
            this.helperButton = Button.builder(Component.literal("Edit Helper"), b -> {

                this.editor.nextField(this);
            }).bounds(0,0,20,20).build();
            this.button = Button.builder(Component.literal((value == null ? "null" :  value.toString()) ), b -> {
                // 打开一个子编辑器
                if(this.editor.getSelectedProperty() != null){
                    this.editor.getSelectedProperty().clear();
                }
                this.getScreen().removeJsonEditor();
                this.getScreen().addWidget(helperButton);

                if(this.textField != null){
                    return;
                }
                int x1 = getScreen().width /2 + 15;
                int w = getScreen().width - x1-5;

//                System.out.println("Editing object: " + value);
                // 如果已经注册适配器的codec，打开json编辑器
                if(ObjectAdapter.getObjectData(this.field.getType()) != null){
                    this.getEditor().setSelectedProperty(this);
                    ObjectAdapter.ObjectData data = ObjectAdapter.getObjectData(this.field.getType());
                    String str = "";
                    textField = new MultiLineEditBox(getScreen().getMinecraft().font,
                            x1, 20, w, 130
                            ,Component.literal("Object Data:"),Component.literal(str));
                    if(this.value != null){
                        var d = data.codec().encodeStart(Minecraft.getInstance().level.registryAccess().createSerializationContext(
                                JsonOps.INSTANCE), this.value).result().orElse(null);
                        if(d!= null){
                            if(d instanceof JsonObject object){
                                Gson gson = new Gson().newBuilder().setPrettyPrinting().create();
                                str = gson.toJson(object);
                            }else {
                                str = d.toString();
                            }
                            textField.setValue(str);
                        }
                    }
                    this.getScreen().addEditor(textField);
                }else {
                    // 否则入栈编辑器栈
                    this.editor.nextField(this);
                }
            }).bounds(x, y, width, 20).build();
            return this.button;
        }

        @Override
        public void saveChanges(){
            if(textField != null){
                String str = textField.getValue();
                var data = ObjectAdapter.getObjectData(this.field.getType());
                int x = getScreen().width /2 + 15;
                int w = getScreen().width - x-5;

                if(data!= null){
                    try {
                        var res = data.codec().parse(Minecraft.getInstance().level.registryAccess()
                                .createSerializationContext(JsonOps.INSTANCE), JsonParser.parseString(str));
                        if(res.result().isPresent()){
                            this.value = (T) res.result().get();
                            this.button.setMessage(Component.literal(this.value.toString()));
                        }else{
                            if(this.errorField != null){
                                this.getScreen().removeEditor(errorField);
                            }
                            this.errorField = new MultiLineEditBox(getScreen().getMinecraft().font,
                                    x, 160, w, 40
                                    ,Component.literal("Error:"),Component.literal(res.error().toString()));
                            this.errorField.setValue(res.error().toString());
                            this.getScreen().addEditor(errorField);
                            return;
                        }
                    }catch (Exception e){
                        if(this.errorField != null){
                            this.getScreen().removeEditor(errorField);
                        }
                        this.errorField = new MultiLineEditBox(getScreen().getMinecraft().font,
                                x, 160, w, 40
                                ,Component.literal("Error:"),Component.literal(e.getMessage()));
                        this.errorField.setValue(e.getMessage());
                        this.getScreen().addEditor(errorField);
                        return;
                    }

                }
                if(this.errorField != null){
                    this.getScreen().removeEditor(errorField);
                }
                this.editor.setSelectedProperty(null);
                this.getScreen().removeEditor(textField);
                this.textField = null;
            }
        }

        @Override
        public void clear(){
            if(this.textField != null){
                this.getScreen().removeEditor(textField);
                this.textField = null;
            }
            if(this.errorField != null){
                this.getScreen().removeEditor(errorField);
                this.errorField = null;
            }
            this.getScreen().deleteWidget(helperButton);
        }
    }
}
package com.github.edg_thexu.better_experience.client.gui.editor;



import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import com.github.edg_thexu.better_experience.client.gui.editor.ObjectEditors.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Consumer;

@OnlyIn(Dist.CLIENT)
public class ObjectEditor {
    private final Object targetObject;
    private final List<PropertyEditor<?>> propertyEditors = new ArrayList<>();
    private final Map<String, Field> fieldCache = new HashMap<>();
//    private final Map<String, Object> valueCache = new HashMap<>();
    private final Consumer<Object> onSave;
    private Map<Field, ObjectEditor> subEditors = new HashMap<>();

    public ObjectEditorScreen screen;
    public PropertyEditor<?> selectedProperty;

    Class<?> targetClass;

    public ObjectEditor(Object targetObject, Consumer<Object> onSave, ObjectEditorScreen screen, Class<?> targetClass) {
        this.targetClass = targetClass;
        this.targetObject = targetObject;
        this.onSave = onSave;
        initializeFields();
        this.screen = screen;

    }

    public void nextField(PropertyEditor<?> complex){
        // 已有缓存的子编辑器，直接打开
        if(this.subEditors.containsKey(complex.getField())){
            this.screen.openObjectEditor(this.subEditors.get(complex.getField()));
            return;
        }
        // 否则，创建新的子编辑器
        ObjectEditor editor;
        if(complex.getField().getType() == PatchedDataComponentMap.class){
            editor = new HelperEditor(complex.getValue(), (v)->{

            }, screen, complex.getField().getType());
        }else{
            editor = new ObjectEditor(complex.getValue(), (v)->{

            }, screen, complex.getField().getType());
        }

        this.screen.openObjectEditor(editor);
        this.subEditors.put(complex.getField(), editor);
    }


    protected void initializeFields() {
        Class<?> clazz = targetClass;
        while (clazz != null && clazz != Object.class) {
            ObjectAdapter.ObjectData data = ObjectAdapter.getObjectData(targetClass);
            for (Field field : clazz.getDeclaredFields()) {
                if (!fieldCache.containsKey(field.getName()) && !Modifier.isStatic(field.getModifiers()) ) { // 不能是静态字段

                    try {
                        String name = field.getName();
                        if(data != null ){
                            if(data.keepFields() != null && !data.keepFields().contains(name) ){
                                continue;
                            }
                            if(data.ignoreFields() != null && data.ignoreFields().contains(name)){
                                continue;
                            }
                        }
                        field.setAccessible(true);
                        fieldCache.put(name, field);
                        PropertyEditor<?> editor = createPropertyEditor(field, field.get(targetObject));
                        propertyEditors.add(editor);


                    } catch (Exception e) {
                        // 忽略无法访问的字段
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }


    private PropertyEditor<?> createPropertyEditor(Field field, Object value) {
        Class<?> type = field.getType();
        
        if (type == String.class) {
            return new StringPropertyEditor(field, (String) value, this);
        } else if (type == int.class || type == Integer.class) {
            return new IntPropertyEditor(field, (Integer) value, this);
        } else if (type == boolean.class || type == Boolean.class) {
            return new BooleanPropertyEditor(field, (Boolean) value, this);
//        } else if (type == double.class || type == Double.class) {
//            return new DoublePropertyEditor(field);
//        } else if (type == float.class || type == Float.class) {
//            return new FloatPropertyEditor(field);
//        } else if (Collection.class.isAssignableFrom(type)) {
//            return new CollectionPropertyEditor<>(field);
//        } else if (Map.class.isAssignableFrom(type)) {
//            return new MapPropertyEditor<>(field);
        } else {
            return new ObjectEditors.ObjectPropertyEditor<>(field, value, this);
        }
    }

    public List<PropertyEditor<?>> getPropertyEditors() {
        return propertyEditors;
    }

    public void saveChanges() {
        // 正在编辑json时，保存json
        if(this.selectedProperty!= null){
            this.selectedProperty.saveChanges();
            return;
        }
        // 否则，保存所有属性
        onSave.accept(targetObject);
        for(PropertyEditor<?> editor : propertyEditors){
            editor.saveChanges();
        }
    }

    /**
     * 设置当前选中的属性
     */
    public void setSelectedProperty(PropertyEditor<?> selectedProperty) {
        this.selectedProperty = selectedProperty;
    }

    public PropertyEditor<?> getSelectedProperty() {
        return selectedProperty;
    }

}
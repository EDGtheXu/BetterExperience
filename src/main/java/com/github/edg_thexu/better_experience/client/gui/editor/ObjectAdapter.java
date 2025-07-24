package com.github.edg_thexu.better_experience.client.gui.editor;

import com.mojang.serialization.Codec;
import net.minecraft.Util;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ObjectAdapter {
    public record ObjectData<T>(Codec<T> codec, Set<String> keepFields, Set<Class<?>> ignoreFields){
        public ObjectData(Codec<T> codec){
            this(codec, null, null);
        }

    }

    public static Map<Class , ObjectData> objectDataMap = Util.make(new HashMap<>(), map -> {

        map.put(Item.class, new ObjectData<>(BuiltInRegistries.ITEM.byNameCodec()));
        map.put(DataComponentMap.class, new ObjectData<>(DataComponentMap.CODEC));
        map.put(PatchedDataComponentMap.class, new ObjectData<>(DataComponentMap.CODEC));
        map.put(ItemStack.class, new ObjectData<>(ItemStack.CODEC, Set.of("item", "count", "components"), null));

    });

    static Field getField(Class clazz, String name){
        try {
            return clazz.getField(name);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }


    public static <T> void registerObject(Class<T> clazz, Codec<T> codec){
        objectDataMap.put(clazz, new ObjectData<>(codec));
    }
    public static <T> ObjectData<T> getObjectData(Class<T> clazz){
        return objectDataMap.get(clazz);
    }

}

package com.github.edg_thexu.better_experience.init;

import com.github.edg_thexu.better_experience.Better_experience;
import com.github.edg_thexu.better_experience.data.component.ItemContainerComponent;
import com.github.edg_thexu.cafelib.api.datacomponent.IDataComponentType;
import com.github.edg_thexu.cafelib.data.codec.DataComponentProvider;
import com.github.edg_thexu.cafelib.registries.CafeLibRegistries;
import com.mojang.serialization.Codec;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;


import java.util.function.Supplier;

public final class ModDataComponentTypes {

    public static DeferredRegister<DataComponentProvider<? extends IDataComponentType<?>>> TYPES =  DeferredRegister.create(CafeLibRegistries.DataComponentProviders.KEY, Better_experience.MODID);
    public static final Supplier<IForgeRegistry<DataComponentProvider<?>>> REGISTRY = TYPES.makeRegistry(RegistryBuilder::new);


    public static final Supplier<DataComponentProvider<ItemContainerComponent>> ITEM_CONTAINER_COMPONENT =
            register("item_container", ItemContainerComponent.CODEC);


    private static <T extends IDataComponentType<T>> Supplier<DataComponentProvider<T>> register(String location, Supplier<Codec<T>> codec) {
        return TYPES.register(location, () -> new DataComponentProvider<>(Better_experience.MODID + ":"+location, codec));
    }

    public static void register(IEventBus bus) {
        TYPES.register(bus);
    }

}

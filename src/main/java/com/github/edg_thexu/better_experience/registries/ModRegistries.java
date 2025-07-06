package com.github.edg_thexu.better_experience.registries;

import com.github.edg_thexu.better_experience.Better_experience;
import com.github.edg_thexu.better_experience.intergration.jei.JeiHelper;
import com.github.edg_thexu.better_experience.registries.itemmatcher.ItemMatcherTypes;
import com.github.edg_thexu.better_experience.registries.recipehandler.RecipeHandlerProviderTypes;
import com.github.edg_thexu.better_experience.registries.recipehandlerfactory.RecipeHandlerFactoryProviderTypes;
import com.github.edg_thexu.cafelib.api.datacomponent.IDataComponentType;
import com.github.edg_thexu.cafelib.data.codec.DataComponentProvider;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.eventbus.api.IEventBus;

import static net.minecraft.resources.ResourceKey.createRegistryKey;


public class ModRegistries {

//    public static void newRegistry(NewRegistryEvent event) {
//        if(JeiHelper.isLoaded()) {
//            event.register(JeiRegistries.RecipeHandlerProviders.REGISTRY);
//            event.register(JeiRegistries.RecipeHandlerFactoryProviders.REGISTRY);
//        }
//    }

    public static void register(IEventBus bus) {
        if(JeiHelper.isLoaded()) {
            RecipeHandlerProviderTypes.TYPES.register(bus);

            RecipeHandlerFactoryProviderTypes.TYPES.register(bus);
            ItemMatcherTypes.TYPES.register(bus);
        }
    }

    public static class DataComponentProviders{

        public static final ResourceKey<Registry<DataComponentProvider<? extends IDataComponentType<?>>>> KEY = createRegistryKey(Better_experience.space("data_component"));

    }

}

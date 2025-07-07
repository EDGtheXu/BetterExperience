package com.github.edg_thexu.better_experience.intergration.jei;

import com.github.edg_thexu.better_experience.Better_experience;
import com.github.edg_thexu.better_experience.registries.itemmatcher.ItemMatcherProvider;
import com.github.edg_thexu.better_experience.registries.recipehandler.RecipeHandlerProvider;
import com.github.edg_thexu.better_experience.registries.recipehandlerfactory.RecipeHandlerFactoryProvider;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.RegistryBuilder;

public class JeiRegistries {
    public static class RecipeHandlerProviders {
        public static final ResourceKey<Registry<RecipeHandlerProvider>> KEY = ResourceKey.createRegistryKey(Better_experience.space("recipe_handler_type"));
        public static final Registry<RecipeHandlerProvider> REGISTRY = new RegistryBuilder<>(KEY).create();
    }

    public static class RecipeHandlerFactoryProviders {
        public static final ResourceKey<Registry<RecipeHandlerFactoryProvider>> KEY = ResourceKey.createRegistryKey(Better_experience.space("recipe_handler_factory_type"));
        public static final Registry<RecipeHandlerFactoryProvider> REGISTRY = new RegistryBuilder<>(KEY).create();
    }

    public static class ItemMatcherProviders {
        public static final ResourceKey<Registry<ItemMatcherProvider>> KEY = ResourceKey.createRegistryKey(Better_experience.space("item_matcher_type"));
        public static final Registry<ItemMatcherProvider> REGISTRY = new RegistryBuilder<>(KEY).create();
    }
}

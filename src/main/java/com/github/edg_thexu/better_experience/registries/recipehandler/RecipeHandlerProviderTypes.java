package com.github.edg_thexu.better_experience.registries.recipehandler;

import com.github.edg_thexu.better_experience.Better_experience;
import com.github.edg_thexu.better_experience.intergration.jei.JeiRegistries;
import com.github.edg_thexu.better_experience.registries.recipehandler.variant.ItemStackUniversalHandler;
import com.github.edg_thexu.better_experience.registries.recipehandler.variant.VanillaRecipeHolderHandler;
import com.mojang.serialization.MapCodec;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;


public class RecipeHandlerProviderTypes {
    public static final DeferredRegister<RecipeHandlerProvider> TYPES = DeferredRegister.create(JeiRegistries.RecipeHandlerProviders.REGISTRY, Better_experience.MODID);

    public static final Supplier<RecipeHandlerProvider> VANILLA_TYPE = register("simple_track_type", ()->VanillaRecipeHolderHandler.CODEC);
    public static final Supplier<RecipeHandlerProvider> BREW_TYPE = register("brew_type", ItemStackUniversalHandler.CODEC);


    private static Supplier<RecipeHandlerProvider> register(String name, Supplier<MapCodec<? extends IRecipeHandler>> codec) {
        return TYPES.register(name, ()->new RecipeHandlerProvider(codec));
    }
}

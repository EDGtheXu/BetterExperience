package com.github.edg_thexu.better_experience.registries.recipehandler;

import com.mojang.serialization.MapCodec;

import java.util.function.Supplier;


public record RecipeHandlerProvider(Supplier<MapCodec<? extends IRecipeHandler>> codec) {


}

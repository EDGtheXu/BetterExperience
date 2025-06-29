package com.github.edg_thexu.better_experience.registries.recipehandler;

import com.mojang.serialization.MapCodec;
import org.confluence.terraentity.registries.track.ITrackType;

import java.util.function.Supplier;


public record RecipeHandlerProvider(Supplier<MapCodec<? extends IRecipeHandler>> codec) {


}

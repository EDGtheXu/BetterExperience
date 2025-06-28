package com.github.edg_thexu.better_experience.registries.recipehandler;

import com.mojang.serialization.MapCodec;
import org.confluence.terraentity.registries.track.ITrackType;


public record RecipeHandlerProvider(MapCodec<? extends IRecipeHandler> codec) {


}

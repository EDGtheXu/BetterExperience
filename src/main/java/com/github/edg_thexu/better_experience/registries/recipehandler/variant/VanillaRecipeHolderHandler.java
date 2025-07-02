package com.github.edg_thexu.better_experience.registries.recipehandler.variant;

import com.github.edg_thexu.better_experience.registries.recipehandler.IRecipeHandler;
import com.github.edg_thexu.better_experience.registries.recipehandler.RecipeHandlerProvider;
import com.github.edg_thexu.better_experience.registries.recipehandler.RecipeHandlerProviderTypes;
import com.github.edg_thexu.better_experience.registries.recipehandler.visitor.IRecipeHandlerVisitor;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;

import java.util.List;

/**
 * jei使用原版RecipeHolder时的处理器，只需要传入id即可获取配方的全部信息
 * @param location 配方的id
 */
public record VanillaRecipeHolderHandler(ResourceLocation location, int count) implements IRecipeHandler<Ingredient> {

    public static MapCodec<VanillaRecipeHolderHandler> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            ResourceLocation.CODEC.fieldOf("location").forGetter(VanillaRecipeHolderHandler::location),
            Codec.INT.fieldOf("count").forGetter(VanillaRecipeHolderHandler::count)
    ).apply(builder, VanillaRecipeHolderHandler::new));

//            ResourceLocation.CODEC.xmap(VanillaRecipeHolderHandler::new, VanillaRecipeHolderHandler::location).fieldOf("handler");


    @Override
    public List<Ingredient> getIngredient(Level level) {
        Recipe recipe = level.getRecipeManager().byKey(location).orElse(null);
        if(recipe == null){
//            if(Internal.getJeiRuntime().getRecipeManager().getRecipeCategory(RecipeTypes.BREWING).)
            return List.of();
        }
        return recipe.getIngredients();
    }

    @Override
    public IRecipeHandlerVisitor<Ingredient> getVisitor() {
        return IRecipeHandlerVisitor.ingredientVisitor;
    }


    @Override
    public boolean match(ItemStack stack, int slot) {
        return false;
    }

    @Override
    public RecipeHandlerProvider getCodec() {
        return RecipeHandlerProviderTypes.VANILLA_TYPE.get();
    }
}

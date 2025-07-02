package com.github.edg_thexu.better_experience.data.gen.recipe;


import com.github.edg_thexu.better_experience.Better_experience;
import com.github.edg_thexu.better_experience.init.ModBlocks;
import com.github.edg_thexu.better_experience.init.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;


public class ModRecipeProvider extends RecipeProvider {

    public ModRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe>  recipeOutput) {

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.AUTO_FISH_BLOCK.get())
                .pattern("aaa")
                .pattern("bcb")
                .pattern("ddd")
                .define('a',Items.GOLD_INGOT)
                .define('b',Items.REDSTONE)
                .define('c',Items.CHEST)
                .define('d',Items.IRON_INGOT)
                .unlockedBy("has_gold_ingot",has(Items.GOLD_INGOT))
                .save(recipeOutput);

//        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.AUTO_SELL_BLOCK)
//                .pattern("a a")
//                .pattern("b b")
//                .pattern("ddd")
//                .define('a',Items.GOLD_INGOT)
//                .define('b',Items.REDSTONE)
//                .define('c',Items.CHEST)
//                .define('d',Items.IRON_INGOT)
//                .unlockedBy("has_gold_ingot",has(Items.GOLD_INGOT))
//                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.PotionBag.get())
                .pattern("aaa")
                .pattern("a a")
                .pattern("aaa")
                .define('a',Items.LEATHER)
                .unlockedBy("has_leather",has(Items.LEATHER))
                .save(recipeOutput);


    }

}

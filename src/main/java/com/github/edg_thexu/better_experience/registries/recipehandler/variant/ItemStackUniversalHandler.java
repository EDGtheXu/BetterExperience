package com.github.edg_thexu.better_experience.registries.recipehandler.variant;

import com.github.edg_thexu.better_experience.registries.recipehandler.IRecipeHandler;
import com.github.edg_thexu.better_experience.registries.recipehandler.RecipeHandlerProvider;
import com.github.edg_thexu.better_experience.registries.recipehandler.RecipeHandlerProviderTypes;
import com.github.edg_thexu.better_experience.registries.recipehandler.visitor.IRecipeHandlerVisitor;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.library.gui.ingredients.RecipeSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

/**
 * jei使用自己的api时，原版的recipeManager获取不到配方，此时需要转成List&lt;Ingredient&gt;，方便和原版进行一样的方式处理
 */
public record ItemStackUniversalHandler(List<Ingredient> ingredients) implements IRecipeHandler<Ingredient> {

    public static MapCodec<ItemStackUniversalHandler> CODEC = Codec.list(Ingredient.CODEC).xmap(ItemStackUniversalHandler::new, ItemStackUniversalHandler::ingredients).fieldOf("ingredients");

    public static ItemStackUniversalHandler create(List<IRecipeSlotDrawable> slotDrawables) {
        List<Ingredient> result = new ArrayList<>();
        for(var it : slotDrawables){
            RecipeSlot slot = (RecipeSlot) it;
            if(slot.getRole() != RecipeIngredientRole.INPUT && slot.getRole() != RecipeIngredientRole.CATALYST) {
                continue;
            }
            List<ITypedIngredient<?>> ingredients = slot.getAllIngredientsList();
            List<ItemStack> itemStacks = new ArrayList<>();
            for(var ing : ingredients){
                ing.getItemStack().ifPresent(itemStacks::add);
            }
            result.add(Ingredient.of(itemStacks.stream()));
        }
        if(result.isEmpty()){
            return null;
        }
        return new ItemStackUniversalHandler(result);
    }

    @Override
    public List<Ingredient> getIngredient(Level level) {
        return ingredients;
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
        return RecipeHandlerProviderTypes.BREW_TYPE.get();
    }
}

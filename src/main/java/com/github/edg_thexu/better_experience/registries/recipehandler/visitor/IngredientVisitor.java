package com.github.edg_thexu.better_experience.registries.recipehandler.visitor;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

/**
 * 原料类型的访问者
 */
public class IngredientVisitor extends AbstractVisitor<Ingredient> {


    @Override
    public boolean match(ItemStack stack, Ingredient ing, int index){
        return ing.test(stack);
    }

    @Override
    public ItemStack[] getStacks(Ingredient ing){
        return ing.getItems();
    }

    @Override
    boolean isEmpty(Ingredient ing) {
        return ing.isEmpty();
    }

}

package com.github.edg_thexu.better_experience.registries.recipehandler.visitor;

import com.github.edg_thexu.better_experience.registries.itemmatcher.ItemStackWrapper;

import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * 原料类型的访问者
 */
public class JeiIngredientVisitor extends AbstractVisitor<List<ItemStackWrapper>> {


    @Override
    public boolean match(ItemStack stack, List<ItemStackWrapper> ing, int index){
        ItemStackWrapper typedIngredient = ing.get(index);
        return typedIngredient.test(stack);
    }

    @Override
    public ItemStack[] getStacks(List<ItemStackWrapper> ings){
        List<ItemStack> itemStacks = new ArrayList<>();
        for(var ing : ings){
            itemStacks.add(ing.normalize());
        }
        return itemStacks.toArray(new ItemStack[0]);
    }

    @Override
    boolean isEmpty(List<ItemStackWrapper> ing) {
        return false;
    }

}

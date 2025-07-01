package com.github.edg_thexu.better_experience.registries.recipehandler.visitor;

import mezz.jei.api.ingredients.ITypedIngredient;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * 原料类型的访问者
 */
public class JeiIngredientVisitor extends AbstractVisitor<List<ITypedIngredient<?>>> {


    @Override
    public boolean match(ItemStack stack, List<ITypedIngredient<?>> ing, int index){
        ITypedIngredient<?> typedIngredient = ing.get(index);
//        if(typedIngredient instanceof normalizedt)
        ItemStack normalizedStack = typedIngredient.getItemStack().orElseGet(null);
        if(normalizedStack == null){
            return true;
        }
//        if(!normalizedStack.getComponentsPatch().isEmpty()){
//            return stack.getComponentsPatch().equals(normalizedStack.getComponentsPatch());
//        }
        DataComponentPatch dataComponentPatch = stack.getComponentsPatch();
        for(var e : normalizedStack.getComponentsPatch().entrySet()){
            if(dataComponentPatch.entrySet().stream().noneMatch(f -> {
//                if(f.getKey().equals(e.getKey())){
//                    return false;
//                }
                return f.getKey().equals(e.getKey()) && f.getValue().get().equals(e.getValue().get());
            })){
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack[] getStacks(List<ITypedIngredient<?>> ings){

        List<ItemStack> itemStacks = new ArrayList<>();
        for(var ing : ings){
            ing.getItemStack().ifPresent(itemStacks::add);
        }

        return itemStacks.toArray(new ItemStack[0]);
    }

    @Override
    boolean isEmpty(List<ITypedIngredient<?>> ing) {
        return false;
    }

}

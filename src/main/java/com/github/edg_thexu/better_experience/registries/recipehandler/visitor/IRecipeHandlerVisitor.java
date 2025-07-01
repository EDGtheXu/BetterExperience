package com.github.edg_thexu.better_experience.registries.recipehandler.visitor;

import com.github.edg_thexu.better_experience.intergration.jei.SearchCache;
import com.github.edg_thexu.better_experience.registries.recipehandler.IRecipeHandler;
import net.minecraft.world.level.Level;

/**
 * 遍历每个配方时进行处理
 * @param <T> 遍历的类型
 */
public interface IRecipeHandlerVisitor<T> {

    boolean visit(Level level, SearchCache searchCache, T ingredient, IRecipeHandler<T> handler);

    IngredientVisitor ingredientVisitor = new IngredientVisitor();

    JeiIngredientVisitor jeiIngredientVisitor = new JeiIngredientVisitor();

}

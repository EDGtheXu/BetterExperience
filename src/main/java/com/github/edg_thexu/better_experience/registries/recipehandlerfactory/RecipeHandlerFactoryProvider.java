package com.github.edg_thexu.better_experience.registries.recipehandlerfactory;

import com.github.edg_thexu.better_experience.registries.recipehandler.IRecipeHandler;
import mezz.jei.library.gui.recipes.RecipeLayout;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 生成配方handler的工厂，注册后满足条件自动选用合适的handler进行原料匹配
 * @param priority 优先级，值越小越优先
 * @param match 匹配条件
 * @param factory 创建handler的工厂
 */
public record RecipeHandlerFactoryProvider(int priority, Predicate<Object> match, Function<RecipeLayout<?>, IRecipeHandler<?>> factory) {


    public boolean match(Object recipe){
        return match.test(recipe);
    }

    public IRecipeHandler<?> create(RecipeLayout<?> layout){
        return factory.apply(layout);
    }
}

package com.github.edg_thexu.better_experience.registries.recipehandlerfactory;

import com.github.edg_thexu.better_experience.Better_experience;
import com.github.edg_thexu.better_experience.intergration.jei.JeiHelper;
import com.github.edg_thexu.better_experience.intergration.jei.JeiRegistries;
import com.github.edg_thexu.better_experience.registries.recipehandler.IRecipeHandler;
import com.github.edg_thexu.better_experience.registries.recipehandler.variant.ItemStackUniversalHandler;
import com.github.edg_thexu.better_experience.registries.recipehandler.variant.VanillaRecipeHolderHandler;
import mezz.jei.library.gui.recipes.RecipeLayout;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;


public class RecipeHandlerFactoryProviderTypes {
    public static final DeferredRegister<RecipeHandlerFactoryProvider> TYPES = DeferredRegister.create(JeiRegistries.RecipeHandlerFactoryProviders.REGISTRY, Better_experience.MODID);

    public static final Supplier<RecipeHandlerFactoryProvider> VanillaRecipe = registerDecorator(register("vanilla", 0,
            r->r instanceof RecipeHolder,
            layout->new VanillaRecipeHolderHandler(((RecipeHolder)layout.getRecipe()).id())
    ), JeiHelper::isLoaded);

    public static final Supplier<RecipeHandlerFactoryProvider> BrewRecipe = registerDecorator(register("universal", 1,
            r->!(r instanceof RecipeHolder),
            layout-> ItemStackUniversalHandler.create(layout.getRecipeSlots().getSlots())
    ), JeiHelper::isLoaded);


    private static Supplier<RecipeHandlerFactoryProvider> register(String name, int priority, Predicate<Object> match, Function<RecipeLayout<?>, IRecipeHandler<?>> factory) {
        return TYPES.register(name, ()->new RecipeHandlerFactoryProvider(priority, match, factory));
    }

    private static Supplier<RecipeHandlerFactoryProvider> registerDecorator (Supplier<RecipeHandlerFactoryProvider> supplier, Supplier<Boolean> condiction) {
        if(condiction.get()){
            return supplier;
        }
        else{
            return ()->null;
        }
//        return TYPES.register(name, ()->new RecipeHandlerFactoryProvider(priority, match, factory));
    }
}

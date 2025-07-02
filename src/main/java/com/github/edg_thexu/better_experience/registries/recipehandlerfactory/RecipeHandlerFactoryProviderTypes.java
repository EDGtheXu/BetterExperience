package com.github.edg_thexu.better_experience.registries.recipehandlerfactory;

import com.github.edg_thexu.better_experience.Better_experience;
import com.github.edg_thexu.better_experience.intergration.jei.JeiHelper;
import com.github.edg_thexu.better_experience.intergration.jei.JeiRegistries;
import com.github.edg_thexu.better_experience.registries.recipehandler.IRecipeHandler;
import com.github.edg_thexu.better_experience.registries.recipehandler.RecipeHandlerProvider;
import com.github.edg_thexu.better_experience.registries.recipehandler.variant.ItemStackUniversalHandler;
import com.github.edg_thexu.better_experience.registries.recipehandler.variant.VanillaRecipeHolderHandler;
import mezz.jei.library.gui.recipes.RecipeLayout;
import net.minecraft.world.inventory.RecipeHolder;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;


import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;


public class RecipeHandlerFactoryProviderTypes {
    public static final DeferredRegister<RecipeHandlerFactoryProvider> TYPES = DeferredRegister.create(JeiRegistries.RecipeHandlerFactoryProviders.KEY, Better_experience.MODID);
    public static final Supplier<IForgeRegistry<RecipeHandlerFactoryProvider>> REGISTRY = TYPES.makeRegistry(RegistryBuilder::new);

    public static final Supplier<RecipeHandlerFactoryProvider> VanillaRecipe = registerDecorator(register("vanilla", 0,
            r->r instanceof RecipeHolder,
            (layout, count)->new VanillaRecipeHolderHandler(((RecipeHolder)layout.getRecipe()).getRecipeUsed().getId(), count)
    ), JeiHelper::isLoaded);

    public static final Supplier<RecipeHandlerFactoryProvider> UniversalRecipe = registerDecorator(register("universal", 1,
            r->!(r instanceof RecipeHolder),
            (layout, count)-> ItemStackUniversalHandler.create(layout.getRecipeSlots().getSlots(), count)
    ), JeiHelper::isLoaded);


    private static Supplier<RecipeHandlerFactoryProvider> register(String name, int priority, Predicate<Object> match, BiFunction<RecipeLayout<?>, Integer, IRecipeHandler<?>> factory) {
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

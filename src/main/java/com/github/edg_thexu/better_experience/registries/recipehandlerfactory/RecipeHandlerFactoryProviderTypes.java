package com.github.edg_thexu.better_experience.registries.recipehandlerfactory;

import com.github.edg_thexu.better_experience.Better_experience;
import com.github.edg_thexu.better_experience.intergration.jei.JeiHelper;
import com.github.edg_thexu.better_experience.intergration.jei.JeiRegistries;
import com.github.edg_thexu.better_experience.registries.itemmatcher.IItemMatcher;
import com.github.edg_thexu.better_experience.registries.itemmatcher.ItemStackWrapper;
import com.github.edg_thexu.better_experience.registries.itemmatcher.variant.EnchantBookMatcher;
import com.github.edg_thexu.better_experience.registries.itemmatcher.variant.EnchantToolMatcher;
import com.github.edg_thexu.better_experience.registries.itemmatcher.variant.IdCountMatcher;
import com.github.edg_thexu.better_experience.registries.itemmatcher.variant.PotionMatcher;
import com.github.edg_thexu.better_experience.registries.recipehandler.IRecipeHandler;
import com.github.edg_thexu.better_experience.registries.recipehandler.variant.ItemStackUniversalHandler;
import com.github.edg_thexu.better_experience.registries.recipehandler.variant.VanillaRecipeHolderHandler;
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.library.gui.recipes.RecipeLayout;
import mezz.jei.library.plugins.vanilla.anvil.AnvilRecipe;
import mezz.jei.library.plugins.vanilla.brewing.JeiBrewingRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.apache.commons.lang3.function.TriFunction;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;


public class RecipeHandlerFactoryProviderTypes {
    public static final DeferredRegister<RecipeHandlerFactoryProvider> TYPES = DeferredRegister.create(JeiRegistries.RecipeHandlerFactoryProviders.REGISTRY, Better_experience.MODID);


    static BiFunction<IRecipeSlotDrawable, IItemMatcher, List<ItemStackWrapper>> toWrapper = (drawable, matcher)-> drawable.getAllIngredientsList().stream().map(i->new ItemStackWrapper(i, matcher)).toList();

    public static final Supplier<RecipeHandlerFactoryProvider> VanillaRecipe = registerDecoratorJei(register("vanilla", 0,
            r->r instanceof RecipeHolder,
            (layout, recipe, count)->new VanillaRecipeHolderHandler(((RecipeHolder)layout.getRecipe()).id(), count)
    ));

    // jei酿造配方
    public static final Supplier<RecipeHandlerFactoryProvider> BrewRecipe = registerDecoratorJei(register("brew", 1,
            r->r instanceof JeiBrewingRecipe,
            (layout, recipe, count)-> {
                JeiBrewingRecipe recipe1 = (JeiBrewingRecipe) recipe;

                List<IRecipeSlotDrawable> slots = layout.getRecipeSlots().getSlots(RecipeIngredientRole.INPUT);
                List<ItemStackWrapper> p1 = toWrapper.apply(slots.get(0), PotionMatcher.INSTANCE);
                List<ItemStackWrapper> p2 = toWrapper.apply(slots.get(3), PotionMatcher.INSTANCE);

                return new ItemStackUniversalHandler(List.of(p1,p1,p1, p2),count);
            }
    ));

    // jei铁砧配方
    public static final Supplier<RecipeHandlerFactoryProvider> AnvilRecipe = registerDecoratorJei(register("anvil", 1,
            r->r instanceof AnvilRecipe,
            (layout, recipe, count)-> {

                AnvilRecipe recipe1 = (AnvilRecipe) recipe;
                ItemStack l = recipe1.leftInputs().get(0);
                ItemStack r = recipe1.rightInputs().get(0);
                List<IRecipeSlotDrawable> slots = layout.getRecipeSlots().getSlots(RecipeIngredientRole.INPUT);

                List<ItemStackWrapper> p1;
                List<ItemStackWrapper> p2;

                if(l.is(Items.ENCHANTED_BOOK) && r.is(Items.ENCHANTED_BOOK)){
                    // 附魔书+附魔书
                    p1 = toWrapper.apply(slots.get(0), EnchantBookMatcher.INSTANCE);
                    p2 = toWrapper.apply(slots.get(1), EnchantBookMatcher.INSTANCE);
                }else if(!l.is(Items.ENCHANTED_BOOK) && r.is(Items.ENCHANTED_BOOK)){
                    // 工具+附魔书
                    p1 = toWrapper.apply(slots.get(0), new EnchantToolMatcher(r.getComponentsPatch()));
                    p2 = toWrapper.apply(slots.get(1), EnchantBookMatcher.INSTANCE);
                }else{
                    // 工具修理
                    p1 = toWrapper.apply(slots.get(0), IdCountMatcher.INSTANCE);
                    p2 = toWrapper.apply(slots.get(1), IdCountMatcher.INSTANCE);
                }

                return new ItemStackUniversalHandler(List.of(p1,p2),count);
            }
    ));

    public static final Supplier<RecipeHandlerFactoryProvider> UniversalRecipe = registerDecoratorJei(register("universal", 10,
            r->true,
            (layout, recipe, count)-> ItemStackUniversalHandler.create(layout.getRecipeSlots().getSlots(), count)
    ));


    private static Supplier<RecipeHandlerFactoryProvider> register(String name, int priority, Predicate<Object> match, TriFunction<RecipeLayout<?>,Object,  Integer, IRecipeHandler<?>> factory) {
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
    private static Supplier<RecipeHandlerFactoryProvider> registerDecoratorJei (Supplier<RecipeHandlerFactoryProvider> supplier) {
        return registerDecorator(supplier, JeiHelper::isLoaded);
    }
}

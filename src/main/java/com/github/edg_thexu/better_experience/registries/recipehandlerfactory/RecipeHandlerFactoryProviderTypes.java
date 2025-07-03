package com.github.edg_thexu.better_experience.registries.recipehandlerfactory;

import com.github.edg_thexu.better_experience.Better_experience;
import com.github.edg_thexu.better_experience.intergration.jei.JeiHelper;
import com.github.edg_thexu.better_experience.intergration.jei.JeiRegistries;
import com.github.edg_thexu.better_experience.registries.itemmatcher.ItemStackWrapper;
import com.github.edg_thexu.better_experience.registries.itemmatcher.variant.EnchantBookMatcher;
import com.github.edg_thexu.better_experience.registries.itemmatcher.variant.EnchantToolMatcher;
import com.github.edg_thexu.better_experience.registries.itemmatcher.variant.IdCountMatcher;
import com.github.edg_thexu.better_experience.registries.itemmatcher.variant.PotionMatcher;
import com.github.edg_thexu.better_experience.registries.recipehandler.IRecipeHandler;
import com.github.edg_thexu.better_experience.registries.recipehandler.variant.ItemStackUniversalHandler;
import com.github.edg_thexu.better_experience.registries.recipehandler.variant.VanillaRecipeHolderHandler;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.library.gui.recipes.RecipeLayout;
import mezz.jei.library.plugins.vanilla.anvil.AnvilRecipe;
import mezz.jei.library.plugins.vanilla.brewing.JeiBrewingRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.SmithingRecipe;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import org.apache.commons.lang3.function.TriFunction;


import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;


public class RecipeHandlerFactoryProviderTypes {
    public static final DeferredRegister<RecipeHandlerFactoryProvider> TYPES = DeferredRegister.create(JeiRegistries.RecipeHandlerFactoryProviders.KEY, Better_experience.MODID);
    public static final Supplier<IForgeRegistry<RecipeHandlerFactoryProvider>> REGISTRY = TYPES.makeRegistry(RegistryBuilder::new);

    // 使用原版的配方
    public static final Supplier<RecipeHandlerFactoryProvider> VanillaRecipe = registerDecoratorJei(register("vanilla", 0,
            r->r instanceof Recipe<?> && !(r instanceof SmithingRecipe),
            (layout, recipe, count)->new VanillaRecipeHolderHandler(((Recipe)layout.getRecipe()).getId(), count)
    ));

    // jei酿造配方
    public static final Supplier<RecipeHandlerFactoryProvider> BrewRecipe = registerDecoratorJei(register("brew", 1,
            r->r instanceof JeiBrewingRecipe,
            (layout, recipe, count)-> {
                JeiBrewingRecipe recipe1 = (JeiBrewingRecipe) recipe;
                List<ItemStack> ings = recipe1.getIngredients();
                List<ItemStack> potions = recipe1.getPotionInputs();
                var p1 = ItemStackWrapper.fromList(potions, PotionMatcher.INSTANCE, true);
                return new ItemStackUniversalHandler(List.of(
                        ItemStackWrapper.fromList(ings, IdCountMatcher.INSTANCE, false),
                        p1,p1,p1),count);
            }
    ));

    static Function<ItemStack, ItemStackWrapper> idCountFunc = ItemStackWrapper::ofIdCount;
    static Function<ItemStack, ItemStackWrapper> enchantBookFunc = stack-> ItemStackWrapper.ofFull(stack, EnchantBookMatcher.INSTANCE);
    static Function<ItemStack, ItemStackWrapper> enchantToolFunc = stack-> ItemStackWrapper.ofFull(stack, EnchantToolMatcher.INSTANCE);


    // jei铁砧配方
    public static final Supplier<RecipeHandlerFactoryProvider> AnvilRecipe = registerDecoratorJei(register("anvil", 1,
            r->r instanceof AnvilRecipe,
            (layout, recipe, count)-> {

                AnvilRecipe recipe1 = (AnvilRecipe) recipe;
                ItemStack l = recipe1.leftInputs().get(0);
                ItemStack r = recipe1.rightInputs().get(0);
                Function<ItemStack, ItemStackWrapper> f1;
                Function<ItemStack, ItemStackWrapper> f2;

                boolean isEnchant = false;
                if(l.is(Items.ENCHANTED_BOOK) && r.is(Items.ENCHANTED_BOOK)){
                    // 附魔书+附魔书
                    f1 = f2 = enchantBookFunc;
                }else if(!l.is(Items.ENCHANTED_BOOK) && r.is(Items.ENCHANTED_BOOK)){
                    // 工具+附魔书
                    f1 = enchantToolFunc;
                    f2 = enchantBookFunc;
                    isEnchant = true;
                }else{
                    // 工具修理
                    f1 = f2 = idCountFunc;
                }
                var a = layout.getRecipeSlots().getSlots(RecipeIngredientRole.INPUT);
                ItemStack left_it = a.get(0).getDisplayedItemStack().orElse(ItemStack.EMPTY).copy();
                ItemStack right_it = a.get(1).getDisplayedItemStack().orElse(ItemStack.EMPTY).copy();
                if(isEnchant){
                    if (right_it.getTag() != null) {
                        EnchantmentHelper.setEnchantments(EnchantmentHelper.deserializeEnchantments(right_it.getTag().getList("StoredEnchantments", 10)), left_it);
                    }
                }
                List<ItemStackWrapper> left = Stream.of(left_it).map(f1).toList();
                List<ItemStackWrapper> right = Stream.of(right_it).map(f2).toList();

                return new ItemStackUniversalHandler(List.of(left, right),count);
            }
    ));

    // 通用不匹配tag配方
    public static final Supplier<RecipeHandlerFactoryProvider> UniversalRecipe = registerDecoratorJei(register("universal", 10,
            r->true,
            (layout, recipe, count)-> ItemStackUniversalHandler.create(layout.getRecipeSlots().getSlots(),(ing, index)-> ItemStackWrapper.ofIdCount(ing.getItemStack().get()), count)
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

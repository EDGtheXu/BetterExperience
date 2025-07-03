package com.github.edg_thexu.better_experience.registries.itemmatcher;

import com.github.edg_thexu.better_experience.Better_experience;
import com.github.edg_thexu.better_experience.intergration.jei.JeiRegistries;
import com.github.edg_thexu.better_experience.registries.itemmatcher.variant.EnchantBookMatcher;
import com.github.edg_thexu.better_experience.registries.itemmatcher.variant.EnchantToolMatcher;
import com.github.edg_thexu.better_experience.registries.itemmatcher.variant.IdCountMatcher;
import com.github.edg_thexu.better_experience.registries.itemmatcher.variant.PotionMatcher;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;


public class ItemMatcherTypes {
    public static final DeferredRegister<IItemMatcher> TYPES = DeferredRegister.create(JeiRegistries.ItemMatcherProviders.KEY, Better_experience.MODID);
    public static final Supplier<IForgeRegistry<IItemMatcher>> REGISTRY = TYPES.makeRegistry(RegistryBuilder::new);

    public static final Supplier<IItemMatcher> VANILLA_TYPE = register("id_count", ()-> IdCountMatcher.INSTANCE);
    public static final Supplier<IItemMatcher> POTION_TYPE = register("potion", ()-> PotionMatcher.INSTANCE);
    public static final Supplier<IItemMatcher> ENCHANT_BOOK_TYPE = register("enchant_book", ()-> EnchantBookMatcher.INSTANCE);
    public static final Supplier<IItemMatcher> ENCHANT_TOOL_TYPE = register("enchant_tool", ()-> EnchantToolMatcher.INSTANCE);


    private static Supplier<IItemMatcher> register(String name, Supplier<IItemMatcher> matcher) {
        return TYPES.register(name, matcher);
    }




}

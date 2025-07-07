package com.github.edg_thexu.better_experience.registries.itemmatcher;

import com.github.edg_thexu.better_experience.Better_experience;
import com.github.edg_thexu.better_experience.intergration.jei.JeiRegistries;
import com.github.edg_thexu.better_experience.registries.itemmatcher.variant.EnchantBookMatcher;
import com.github.edg_thexu.better_experience.registries.itemmatcher.variant.EnchantToolMatcher;
import com.github.edg_thexu.better_experience.registries.itemmatcher.variant.IdCountMatcher;
import com.github.edg_thexu.better_experience.registries.itemmatcher.variant.PotionMatcher;
import com.mojang.serialization.MapCodec;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;


public class ItemMatcherTypes {
    public static final DeferredRegister<ItemMatcherProvider> TYPES = DeferredRegister.create(JeiRegistries.ItemMatcherProviders.KEY, Better_experience.MODID);

    public static final Supplier<ItemMatcherProvider> VANILLA_TYPE = register("id_count", IdCountMatcher.CODEC);
    public static final Supplier<ItemMatcherProvider> POTION_TYPE = register("potion", PotionMatcher.CODEC);
    public static final Supplier<ItemMatcherProvider> ENCHANT_BOOK_TYPE = register("enchant_book", EnchantBookMatcher.CODEC);
    public static final Supplier<ItemMatcherProvider> ENCHANT_TOOL_TYPE = register("enchant_tool", EnchantToolMatcher.CODEC);


    private static Supplier<ItemMatcherProvider> register(String name, MapCodec<? extends IItemMatcher> codec) {
        return TYPES.register(name, ()->new ItemMatcherProvider(codec));
    }




}

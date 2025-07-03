package com.github.edg_thexu.better_experience.registries.itemmatcher;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.item.ItemStack;

public interface IItemMatcher {

    boolean matches(ItemStackWrapper wrapper, ItemStack stack);

    MapCodec<? extends IItemMatcher> codec();

    ItemStack normalize(ItemStackWrapper wrapper);

    Codec<IItemMatcher> TYPE_CODEC = ItemMatcherTypes.REGISTRY.get().getCodec();

}

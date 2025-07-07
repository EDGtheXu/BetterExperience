package com.github.edg_thexu.better_experience.registries.itemmatcher.variant;

import com.github.edg_thexu.better_experience.registries.itemmatcher.IItemMatcher;
import com.github.edg_thexu.better_experience.registries.itemmatcher.ItemMatcherProvider;
import com.github.edg_thexu.better_experience.registries.itemmatcher.ItemMatcherTypes;
import com.github.edg_thexu.better_experience.registries.itemmatcher.ItemStackWrapper;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.item.ItemStack;

public class IdCountMatcher implements IItemMatcher {

    public static final IdCountMatcher INSTANCE = new IdCountMatcher();
    public static MapCodec<IdCountMatcher> CODEC = Codec.of(Encoder.empty(), Decoder.unit(INSTANCE));

    @Override
    public boolean matches(ItemStackWrapper wrapper, ItemStack stack) {
        return wrapper.getItem() == stack.getItem() && wrapper.getCount() <= stack.getCount();
    }

    @Override
    public ItemMatcherProvider provider() {
        return ItemMatcherTypes.VANILLA_TYPE.get();
    }
}

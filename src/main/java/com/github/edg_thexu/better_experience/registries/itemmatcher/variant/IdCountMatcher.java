package com.github.edg_thexu.better_experience.registries.itemmatcher.variant;

import com.github.edg_thexu.better_experience.registries.itemmatcher.IItemMatcher;
import com.github.edg_thexu.better_experience.registries.itemmatcher.ItemStackWrapper;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.item.ItemStack;

public class IdCountMatcher implements IItemMatcher {

    public static final IdCountMatcher INSTANCE = new IdCountMatcher();
    static MapCodec<IdCountMatcher> CODEC = Codec.of(Encoder.empty(), Decoder.unit(INSTANCE));
    @Override
    public boolean matches(ItemStackWrapper wrapper, ItemStack stack) {
        return wrapper.item == stack.getItem() && wrapper.count <= stack.getCount();
    }

    public ItemStack normalize(ItemStackWrapper wrapper){
        return new ItemStack(wrapper.item, wrapper.count);
    }

    @Override
    public MapCodec<? extends IItemMatcher> codec() {
        return CODEC;
    }
}

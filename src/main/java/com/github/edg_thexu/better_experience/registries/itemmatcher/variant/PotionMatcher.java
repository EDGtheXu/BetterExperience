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

public class PotionMatcher implements IItemMatcher {

    public static final PotionMatcher INSTANCE = new PotionMatcher();
    public static MapCodec<PotionMatcher> CODEC = Codec.of(Encoder.empty(), Decoder.unit(INSTANCE));

    @Override
    public boolean matches(ItemStackWrapper wrapper, ItemStack stack) {
        ItemStack need = wrapper.ingredients().getItemStack().orElse(ItemStack.EMPTY);
        return need.getComponentsPatch().equals(stack.getComponentsPatch());
    }

    @Override
    public ItemMatcherProvider provider() {
        return ItemMatcherTypes.POTION_TYPE.get();
    }


}

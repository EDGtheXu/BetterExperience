package com.github.edg_thexu.better_experience.registries.itemmatcher.variant;

import com.github.edg_thexu.better_experience.registries.itemmatcher.IItemMatcher;
import com.github.edg_thexu.better_experience.registries.itemmatcher.ItemStackWrapper;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;

public class PotionMatcher implements IItemMatcher {

    public static final PotionMatcher INSTANCE = new PotionMatcher();
    static MapCodec<PotionMatcher> CODEC = Codec.of(Encoder.empty(), Decoder.unit(INSTANCE));

    @Override
    public boolean matches(ItemStackWrapper wrapper, ItemStack stack) {
        if(!(wrapper.item == stack.getItem() && wrapper.count == stack.getCount())){
            return false;
        }
        Potion potion = PotionUtils.getPotion(stack);
        if(wrapper.tag == null){
            return potion == Potions.EMPTY;
        }

        return PotionUtils.getPotion(wrapper.tag) == potion;
    }

    public ItemStack normalize(ItemStackWrapper wrapper){
        ItemStack stack = Items.POTION.getDefaultInstance();
        PotionUtils.setPotion(stack, PotionUtils.getPotion(wrapper.tag));
        return stack;
    }

    @Override
    public MapCodec<? extends IItemMatcher> codec() {
        return CODEC;
    }
}

package com.github.edg_thexu.better_experience.registries.itemmatcher.variant;

import com.github.edg_thexu.better_experience.registries.itemmatcher.IItemMatcher;
import com.github.edg_thexu.better_experience.registries.itemmatcher.ItemStackWrapper;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.Map;
import java.util.Objects;

public class EnchantBookMatcher implements IItemMatcher {

    public static final EnchantBookMatcher INSTANCE = new EnchantBookMatcher();
    static MapCodec<EnchantBookMatcher> CODEC = Codec.of(Encoder.empty(), Decoder.unit(INSTANCE));

    @Override
    public boolean matches(ItemStackWrapper wrapper, ItemStack stack) {
        if(!(wrapper.item == stack.getItem() && wrapper.count == stack.getCount())){
            return false;
        }
        Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack);
        if(wrapper.tag == null){
            return true;
        }
        Map<Enchantment, Integer> map2 = EnchantmentHelper.deserializeEnchantments(wrapper.tag.getList("StoredEnchantments", 10));
        for(Map.Entry<Enchantment, Integer> entry : map2.entrySet()){
            if(!map.containsKey(entry.getKey())){
                return false;
            }
            int level = map.get(entry.getKey());
            if(entry.getValue() != level){
                return false;
            }
        }
        return true;
    }

    public ItemStack normalize(ItemStackWrapper wrapper){
        ItemStack stack = Items.ENCHANTED_BOOK.getDefaultInstance();
        if(wrapper.tag == null){
            return stack;
        }
        EnchantmentHelper.setEnchantments(EnchantmentHelper.deserializeEnchantments(wrapper.tag.getList("StoredEnchantments", 10)), stack);
        return stack;
    }

    @Override
    public MapCodec<? extends IItemMatcher> codec() {
        return CODEC;
    }
}

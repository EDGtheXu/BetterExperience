package com.github.edg_thexu.better_experience.registries.itemmatcher.variant;

import com.github.edg_thexu.better_experience.registries.itemmatcher.IItemMatcher;
import com.github.edg_thexu.better_experience.registries.itemmatcher.ItemStackWrapper;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.Map;

public class EnchantToolMatcher implements IItemMatcher {

    public static final EnchantToolMatcher INSTANCE = new EnchantToolMatcher();
    static MapCodec<EnchantToolMatcher> CODEC = Codec.of(Encoder.empty(), Decoder.unit(INSTANCE));

    @Override
    public boolean matches(ItemStackWrapper wrapper, ItemStack stack) {
        if(!(wrapper.item == stack.getItem() && wrapper.count == stack.getCount())){
            return false;
        }
        Map<Enchantment, Integer> map = stack.getAllEnchantments();
        if(wrapper.tag == null){
            return true;
        }
        if(map.isEmpty()){
            return true;
        }
        Map<Enchantment, Integer> map2 = EnchantmentHelper.deserializeEnchantments(wrapper.tag.getList("Enchantments", 10));
        for(Map.Entry<Enchantment, Integer> entry : map2.entrySet()){
            if(!map.containsKey(entry.getKey())){
                return true;
            }
            int level = map.get(entry.getKey());
            if(entry.getValue() > level){
                return true;
            }
        }
        return false;
    }

    @Override
    public ItemStack normalize(ItemStackWrapper wrapper){
        return wrapper.toStack();
    }

    @Override
    public MapCodec<? extends IItemMatcher> codec() {
        return CODEC;
    }
}

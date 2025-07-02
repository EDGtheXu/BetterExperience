package com.github.edg_thexu.better_experience.utils;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

public final class EnchantmentUtil {

    public static int getEnchantmentLevel(Enchantment enchantment, ItemStack stack) {
        return stack.getEnchantmentLevel(enchantment);
    }
}

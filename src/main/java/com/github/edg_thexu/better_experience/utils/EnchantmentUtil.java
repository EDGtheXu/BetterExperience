package com.github.edg_thexu.better_experience.utils;

import com.github.edg_thexu.better_experience.intergration.confluence_lib.ConfluenceLibHelper;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;

import java.util.Iterator;

public final class EnchantmentUtil {

    public static int getEnchantmentLevel(ResourceKey<Enchantment> enchantments, ItemStack stack) {
        if(ConfluenceLibHelper.isLoaded()){
            return org.confluence.lib.util.EnchantmentUtil.getEnchantmentLevel(enchantments, stack);
        }
        if (stack != null && !stack.isEmpty()) {
            ItemEnchantments itemenchantments = stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
            Iterator var3 = itemenchantments.entrySet().iterator();

            Object2IntMap.Entry entry;
            do {
                if (!var3.hasNext()) {
                    return 0;
                }

                entry = (Object2IntMap.Entry)var3.next();
            } while(!((Holder)entry.getKey()).is(enchantments));

            return entry.getIntValue();
        } else {
            return 0;
        }
    }
}

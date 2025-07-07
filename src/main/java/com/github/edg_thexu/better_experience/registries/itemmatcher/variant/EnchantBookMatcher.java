package com.github.edg_thexu.better_experience.registries.itemmatcher.variant;

import com.github.edg_thexu.better_experience.registries.itemmatcher.IItemMatcher;
import com.github.edg_thexu.better_experience.registries.itemmatcher.ItemMatcherProvider;
import com.github.edg_thexu.better_experience.registries.itemmatcher.ItemMatcherTypes;
import com.github.edg_thexu.better_experience.registries.itemmatcher.ItemStackWrapper;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.MapCodec;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;

public class EnchantBookMatcher implements IItemMatcher {

    public static final EnchantBookMatcher INSTANCE = new EnchantBookMatcher();
    public static MapCodec<EnchantBookMatcher> CODEC = Codec.of(Encoder.empty(), Decoder.unit(INSTANCE));

    @Override
    public boolean matches(ItemStackWrapper wrapper, ItemStack stack) {
        if(!(wrapper.getItem() == stack.getItem() && wrapper.getCount() == stack.getCount())){
            return false;
        }

        var patch = stack.getComponentsPatch().get(DataComponents.STORED_ENCHANTMENTS);
        if(patch == null){
            return false;
        }
        ItemEnchantments map = patch.orElse(null);
        if(map == null){
            return false;
        }
        patch = wrapper.ingredients().getItemStack().orElse(ItemStack.EMPTY).getComponentsPatch().get(DataComponents.STORED_ENCHANTMENTS);
        if(patch == null){
            return true;
        }
        ItemEnchantments map2 = patch.orElse(null);
        if(map2 == null){
            return true;
        }
        for(Object2IntMap.Entry<Holder<Enchantment>> entry : map2.entrySet()){
            if(!map.keySet().contains(entry.getKey())){
                return false;
            }
            int level = map.getLevel(entry.getKey());
            if(map2.getLevel(entry.getKey()) != level){
                return false;
            }
        }
        return true;
    }


    @Override
    public ItemMatcherProvider provider() {
        return ItemMatcherTypes.ENCHANT_BOOK_TYPE.get();
    }
}

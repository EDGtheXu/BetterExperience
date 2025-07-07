package com.github.edg_thexu.better_experience.registries.itemmatcher;

import com.github.edg_thexu.better_experience.intergration.jei.JeiRegistries;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.item.ItemStack;

public interface IItemMatcher {

    boolean matches(ItemStackWrapper wrapper, ItemStack stack);

    ItemMatcherProvider provider();

    default ItemStack normalize(ItemStackWrapper wrapper){
        return wrapper.ingredient.getItemStack().orElse(ItemStack.EMPTY);
    }

    Codec<IItemMatcher> TYPE_CODEC = JeiRegistries.ItemMatcherProviders.REGISTRY.byNameCodec()
            .dispatch(IItemMatcher::provider, ItemMatcherProvider::codec);

}

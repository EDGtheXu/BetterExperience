package com.github.edg_thexu.better_experience.intergration.jei.ingredient;

import com.mojang.serialization.Codec;
import mezz.jei.api.ingredients.ITypedIngredient;
import net.minecraft.world.item.ItemStack;

public interface IITypedIngredient {

    boolean match(ItemStack stack);

    Codec<? extends ITypedIngredient<?>> getCodec();
}

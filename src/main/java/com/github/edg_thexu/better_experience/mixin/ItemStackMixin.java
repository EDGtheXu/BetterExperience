package com.github.edg_thexu.better_experience.mixin;

import com.github.edg_thexu.better_experience.config.CommonConfig;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemStack.class)
public class ItemStackMixin {

    @WrapOperation(method = "getMaxStackSize", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item;getMaxStackSize(Lnet/minecraft/world/item/ItemStack;)I"))
    public int getMaxStackSizeMixin(Item instance, ItemStack stack, Operation<Integer> original) {
        int ori = original.call(instance, stack);

        if(CommonConfig.MODIFY_MAX_STACK_SIZE.get()) {
            Item item = stack.getItem();
            if(item instanceof PotionItem) {
                return Math.max(ori, 64);
            }
            if(item instanceof ArrowItem){
                return Math.max(ori, 999);
            }
        }
        return ori;
    }
}


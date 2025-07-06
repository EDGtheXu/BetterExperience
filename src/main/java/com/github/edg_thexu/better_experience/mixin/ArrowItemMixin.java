package com.github.edg_thexu.better_experience.mixin;

import com.github.edg_thexu.better_experience.config.CommonConfig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ArrowItem.class, remap = false)
public class ArrowItemMixin {

    @Inject(method = "isInfinite", at = @At("HEAD"), cancellable = true)
    private void isInfiniteMixin(ItemStack stack, ItemStack bow, Player player, CallbackInfoReturnable<Boolean> cir) {
        if(CommonConfig.INFINITE_AMMO.get() && stack.getCount() >= CommonConfig.INFINITE_AMMO_STACK_SIZE.get()){
            cir.setReturnValue(true);
            cir.cancel();
        }
    }
}

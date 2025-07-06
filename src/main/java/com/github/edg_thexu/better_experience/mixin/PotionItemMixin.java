package com.github.edg_thexu.better_experience.mixin;

import com.github.edg_thexu.better_experience.config.CommonConfig;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PotionItem.class)
public class PotionItemMixin {
    @Inject(method = "getUseDuration", at = @At("HEAD"), cancellable = true)
    private void getUseDurationMixin(ItemStack pStack, CallbackInfoReturnable<Integer> cir) {
        if(CommonConfig.INSTANTLY_DRINK.get()){
            cir.setReturnValue(5);
            cir.cancel();
        }
    }

}

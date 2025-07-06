package com.github.edg_thexu.better_experience.mixin;

import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Slot.class)
public class SlotMixin {

//    @Inject(method = "getMaxStackSize()I", at = @At("HEAD"), cancellable = true)
//    public void getMaxStackSizeMixin(CallbackInfoReturnable<Integer> cir) {
//        cir.setReturnValue(999);
//        cir.cancel();
//    }
}

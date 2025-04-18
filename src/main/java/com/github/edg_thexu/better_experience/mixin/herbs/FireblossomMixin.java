package com.github.edg_thexu.better_experience.mixin.herbs;

import com.github.edg_thexu.better_experience.config.CommonConfig;
import org.confluence.mod.common.block.natural.herbs.Fireblossom;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Fireblossom.class)
public class FireblossomMixin {
    @Inject(method = "canBloom", at = @At("HEAD"), cancellable = true)
    private void canBloom(CallbackInfoReturnable<Boolean> cir) {
        if(CommonConfig.HERB_GROWTH_NO_STRICT.get()) {
            cir.setReturnValue(true);
            cir.cancel();
        }
    }
}

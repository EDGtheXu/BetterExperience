package com.github.edg_thexu.better_experience.mixin.integration.confluence.herbs;

import com.github.edg_thexu.better_experience.config.CommonConfig;
import org.confluence.mod.common.block.natural.herbs.Waterleaf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
@Pseudo
@Mixin(targets = "org.confluence.mod.common.block.natural.herbs.Waterleaf")
//@Mixin(Waterleaf.class)
public class WaterleafMixin {

    @Inject(method = "canBloom", at = @At("HEAD"), cancellable = true)
    private void canBloom(CallbackInfoReturnable<Boolean> cir) {
        if(CommonConfig.HERB_GROWTH_NO_STRICT.get()) {
            cir.setReturnValue(true);
            cir.cancel();
        }
    }
}

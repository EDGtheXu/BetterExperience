package com.github.edg_thexu.better_experience.mixin.herbs;

import com.github.edg_thexu.better_experience.config.ServerConfig;
import org.confluence.mod.common.block.natural.herbs.Daybloom;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Daybloom.class)
public class DaybloomMixin {

    @Inject(method = "canBloom", at = @At("HEAD"), cancellable = true)
    private void canBloom(CallbackInfoReturnable<Boolean> cir) {
        if(ServerConfig.HERB_GROWTH_NO_STRICT.get()) {
            cir.setReturnValue(true);
            cir.cancel();
        }
    }
}

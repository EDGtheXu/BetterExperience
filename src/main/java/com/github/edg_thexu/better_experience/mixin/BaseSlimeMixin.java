package com.github.edg_thexu.better_experience.mixin;

import com.github.edg_thexu.better_experience.config.ServerConfig;
import org.confluence.terraentity.entity.monster.slime.BaseSlime;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BaseSlime.class)
public class BaseSlimeMixin {

    @Inject(method = "tickDeath", at = @At(value = "INVOKE", target = "Lorg/confluence/terraentity/entity/monster/slime/BaseSlime;level()Lnet/minecraft/world/level/Level;"), cancellable = true)
    private void tickDeathMixin(CallbackInfo ci) {
        if(ServerConfig.SLIME_DIE_NO_LAVA.get())
            ci.cancel();
    }
}

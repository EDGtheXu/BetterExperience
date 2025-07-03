package com.github.edg_thexu.better_experience.mixin.integration.terra_entity;

import com.github.edg_thexu.better_experience.config.CommonConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Pseudo
@Mixin(targets = "org.confluence.terraentity.entity.monster.slime.BaseSlime")
//@Mixin(BaseSlime.class)
public class BaseSlimeMixin {

    @Inject(method = "tickDeath", at = @At(value = "INVOKE", target = "Lorg/confluence/terraentity/entity/monster/slime/BaseSlime;level()Lnet/minecraft/world/level/Level;"), cancellable = true)
    private void tickDeathMixin(CallbackInfo ci) {
        if(CommonConfig.SLIME_DIE_NO_LAVA.get())
            ci.cancel();
    }
}

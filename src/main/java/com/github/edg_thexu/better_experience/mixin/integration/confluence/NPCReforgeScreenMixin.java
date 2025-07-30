package com.github.edg_thexu.better_experience.mixin.integration.confluence;

import com.github.edg_thexu.better_experience.module.reforge.BetterReforgeManager;
import org.confluence.mod.client.gui.container.NPCReforgeScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NPCReforgeScreen.class)
public class NPCReforgeScreenMixin {

    @Inject(method = "<init>", at = @At("RETURN"))
    private void initMixin(CallbackInfo ci) {
        BetterReforgeManager.initButton((NPCReforgeScreen) (Object) this);
    }

    @Inject(method = "init", at = @At("RETURN"))
    private void init(CallbackInfo ci) {
        BetterReforgeManager.init((NPCReforgeScreen) (Object) this);

    }

}

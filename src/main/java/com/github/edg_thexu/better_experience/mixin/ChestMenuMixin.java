package com.github.edg_thexu.better_experience.mixin;

import com.github.edg_thexu.better_experience.attachment.EnderChestAttachment;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChestMenu.class)
public class ChestMenuMixin {

    @Inject(method = "removed", at = @At("HEAD"))
    private void onRemoved(Player player, CallbackInfo ci) {

        if(player instanceof ServerPlayer sp)
            EnderChestAttachment.sync(sp);
    }
}

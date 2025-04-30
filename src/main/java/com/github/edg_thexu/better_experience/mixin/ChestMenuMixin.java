package com.github.edg_thexu.better_experience.mixin;

import com.github.edg_thexu.better_experience.attachment.EnderChestAttachment;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.PlayerEnderChestContainer;
import org.confluence.mod.common.attachment.PlayerPiggyBankContainer;
import org.confluence.mod.common.attachment.PlayerSafeContainer;
import org.confluence.mod.common.block.functional.PiggyBankBlock;
import org.confluence.mod.common.block.functional.SafeBlock;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChestMenu.class)
public abstract class ChestMenuMixin {

    @Shadow public abstract Container getContainer();

    @Shadow @Final private Container container;

    @Inject(method = "removed", at = @At("HEAD"))
    private void onRemoved(Player player, CallbackInfo ci) {
        if(player instanceof ServerPlayer sp) {
            Container container = this.getContainer();
            if(container instanceof PlayerEnderChestContainer) {
                EnderChestAttachment.syncEnderChest(sp);
            }else if(container instanceof PlayerPiggyBankContainer){
                EnderChestAttachment.syncPig(sp);
            }else if(container instanceof PlayerSafeContainer){
                EnderChestAttachment.syncSafe(sp);
            }
        }
    }
}

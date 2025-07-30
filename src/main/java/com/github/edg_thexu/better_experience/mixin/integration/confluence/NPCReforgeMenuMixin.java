package com.github.edg_thexu.better_experience.mixin.integration.confluence;

import com.github.edg_thexu.better_experience.config.CommonConfig;
import com.github.edg_thexu.better_experience.init.ModAttachments;
import com.github.edg_thexu.better_experience.intergration.confluence.ConfluenceHelper;
import com.github.edg_thexu.better_experience.module.reforge.BetterReforgeManager;
import com.github.edg_thexu.better_experience.networks.s2c.ClientBoundConfigPacket;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import org.confluence.mod.common.component.prefix.ModPrefix;
import org.confluence.mod.common.component.prefix.PrefixType;
import org.confluence.mod.util.PlayerUtils;
import org.confluence.mod.util.PrefixUtils;
import org.confluence.terraentity.utils.AdapterUtils;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(targets = "org.confluence.mod.common.menu.NPCReforgeMenu")
//@Mixin(NPCReforgeMenu.class)
public abstract class NPCReforgeMenuMixin extends AbstractContainerMenu {

    @Final
    @Shadow
    private int[] data;

    @Shadow @Final private Player player;

    protected NPCReforgeMenuMixin(@Nullable MenuType<?> menuType, int containerId) {
        super(menuType, containerId);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private static void init(int containerId, Inventory inventory, CallbackInfo ci) {
        Player player1 = inventory.player;
        if(player1 instanceof ServerPlayer serverPlayer){
            AdapterUtils.sendToPlayer(serverPlayer,
                    serverPlayer.getData(ModAttachments.TEMP_DATA).enableBetterReforge()?
                            new ClientBoundConfigPacket(2) :
                            new ClientBoundConfigPacket(3));
        }
    }


    @Inject(method = "slotsChanged", at = @At(value = "INVOKE", target = "Lorg/confluence/mod/util/PrefixUtils;getReforgeCost(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;)I"))
    private void slotsChanged(Container container, CallbackInfo ci, @Local PrefixType prefixType, @Local ItemStack itemStack) {

        if(CommonConfig.BETTER_REINFORCED_TOOL.get() && ConfluenceHelper.isLoaded()
                && this.player.getData(ModAttachments.TEMP_DATA).enableBetterReforge()) {
            data[1] = BetterReforgeManager.getBetterPrefix(prefixType, itemStack);
        }
    }



}

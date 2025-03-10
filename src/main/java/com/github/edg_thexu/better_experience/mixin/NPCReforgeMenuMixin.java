package com.github.edg_thexu.better_experience.mixin;

import com.github.edg_thexu.better_experience.config.ServerConfig;
import com.github.edg_thexu.better_experience.module.reforge.BetterReforgeManager;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import org.confluence.mod.common.component.prefix.PrefixType;
import org.confluence.mod.common.menu.NPCReforgeMenu;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NPCReforgeMenu.class)
public class NPCReforgeMenuMixin {

    @Final
    @Shadow
    private int[] data;

    @Inject(method = "slotsChanged", at = @At(value = "INVOKE", target = "Lorg/confluence/mod/util/PrefixUtils;getReforgeCost(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;)I"))
    private void slotsChanged(Container container, CallbackInfo ci, @Local PrefixType prefixType, @Local ItemStack itemStack) {
        if(ServerConfig.BETTER_REINFORCED_TOOL.get()) {
            data[1] = BetterReforgeManager.getBetterPrefix(prefixType, itemStack);
        }
    }

}

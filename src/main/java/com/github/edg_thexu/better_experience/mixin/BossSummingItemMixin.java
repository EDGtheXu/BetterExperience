package com.github.edg_thexu.better_experience.mixin;

import com.github.edg_thexu.better_experience.config.ServerConfig;
import net.minecraft.world.item.ItemStack;
import org.confluence.mod.common.item.common.BossSummingItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BossSummingItem.class)
public class BossSummingItemMixin {

    @Redirect(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;shrink(I)V"))
    private void useMixin(ItemStack instance, int decrement) {
        if(ServerConfig.NO_CONSUME_SUMMONER.get()){
            return;
        }
        instance.shrink(decrement);
    }
}

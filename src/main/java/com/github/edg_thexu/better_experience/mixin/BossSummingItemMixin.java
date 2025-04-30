package com.github.edg_thexu.better_experience.mixin;

import com.github.edg_thexu.better_experience.config.CommonConfig;
import net.minecraft.world.item.ItemStack;
import org.confluence.mod.common.item.common.BossSummoningItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BossSummoningItem.class)
public class BossSummingItemMixin {

    @Redirect(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;shrink(I)V"))
    private void useMixin(ItemStack instance, int decrement) {
        if(CommonConfig.NO_CONSUME_SUMMONER.get()){
            return;
        }
        instance.shrink(decrement);
    }
}

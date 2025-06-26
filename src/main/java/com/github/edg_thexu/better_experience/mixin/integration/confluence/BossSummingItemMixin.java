package com.github.edg_thexu.better_experience.mixin.integration.confluence;

import com.github.edg_thexu.better_experience.config.CommonConfig;
import net.minecraft.world.item.ItemStack;
import org.confluence.mod.common.item.common.BossSummoningItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
@Pseudo
@Mixin(targets = "org.confluence.mod.common.item.common.BossSummoningItem")
//@Mixin(BossSummoningItem.class)
public class BossSummingItemMixin {

    @Redirect(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;shrink(I)V"))
    private void useMixin(ItemStack instance, int decrement) {
        if(CommonConfig.NO_CONSUME_SUMMONER.get()){
            return;
        }
        instance.shrink(decrement);
    }
}

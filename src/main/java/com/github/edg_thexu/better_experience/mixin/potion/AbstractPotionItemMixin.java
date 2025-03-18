package com.github.edg_thexu.better_experience.mixin.potion;

import com.github.edg_thexu.better_experience.config.CommonConfig;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.confluence.mod.common.item.potion.AbstractPotionItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractPotionItem.class)
public abstract class AbstractPotionItemMixin {
    @Shadow
    public abstract ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity living);

    @WrapOperation(method = "use(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResultHolder;", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemUtils;startUsingInstantly(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResultHolder;"))
    private InteractionResultHolder<ItemStack> instantly(Level level, Player player, InteractionHand hand, Operation<InteractionResultHolder<ItemStack>> original) {
        if (CommonConfig.INSTANTLY_DRINK.get()) {
            return InteractionResultHolder.consume(finishUsingItem(player.getItemInHand(hand), level, player));
        }
        return original.call(level, player, hand);
    }
}

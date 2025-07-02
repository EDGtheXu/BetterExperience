package com.github.edg_thexu.better_experience.item;

import com.github.edg_thexu.better_experience.networks.c2s.BreakBlocksPacketC2S;
import com.github.edg_thexu.better_experience.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class MagicBoomStaff extends Item {

    // client
    public int range;
    public int maxRange;
    public MagicBoomStaff(Properties properties, int range, int maxRange) {
        super(properties);
        this.range = range;
        this.maxRange = maxRange;
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {

        if(level.isClientSide()){
            BlockPos pos = ModUtils.getEyeBlockHitResult(player, maxRange * 2);

            ModUtils.sendToServer(new BreakBlocksPacketC2S(pos.offset(-range,-range,-range), pos.offset(range,range,range)));
            player.startUsingItem(usedHand);
        }
        return InteractionResultHolder.success(player.getItemInHand(usedHand));
    }

    public void appendHoverText(ItemStack stack, Level context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("better_experience.tooltip.magic_boom_staff.info"));

    }

}
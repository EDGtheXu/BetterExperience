package com.github.edg_thexu.better_experience.item;

import com.github.edg_thexu.better_experience.network.C2S.BreakBlocksPacketC2S;
import com.github.edg_thexu.better_experience.utils.ModUtils;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.ItemFishedEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.confluence.mod.common.entity.fishing.BaseFishingHook;
import org.confluence.mod.common.init.ModEntities;
import org.confluence.mod.common.init.item.FishingPoleItems;

import java.util.Iterator;
import java.util.List;

public class MagicBoomStaff extends Item {

    // client
    public int range;
    public int maxRange = 5;
    public MagicBoomStaff(Properties properties, int range) {
        super(properties);
        this.range = range;
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {

        if(level.isClientSide()){
            BlockPos pos = ModUtils.getEyeBlockHitResult(player);

            PacketDistributor.sendToServer(new BreakBlocksPacketC2S(pos.offset(-range,-range,-range), pos.offset(range,range,range)));
            player.startUsingItem(usedHand);
        }
        return InteractionResultHolder.success(player.getItemInHand(usedHand));
    }

    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("better_experience.tooltip.magic_boom_staff.info"));

    }

}
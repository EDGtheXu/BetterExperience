package com.github.edg_thexu.better_experience.event;

import com.github.edg_thexu.better_experience.Better_experience;
import com.github.edg_thexu.better_experience.attachment.EnderChestAttachment;
import com.github.edg_thexu.better_experience.config.CommonConfig;
import com.github.edg_thexu.better_experience.module.autofish.FishBugRepetition;
import com.github.edg_thexu.better_experience.module.autopotion.PlayerAttribute;
import com.github.edg_thexu.better_experience.module.autopotion.PlayerInventoryManager;
import com.github.edg_thexu.better_experience.module.faststorage.StorageManager;
import com.github.edg_thexu.better_experience.networks.s2c.ClientBoundConfigPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = Better_experience.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerEvents {
    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        PlayerInventoryManager.getInstance().detect(player);

        if (player.level() instanceof ServerLevel sl) {
            PlayerAttribute.notifyDirty(sl);
            FishBugRepetition.detect(player);
            StorageManager.saveMoneyToPiggy(player);
        }
    }

    @SubscribeEvent
    public static void PlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        event.getEntity().sendSystemMessage(Component.translatable("better_experience.welcome_message"));
    }

    @SubscribeEvent
    public static void entityJoinLevel(EntityJoinLevelEvent event) {
        // 欢迎语
        if (event.getEntity() instanceof ServerPlayer player) {
            EnderChestAttachment.syncAll(player);
//            player.getInventory().add(ModItems.MagicBoomStaff.toStack());
            ClientBoundConfigPacket.sync(player);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void playerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            // 同步所有相关容器
            EnderChestAttachment.syncAll(player);
            if (CommonConfig.FILL_LIFE_ON_RESPAWN.get())
                player.setHealth(player.getMaxHealth());
            PlayerAttribute.applyAdditionalAttributes(player);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void breakSpeed(PlayerEvent.BreakSpeed event) {
        event.setNewSpeed((float) (event.getNewSpeed() * CommonConfig.BLOCK_BREAK_SPEED_MULTIPLIER.get()));
    }
}

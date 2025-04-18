package com.github.edg_thexu.better_experience.event;

import com.github.edg_thexu.better_experience.Better_experience;
import com.github.edg_thexu.better_experience.attachment.EnderChestAttachment;
import com.github.edg_thexu.better_experience.config.CommonConfig;
import com.github.edg_thexu.better_experience.mixed.IFishingHook;
import com.github.edg_thexu.better_experience.mixed.IPlayer;
import com.github.edg_thexu.better_experience.mixin.AbstractFishingPoleAccessor;
import com.github.edg_thexu.better_experience.module.autofish.AutoFishManager;
import com.github.edg_thexu.better_experience.module.autopotion.PlayerAttribute;
import com.github.edg_thexu.better_experience.module.autopotion.PlayerInventoryManager;
import com.github.edg_thexu.better_experience.networks.s2c.ClientBoundConfigPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.confluence.mod.common.item.fishing.AbstractFishingPole;

@EventBusSubscriber(modid = Better_experience.MODID, bus = EventBusSubscriber.Bus.GAME)
public class PlayerEvents {
    @SubscribeEvent
    public static void playerTick(PlayerTickEvent.Post event) {
        PlayerInventoryManager.getInstance().detect(event.getEntity());
        Player player1 = event.getEntity();
        if (player1.level() instanceof ServerLevel sl)
            PlayerAttribute.notifyDirty(sl);
        // 复刻钓鱼bug
        if (!event.getEntity().level().isClientSide && CommonConfig.MULTI_FISH.get()) {
            IPlayer player = (IPlayer) event.getEntity();
            if (player.betterExperience$getHammerUsingTicks() > 0) {
                player.betterExperience$setHammerUsingTicks(player.betterExperience$getHammerUsingTicks() - 1);
                ItemStack poleStack = event.getEntity().getMainHandItem();
                Level level = event.getEntity().level();
                FishingHook hook = null;
                try {
                    float power = AutoFishManager.computeFishingPower(event.getEntity(), poleStack, null, null);
                    if (poleStack.getItem() instanceof AbstractFishingPole pole) {
                        hook = ((AbstractFishingPoleAccessor) pole).callGetHook(poleStack, event.getEntity(), level, (int) power, 5);
                    } else if (poleStack.getItem() instanceof FishingRodItem) {
                        hook = new FishingHook(event.getEntity(), level, (int) power, 5);
                    }
                } catch (Exception e) {
                    Better_experience.LOGGER.warn(e.getMessage());
                    return;
                }
                if (hook != null) {
                    ((IFishingHook) hook).betterExperience$setAutoCatch(true);
                    level.addFreshEntity(hook);
                }
            }
        }
    }

    @SubscribeEvent
    public static void entityJoinLevel(EntityJoinLevelEvent event) {
        // 欢迎语
        if (event.getEntity() instanceof ServerPlayer player) {
            if (player.connection.tickCount == 0)
                player.sendSystemMessage(Component.translatable("better_experience.welcome_message"));
            EnderChestAttachment.sync(player);
//            player.getInventory().add(ModItems.MagicBoomStaff.toStack());
            ClientBoundConfigPacket.sync(player);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void playerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            // 同步末影箱
            EnderChestAttachment.sync(player);
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

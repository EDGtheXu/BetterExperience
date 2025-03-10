package com.github.edg_thexu.better_experience.event;

import com.github.edg_thexu.better_experience.Better_experience;
import com.github.edg_thexu.better_experience.attachment.EnderChestAttachment;
import com.github.edg_thexu.better_experience.config.ServerConfig;
import com.github.edg_thexu.better_experience.module.autopotion.PlayerInventoryManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.FishingRodItem;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = Better_experience.MODID, bus = EventBusSubscriber.Bus.GAME)
public class PlayerEvent {

    @SubscribeEvent
    public static void playerTick(PlayerTickEvent.Post event){

        PlayerInventoryManager.getInstance().detect(event.getEntity());
        if(!event.getEntity().level().isClientSide)
            event.getEntity().sendSystemMessage(Component.literal(String.valueOf(event.getEntity().fishing)));
    }

    @SubscribeEvent
    public static void entityJoinLevel(EntityJoinLevelEvent event){

        // 欢迎语
        if(event.getEntity() instanceof ServerPlayer player){
            if(player.connection.tickCount == 0 )
                player.sendSystemMessage(Component.translatable("better_experience.welcome_message"));

            // 增加摔落高度
            ResourceLocation location = Better_experience.space("fall_distance_modifier");
            if(player.getAttribute(Attributes.SAFE_FALL_DISTANCE).hasModifier(location)){
                player.getAttribute(Attributes.SAFE_FALL_DISTANCE).removeModifier(location);
            }
            player.getAttribute(Attributes.SAFE_FALL_DISTANCE).addPermanentModifier(
                    new AttributeModifier(location, ServerConfig.ADDITIONAL_FALL_DISTANCE.get(), AttributeModifier.Operation.ADD_VALUE));

        }

    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void playerRespawn(net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerRespawnEvent event) {
        if(event.getEntity() instanceof ServerPlayer player) {
            // 同步末影箱
            EnderChestAttachment.sync(player);
            if(ServerConfig.FILL_LIFE_ON_RESPAWN.get())
                player.setHealth(player.getMaxHealth());
        }
    }

}

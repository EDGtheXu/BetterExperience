package com.github.edg_thexu.better_experience.event;

import com.github.edg_thexu.better_experience.Better_experience;
import com.github.edg_thexu.better_experience.module.autopotion.PlayerInventoryManager;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = Better_experience.MODID, bus = EventBusSubscriber.Bus.GAME)
public class PlayerEvent {

    @SubscribeEvent
    public static void playerTick(PlayerTickEvent.Post event){

        PlayerInventoryManager.getInstance().detect(event.getEntity());

    }

    @SubscribeEvent
    public static void entityJoinLevel(EntityJoinLevelEvent event){

        // 欢迎语
        if(event.getEntity() instanceof ServerPlayer player){
            if(player.connection.tickCount == 0 )
                player.sendSystemMessage(Component.translatable("better_experience.welcome_message"));
        }

    }
}

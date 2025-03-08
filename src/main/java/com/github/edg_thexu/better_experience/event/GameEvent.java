package com.github.edg_thexu.better_experience.event;

import com.github.edg_thexu.better_experience.Better_experience;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent;

@EventBusSubscriber(modid = Better_experience.MODID, bus = EventBusSubscriber.Bus.GAME)
public class GameEvent {

    @SubscribeEvent
    public static void entitySpawn(FinalizeSpawnEvent event){

    }

}

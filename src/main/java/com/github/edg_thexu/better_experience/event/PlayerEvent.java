package com.github.edg_thexu.better_experience.event;

import com.github.edg_thexu.better_experience.Better_experience;
import com.github.edg_thexu.better_experience.module.autopotion.PlayerInventoryManager;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = Better_experience.MODID, bus = EventBusSubscriber.Bus.GAME)
public class PlayerEvent {

    static PlayerInventoryManager manager = new PlayerInventoryManager();
    @SubscribeEvent
    public static void event(PlayerTickEvent.Post event){

        manager.detect(event.getEntity());

    }
}

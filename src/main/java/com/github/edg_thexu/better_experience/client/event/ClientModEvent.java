package com.github.edg_thexu.better_experience.client.event;

import com.github.edg_thexu.better_experience.Better_experience;
import com.github.edg_thexu.better_experience.client.gui.config_container.ConfigContainerRegister;
import com.github.edg_thexu.better_experience.client.gui.container.AutoFishScreen;
import com.github.edg_thexu.better_experience.client.gui.container.PotionBagScreen;
import com.github.edg_thexu.better_experience.client.renderer.AutoFishBlockRenderer;
import com.github.edg_thexu.better_experience.init.ModBlocks;
import com.github.edg_thexu.better_experience.init.ModMenus;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;


@Mod.EventBusSubscriber(modid = Better_experience.MODID, bus = Mod.EventBusSubscriber.Bus.MOD,value = Dist.CLIENT)
public class ClientModEvent {

    @SubscribeEvent
    public static void onClientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(ModMenus.AUTO_FISH_MENU.get(), AutoFishScreen::new);
            MenuScreens.register(ModMenus.POTION_BAG_MENU.get(), PotionBagScreen::new);

        });
    }

    @SubscribeEvent
    public static void onEnqueue(final InterModEnqueueEvent event) {
        ConfigContainerRegister.registerModsPage(event);
    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlocks.AUTO_FISH_BLOCK_ENTITY.get(), c -> new AutoFishBlockRenderer());
//        if(ConfluenceHelper.isLoaded()) {
//            event.registerBlockEntityRenderer(ModBlocks.AUTO_SELL_BLOCK_ENTITY.get(), c -> new AutoSellBlockRenderer());
//
//        }

    }
}

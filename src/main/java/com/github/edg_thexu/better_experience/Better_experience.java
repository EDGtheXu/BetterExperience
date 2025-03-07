package com.github.edg_thexu.better_experience;

import com.github.edg_thexu.better_experience.config.ServerConfig;
import com.github.edg_thexu.better_experience.init.ModItems;
import com.github.edg_thexu.better_experience.init.ModTabs;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.confluence.mod.common.event.game.entity.EntityEvents;
import org.confluence.mod.common.menu.NPCTradesMenu;
import org.confluence.mod.mixin.client.gui.DeathScreenMixin;
import org.confluence.terraentity.entity.monster.slime.BaseSlime;
import org.slf4j.Logger;


@Mod(Better_experience.MODID)
public class Better_experience {

    public static final String MODID = "better_experience";

    private static final Logger LOGGER = LogUtils.getLogger();

    public static ResourceLocation space(String location){
        return ResourceLocation.fromNamespaceAndPath(Better_experience.MODID, location);
    }

    public Better_experience(IEventBus modEventBus, ModContainer modContainer) {

        modContainer.registerConfig(ModConfig.Type.COMMON, ServerConfig.init());
        ModItems.register(modEventBus);

        ModTabs.TABS.register(modEventBus);

    }

}

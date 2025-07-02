package com.github.edg_thexu.better_experience.event;

import com.github.edg_thexu.better_experience.Better_experience;
import com.github.edg_thexu.better_experience.config.CommonConfig;
import com.github.edg_thexu.better_experience.init.ModBlocks;
import com.github.edg_thexu.better_experience.init.ModItems;
import com.github.edg_thexu.better_experience.intergration.confluence.ConfluenceHelper;
import com.github.edg_thexu.better_experience.intergration.confluence_lib.ConfluenceLibHelper;
import com.github.edg_thexu.better_experience.intergration.jei.JeiHelper;
import com.github.edg_thexu.better_experience.module.autopotion.PlayerAttribute;
import com.github.edg_thexu.better_experience.networks.NetworkHandler;
import com.github.edg_thexu.better_experience.networks.c2s.BreakBlocksPacketC2S;
import com.github.edg_thexu.better_experience.networks.c2s.PotionApplyPacketC2S;
import com.github.edg_thexu.better_experience.networks.c2s.SearchJeiIngredientsPacketC2S;
import com.github.edg_thexu.better_experience.networks.c2s.ServerBoundPacketC2S;
import com.github.edg_thexu.better_experience.networks.s2c.ClientBoundConfigPacket;
import com.github.edg_thexu.better_experience.networks.s2c.EnderChestItemsS2C;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.server.ServerLifecycleHooks;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = Better_experience.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEvent {

    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            NetworkHandler.register();
        });
    }

    @SubscribeEvent
    public static void registerPayloadHandlers(ModConfigEvent.Reloading event) {
        if (event.getConfig().getSpec() == CommonConfig.SPEC) {
            // 重新加载人物属性
            PlayerAttribute.dirty = true;
        }
    }

//    @SubscribeEvent(priority = EventPriority.LOWEST)
//    public static void modifyDefaultComponents(ModifyDefaultComponentsEvent event) {
//        try {
//
//            if (CommonConfig.MODIFY_MAX_STACK_SIZE.get()) {
//                for (Map.Entry<ResourceKey<Item>, Item> entry : BuiltInRegistries.ITEM.entrySet()) {
//                    Item item = entry.getValue();
//                    DataComponentMap components = item.components();
//                    if (components.has(DataComponents.MAX_DAMAGE) || ConfluenceHelper.isLoaded() && PrefixUtils.couldReforge(item.getDefaultInstance()))
//                        continue;
//                    String namespace = entry.getKey().location().getNamespace();
//                    if ("minecraft".equals(namespace) || ConfluenceHelper.isLoaded() && Confluence.MODID.equals(namespace)) {
//                        int maxStackSize = item.getDefaultMaxStackSize();
//                        if (maxStackSize == 1 || components.has(DataComponents.FOOD) || components.has(DataComponents.POTION_CONTENTS)) {
//                            event.modify(item, builder -> builder.set(DataComponents.MAX_STACK_SIZE, 99));
//                        } else if (maxStackSize == 16) {
//                            event.modify(item, builder -> builder.set(DataComponents.MAX_STACK_SIZE, 999));
//                        } else if (maxStackSize == 64) {
//                            event.modify(item, builder -> builder.set(DataComponents.MAX_STACK_SIZE, 9999));
//                        }
//                    }
//                }
//            }
//        } catch (IllegalStateException ignored) {
//
//        }
//
//        if(ConfluenceLibHelper.isLoaded()){
//            event.modify(ModItems.PotionBag, builder -> builder.set(ConfluenceMagicLib.MOD_RARITY.get(), ModRarity.ORANGE));
//            event.modify(ModItems.MagicBoomStaff, builder -> builder.set(ConfluenceMagicLib.MOD_RARITY.get(), ModRarity.YELLOW));
//            event.modify(ModItems.StarBoomStaff, builder -> builder.set(ConfluenceMagicLib.MOD_RARITY.get(), ModRarity.CYAN));
//            event.modify(ModBlocks.AUTO_FISH_BLOCK, builder -> builder.set(ConfluenceMagicLib.MOD_RARITY.get(), ModRarity.BLUE));
//
//        }
//    }
    @SubscribeEvent
    public static void registerConfig(ModConfigEvent.Reloading event) {
//        try{
            if(event.getConfig().getSpec() == CommonConfig.SPEC){
                if(ServerLifecycleHooks.getCurrentServer() != null) {
                    ClientBoundConfigPacket.syncAll();
                }
            }
//        }catch(Exception ignored){
//        }

    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
//        List<Block> supportBlocks = new ArrayList<>();
//        supportBlocks.add(ModBlocks.AUTO_FISH_BLOCK.get());
//        if(ConfluenceHelper.isLoaded()){
//            supportBlocks.add(ModBlocks.AUTO_SELL_BLOCK.get());
//        }
//        event.registerBlock(Capabilities.ItemHandler.BLOCK, (level, pos, state, blockEntity, side) -> {
//            Container container = ChestBlock.getContainer((ChestBlock) state.getBlock(), state, level, pos, true);
//            if (container == null) return null;
//            return new InvWrapper(container);
//        }, supportBlocks.toArray(new Block[0]));
    }

}

package com.github.edg_thexu.better_experience;

import com.github.edg_thexu.better_experience.config.CommonConfig;
import com.github.edg_thexu.better_experience.init.*;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Mod(Better_experience.MODID)
public class Better_experience {
    public static final String MODID = "better_experience";

    public static final Logger LOGGER = LoggerFactory.getLogger("Better Experience");

    public static ResourceLocation space(String location){
        return ResourceLocation.fromNamespaceAndPath(Better_experience.MODID, location);
    }

    public Better_experience(IEventBus modEventBus, ModContainer modContainer) {

        modContainer.registerConfig(ModConfig.Type.COMMON, CommonConfig.init());
        ModItems.register(modEventBus);

        ModTabs.TABS.register(modEventBus);
        ModAttachments.TYPES.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModMenus.TYPES.register(modEventBus);
    }

}

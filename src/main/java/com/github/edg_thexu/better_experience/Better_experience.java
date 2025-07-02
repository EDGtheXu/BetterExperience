package com.github.edg_thexu.better_experience;

import com.github.edg_thexu.better_experience.config.ConfigRegistry;
import com.github.edg_thexu.better_experience.init.*;
import com.github.edg_thexu.better_experience.intergration.terra_gun.TGHelper;
import com.github.edg_thexu.better_experience.registries.ModRegistries;
import net.minecraft.resources.ResourceLocation;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("removal")
@Mod(Better_experience.MODID)
public class Better_experience {
    public static final String MODID = "better_experience";

    public static final Logger LOGGER = LoggerFactory.getLogger("Better Experience");

    public static ResourceLocation space(String location){
        return new ResourceLocation(Better_experience.MODID, location);
    }

    public static ResourceLocation space(String modid, String location){
        return new ResourceLocation(modid, location);
    }

    public static ResourceLocation defaultSpacee(String location){
        return new ResourceLocation("minecraft", location);
    }

    public Better_experience() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

//        modEventBus.addListener(ModRegistries::newRegistry);
        ModRegistries.register(modEventBus);

//        modContainer.registerConfig(ModConfig.Type.COMMON, CommonConfig.init());
        ModItems.register(modEventBus);

        ModTabs.TABS.register(modEventBus);
//        ModAttachments.TYPES.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModMenus.TYPES.register(modEventBus);
//        ModDataComponentTypes.TYPES.register(modEventBus);


        if(TGHelper.isLoaded()){
//            TerraGunEvents.init();
//            NeoForge.EVENT_BUS.register(new TerraGunEvents());
        }
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigRegistry.register());

    }

}

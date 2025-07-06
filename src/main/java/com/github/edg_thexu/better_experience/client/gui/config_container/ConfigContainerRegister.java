package com.github.edg_thexu.better_experience.client.gui.config_container;

import com.github.edg_thexu.better_experience.Better_experience;
import com.github.edg_thexu.better_experience.config.ClientConfig;
import com.github.edg_thexu.better_experience.config.CommonConfig;
import com.github.edg_thexu.better_experience.intergration.jei.JeiHelper;
import com.github.edg_thexu.better_experience.intergration.terra_entity.TEHelper;
import com.github.edg_thexu.cafelib.client.gui.config_container.ConfigScreen;
import com.github.edg_thexu.cafelib.client.gui.config_container.ConfigScreenBuilder;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;



//@SuppressWarnings("all")
public class ConfigContainerRegister {

    public static ConfigScreenBuilder init(ConfigScreen screen){
        ConfigScreenBuilder builder;
        builder = ConfigScreenBuilder.builder(screen);

builder.addTab(Better_experience.MODID,"Client",45);


        builder.addCheckBox(ClientConfig.SHOW_OUTLINES)
                .comment("Show Staff outlines.");


builder.addTab(Better_experience.MODID,"Item",90);

        builder.addCheckBox(CommonConfig.AUTO_POTION_OPEN)
                        .comment("Apply potion automatically without consuming. Support Inventory, Ender Chest and Potion Bag.");
        builder.addIntSliderEditBox(CommonConfig.AUTO_POTION_STACK_SIZE, 1, 64)
                .comment("Auto potion stack size.");
        builder.addCheckBox(CommonConfig.INSTANTLY_DRINK)
                .comment("Instantly Drink");
        builder.addCheckBox(CommonConfig.INFINITE_AMMO)
                .comment("Infinite ammo.");
        builder.addIntSliderEditBox(CommonConfig.INFINITE_AMMO_STACK_SIZE, 1, 64)
                .comment("How many ammo stack could apply without consuming automatically. (Default: 3996) (1 - 64)");
        builder.addCheckBox(CommonConfig.MODIFY_MAX_STACK_SIZE)
                .comment("Modify Max Stack Size.");
        builder.addCheckBox(CommonConfig.FORBIDDEN_MAGIC_BOOM_STAFF)
                .comment("Forbidden magic boom staff.");


builder.addTab(Better_experience.MODID,"Player",290);

        builder.addCheckBox(CommonConfig.FILL_LIFE_ON_RESPAWN)
                .comment("Fill Life on Respawn.");

        builder.addCheckBox(CommonConfig.MULTI_FISH)
                .comment("Multi fish after left click with axe and change fishing rod soon.");

        builder.addDoubleEditBox(CommonConfig.BLOCK_BREAK_SPEED_MULTIPLIER)
                .comment("Block Break Speed Multiplier. (Default: 1.0) (0.1 - 100)");


        if(JeiHelper.isLoaded()) {
            builder.addIntSliderEditBox(CommonConfig.QUICK_JEI_FETCH_DISTANCE, 1, 30)
                    .comment("Quick JEI Fetch Distance.");
            builder.addCheckBox(CommonConfig.QUICK_JEI_FETCH)
                    .comment("Quick JEI Fetch.");
        }

builder.addTab(Better_experience.MODID,"World",300);

builder.addTab(Better_experience.MODID,"Entity",300);

        if(TEHelper.isLoaded()) {
            builder.addCheckBox(CommonConfig.SLIME_DIE_NO_LAVA)
                    .comment("Forbidden slime to generate lava.");
        }

        builder.build(Better_experience.MODID);
        return builder;
    }

    @SuppressWarnings("removal")
    public static void registerModsPage(final InterModEnqueueEvent event) {
        event.enqueueWork(() -> {
            if (FMLEnvironment.dist == Dist.CLIENT) {
                ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () ->
                        new ConfigScreenHandler.ConfigScreenFactory((client, parent) -> new BEConfigScreen(parent, ConfigContainerRegister::init)));
            }
        });
    }


}

package com.github.edg_thexu.better_experience.config;


import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeConfigSpec;

@OnlyIn(Dist.CLIENT)
public class ClientConfig {

    public static ForgeConfigSpec.ConfigValue<Boolean> SHOW_OUTLINES;
    public static ForgeConfigSpec.ConfigValue<Boolean> MULTI_FISHING;
    public static ForgeConfigSpec SPEC;

    public static ForgeConfigSpec init(ForgeConfigSpec.Builder BUILDER){

        SHOW_OUTLINES = BUILDER
                .comment("Show outlines")
                .define("show_outlines", true);
        MULTI_FISHING = BUILDER
                .comment("Multi Fishing")
                .define("client_multi_fishing", false);


        SPEC = BUILDER.build();
        return SPEC;
    }

}

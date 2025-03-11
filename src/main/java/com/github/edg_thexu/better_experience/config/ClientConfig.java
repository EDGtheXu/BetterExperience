package com.github.edg_thexu.better_experience.config;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.ModConfigSpec;

@OnlyIn(Dist.CLIENT)
public class ClientConfig {

    public static ModConfigSpec.ConfigValue<Boolean> SHOW_OUTLINES;
    public static ModConfigSpec.ConfigValue<Boolean> MULTI_FISHING;
    public static ModConfigSpec SPEC;

    public static ModConfigSpec init(){
        final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

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

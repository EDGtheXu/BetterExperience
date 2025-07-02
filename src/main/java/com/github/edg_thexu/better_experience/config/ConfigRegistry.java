package com.github.edg_thexu.better_experience.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigRegistry {

    public static ForgeConfigSpec SPEC;

    public static ForgeConfigSpec register(){
        ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

        ClientConfig.init(BUILDER);
        CommonConfig.init(BUILDER);

        SPEC = BUILDER.build();
        return SPEC;
    }
}

package com.github.edg_thexu.better_experience.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ServerConfig {
    public static ModConfigSpec.ConfigValue<Boolean> AUTO_POTION_OPEN;

    public static ModConfigSpec.ConfigValue<Integer> AUTO_POTION_STACK_SIZE;

    public static ModConfigSpec init(){
        final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

        AUTO_POTION_OPEN = BUILDER
                .comment("Should the potion automatically open?")
                .define("auto_potion_open", true);
        AUTO_POTION_STACK_SIZE = BUILDER
                .comment("How many potion stack could apply without consuming automatically?")
                .defineInRange("auto_potion_stack_size", 10,1,16);

        return BUILDER.build();
    }
}

package com.github.edg_thexu.better_experience.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ServerConfig {
    public static ModConfigSpec.ConfigValue<Boolean> AUTO_POTION_OPEN;

    public static ModConfigSpec.ConfigValue<Integer> AUTO_POTION_STACK_SIZE;

    public static ModConfigSpec.ConfigValue<Boolean> INFINITE_AMMO;

    public static ModConfigSpec.ConfigValue<Integer> INFINITE_AMMO_STACK_SIZE;

    public static ModConfigSpec.ConfigValue<Boolean> NO_CONSUME_SUMMONER;

    public static ModConfigSpec.ConfigValue<Boolean> SLIME_DIE_NO_LAVA;



    public static ModConfigSpec init(){
        final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

        // 药水无线续杯
        AUTO_POTION_OPEN = BUILDER
                .comment("Should the potion automatically open?")
                .define("auto_potion_open", true);
        AUTO_POTION_STACK_SIZE = BUILDER
                .comment("How many potion stack could apply without consuming automatically?")
                .defineInRange("auto_potion_stack_size", 10,1,16);

        // 无限弹药
        INFINITE_AMMO = BUILDER
                .comment("Should the player have infinite ammo?")
                .define("infinite_ammo", false);
        INFINITE_AMMO_STACK_SIZE = BUILDER
                .comment("How many ammo stack could apply without consuming automatically?")
                .defineInRange("infinite_ammo_stack_size", 3996,64,9999);


        NO_CONSUME_SUMMONER = BUILDER
                .comment("Not consume summon item?")
                .define("no_consume_summoner", false);

        SLIME_DIE_NO_LAVA = BUILDER
                .comment("Forbidden slime to generate lava?")
                .define("slime_die_no_lava", true);

        return BUILDER.build();
    }
}

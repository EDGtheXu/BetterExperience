package com.github.edg_thexu.better_experience.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class CommonConfig {
    /**
     * 是否开启药水无线续杯
     */
    public static ModConfigSpec.BooleanValue AUTO_POTION_OPEN;
    /**
     * 无线续杯数量
     */
    public static ModConfigSpec.IntValue AUTO_POTION_STACK_SIZE;
    /**
     * 瞬间喝药
     */
    public static ModConfigSpec.BooleanValue INSTANTLY_DRINK;

    /**
     * 是否开启药水无线续杯
     */
    public static ModConfigSpec.BooleanValue INFINITE_AMMO;

    /**
     * 无限弹药数量
     */
    public static ModConfigSpec.IntValue INFINITE_AMMO_STACK_SIZE;

    /**
     * 修改物品最大堆叠
     */
    public static ModConfigSpec.BooleanValue MODIFY_MAX_STACK_SIZE;

    /**
     * 召唤BOSS无需消耗召唤物品
     */
    public static ModConfigSpec.BooleanValue NO_CONSUME_SUMMONER;

    /**
     * 禁止史莱姆死亡时生成熔岩
     */
    public static ModConfigSpec.BooleanValue SLIME_DIE_NO_LAVA;

    /**
     * 额外的摔落免疫
     */
    public static ModConfigSpec.IntValue ADDITIONAL_FALL_DISTANCE;
    /**
     * 宝石树种植无限制
     */
    public static ModConfigSpec.BooleanValue STONE_SAPLING_TREE_NO_STRICT;
    /**
     * 草药种植无限制
     */
    public static ModConfigSpec.BooleanValue HERB_GROWTH_NO_STRICT;
    /**
     * 重生时填满生命值
     */
    public static ModConfigSpec.BooleanValue FILL_LIFE_ON_RESPAWN;
    /**
     * 更好的重铸
     */
    public static ModConfigSpec.BooleanValue MULTI_FISH;
    /**
     * 更好的重铸
     */
    public static ModConfigSpec.BooleanValue BETTER_REINFORCED_TOOL;
    /**
     * 骨粉催熟
     */
    public static ModConfigSpec.BooleanValue VALID_BONEMEAL_TARGET;

    public static ModConfigSpec init() {
        final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

        BUILDER.push("Item");

        // 药水无线续杯
        AUTO_POTION_OPEN = BUILDER
                .comment("Apply potion automatically without consuming. Support Inventory and Ender Chest.")
                .define("auto_potion_open", true);
        AUTO_POTION_STACK_SIZE = BUILDER
                .comment("How many potion stack could apply without consuming automatically?")
                .defineInRange("auto_potion_stack_size", 10, 1, 9999);
        INSTANTLY_DRINK = BUILDER
                .comment("Instantly Drink")
                .define("instantly_drink", false);

        // 无限弹药
        INFINITE_AMMO = BUILDER
                .comment("Should the player have infinite ammo?")
                .define("infinite_ammo", false);
        INFINITE_AMMO_STACK_SIZE = BUILDER
                .comment("How many ammo stack could apply without consuming automatically?")
                .defineInRange("infinite_ammo_stack_size", 3996, 64, 9999);

        NO_CONSUME_SUMMONER = BUILDER
                .comment("Not consume summon item?")
                .define("no_consume_summoner", false);

        BETTER_REINFORCED_TOOL = BUILDER
                .comment("Better reinforced tool.")
                .define("better_reinforced_tool", false);

        MODIFY_MAX_STACK_SIZE = BUILDER
                .comment("Modify Max Stack Size")
                .define("modify_max_stack_size", false);

        BUILDER.pop();
        BUILDER.push("Player");

        FILL_LIFE_ON_RESPAWN = BUILDER
                .comment("Fill life on respawn.")
                .define("fill_life_on_respawn", false);

        ADDITIONAL_FALL_DISTANCE = BUILDER
                .comment("Additional fall distance for players.")
                .defineInRange("additional_fall_distance", 0, 0, 100);

        MULTI_FISH = BUILDER
                .comment("Multi fish.")
                .define("server_multi_fishing", true);

        BUILDER.pop();
        BUILDER.push("World");


        STONE_SAPLING_TREE_NO_STRICT = BUILDER
                .comment("Stone sapling tree grows without strict conditions.")
                .define("stone_sapling_tree_no_strict", false);

        HERB_GROWTH_NO_STRICT = BUILDER
                .comment("Herb growth without strict conditions.")
                .define("herb_growth_no_strict", false);

        VALID_BONEMEAL_TARGET = BUILDER
                .comment("Valid bonemeal target.")
                .define("valid_bonemeal_target", true);

        BUILDER.pop();
        BUILDER.push("Entity");

        SLIME_DIE_NO_LAVA = BUILDER
                .comment("Forbidden slime to generate lava?")
                .define("slime_die_no_lava", true);

        BUILDER.pop();

        SPEC = BUILDER.build();
        return SPEC;
    }

    public static ModConfigSpec SPEC;
}

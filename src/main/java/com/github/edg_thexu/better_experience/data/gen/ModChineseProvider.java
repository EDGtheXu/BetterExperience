package com.github.edg_thexu.better_experience.data.gen;

import com.github.edg_thexu.better_experience.Better_experience;
import com.github.edg_thexu.better_experience.init.ModBlocks;
import com.github.edg_thexu.better_experience.init.ModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;



public class ModChineseProvider extends LanguageProvider {
    public ModChineseProvider(PackOutput output) {
        super(output, Better_experience.MODID, "zh_cn");
    }

    @Override
    protected void addTranslations() {

        add("better_experience.welcome_message", "已启用 [汇流来世:更好的体验] , 按ESC并在mod中找到本模组配置启用功能。");

        add("creativetab.better_experience.item", "汇流来世 | 更好的体验");

        // items
        add(ModItems.MagicBoomStaff.get(), "法爆魔杖");
        add(ModItems.StarBoomStaff.get(), "星爆魔杖");
        add(ModItems.PotionBag.get(), "药水袋");
        add(ModBlocks.AUTO_FISH_BLOCK.get(), "自动钓鱼机");
        add(ModBlocks.AUTO_SELL_BLOCK.get(), "自动贩卖机");
        add(ModBlocks.REFORGE_BLOCK.get(), "重铸机");

        // tooltips
        add("better_experience.tooltip.magic_boom_staff.info", "左手物品决定镐力 [按住shift+鼠标滚轮调整大小]");
        add("better_experience.tooltip.potion_bag.info", "可以存储一些药水和食物");
        add("better_experience.tooltip.jei.fetch_ingredients", "从周围箱子取出材料");
        add("better_experience.tooltip.better_reforge.enable", "启用更好的重铸");

        add("better_experience.gui.fast_storage", "一键存储");
        add("better_experience.gui.potion_screen.auto_collect.message", "自动收集药水");

        // config
        add("better_experience.configuration.Player", "玩家");
        add("better_experience.configuration.World", "世界");
        add("better_experience.configuration.Entity", "生物");
        add("better_experience.configuration.Item", "物品");



        add("better_experience.configuration.show_outlines", "法爆魔杖显示边框");
        add("better_experience.configuration.auto_potion_open", "药水 无限续杯");
        add("better_experience.configuration.auto_potion_stack_size", "药水 无限续杯 需求量");
        add("better_experience.configuration.instantly_drink", "瞬间喝药");
        add("better_experience.configuration.infinite_ammo", "无限弹药");
        add("better_experience.configuration.infinite_ammo_stack_size", "无限弹药 需求量");
        add("better_experience.configuration.modify_max_stack_size", "调整 物品 最大堆叠");
        add("better_experience.configuration.no_consume_summoner", "不消耗BOSS召唤物");
        add("better_experience.configuration.slime_die_no_lava", "岩浆史莱姆死亡不生成岩浆");
        add("better_experience.configuration.additional_fall_distance", "额外的摔落免疫高度");
        add("better_experience.configuration.stone_sapling_tree_no_strict", "宝石树生长无限制");
        add("better_experience.configuration.fill_life_on_respawn", "重生时回满生命值");
        add("better_experience.configuration.herb_growth_no_strict", "草生长无限制");
        add("better_experience.configuration.better_reinforced_tool", "更好的重铸");
        add("better_experience.configuration.client_multi_fishing", "客户端 抡锤子钓鱼bug");
        add("better_experience.configuration.server_multi_fishing", "服务器 抡锤子钓鱼bug");
        add("better_experience.configuration.valid_bonemeal_target", "骨粉可以催熟宝石树");
        add("better_experience.configuration.block_break_speed_multiplier", "方块 破坏速度 倍率");
        add("better_experience.configuration.forbidden_magic_boom_staff", "禁用法爆魔杖");
        add("better_experience.configuration.auto_save_money", "自动把钱存到猪猪存钱罐");
        add("better_experience.configuration.quick_jei_fetch", "JEI快速从周围箱子取材料");


        // info
        add("better_experience.autofish.info.lack", "自动钓鱼机缺少方块连接：");
        add("better_experience.info.forbidden_magic_boom_staff", "你不能在这个世界使用法爆魔杖，请在ESC-MOD-better_experience-config-Item中打开功能");
        add("better_experience.info.jei.not_enough_ingredients", "附近的箱子没有足够的材料");

        // container
        add("block.better_experience.autofish_machine", "自动钓鱼机");
    }
}

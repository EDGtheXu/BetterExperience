package com.github.edg_thexu.better_experience.data.gen;

import com.github.edg_thexu.better_experience.Better_experience;
import com.github.edg_thexu.better_experience.init.ModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;



public class TEChineseProvider extends LanguageProvider {
    public TEChineseProvider(PackOutput output) {
        super(output, Better_experience.MODID, "zh_cn");
    }

    @Override
    protected void addTranslations() {

        add("better_experience.welcome_message", "已启用 [汇流来世:更好的体验] , 按ESC并在mod中找到本模组配置启用功能。");

        // items
        add(ModItems.MagicBoomStaff.get(), "魔爆法杖");


        // tooltips
        add("better_experience.tooltip.magic_boom_staff.info", "左手物品决定镐力");

        // config

        add("better_experience.configuration.show_outlines", "法爆魔杖显示边框");



        add("better_experience.configuration.auto_potion_open", "药水无限续杯");

        add("better_experience.configuration.auto_potion_stack_size", "无限续杯药水堆叠数量");

        add("better_experience.configuration.infinite_ammo", "无限弹药");

        add("better_experience.configuration.infinite_ammo_stack_size", "无限弹药堆叠数量");


    }
}

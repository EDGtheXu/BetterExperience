package com.github.edg_thexu.better_experience.data.gen;

import com.github.edg_thexu.better_experience.Better_experience;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;



public class TEChineseProvider extends LanguageProvider {
    public TEChineseProvider(PackOutput output) {
        super(output, Better_experience.MODID, "zh_cn");
    }

    @Override
    protected void addTranslations() {


        // config
        add("better_experience.configuration.auto_potion_open", "开启自动药水");

        add("better_experience.configuration.auto_potion_stack_size", "自动药水堆叠数量");

    }
}

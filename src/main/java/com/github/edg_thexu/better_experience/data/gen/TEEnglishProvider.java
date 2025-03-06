package com.github.edg_thexu.better_experience.data.gen;

import com.github.edg_thexu.better_experience.Better_experience;
import com.github.edg_thexu.better_experience.init.ModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;


import java.util.Arrays;
import java.util.stream.Collectors;


public class TEEnglishProvider extends LanguageProvider {
    public TEEnglishProvider(PackOutput output) {
        super(output, Better_experience.MODID, "en_us");
    }

    private static String toTitleCase(String raw) {
        return Arrays.stream(raw.split("_"))
                .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }

    @Override
    protected void addTranslations() {

        add("better_experience.welcome_message", "Better Experience has been enabled, press ESC to access the mod's configuration to enable features.");

        // items
        add(ModItems.MagicBoomStaff.get(), "Magic Boom Staff");


        // tooltips
        add("better_experience.tooltip.magic_boom_staff.info", "power depends on the item in left hand");

        // Config
        add("better_experience.configuration.show_outlines", "Magic Boom Staff Show Outlines");



        add("better_experience.configuration.auto_potion_open", "Open Auto-Potion");

        add("better_experience.configuration.auto_potion_stack_size", "Auto-Potion Stack Size");

        add("better_experience.configuration.infinite_ammo", "Infinite Ammo");

        add("better_experience.configuration.infinite_ammo_stack_size", "Infinite Ammo Stack Size");
    }
}

package com.github.edg_thexu.better_experience.data.gen;

import com.github.edg_thexu.better_experience.Better_experience;
import com.github.edg_thexu.better_experience.init.ModBlocks;
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

        add("creativetab.better_experience.item", "Confluence | Better Experience");


        // items
        add(ModItems.MagicBoomStaff.get(), "Magic Boom Staff");
        add(ModItems.StarBoomStaff.get(), "Star Boom Staff");
        add(ModBlocks.AUTO_FISH_BLOCK.get(), "Auto Fish Machine");


        // tooltips
        add("better_experience.tooltip.magic_boom_staff.info", "power depends on the item in left hand [hold shift and scroll mousewheel to adjust size]");
        add("better_experience.gui.fast_storage", "Fast Storage");


        // Config
        add("better_experience.configuration.Player", "Player");
        add("better_experience.configuration.World", "World");
        add("better_experience.configuration.Entity", "Entity");
        add("better_experience.configuration.Item", "Item");



        add("better_experience.configuration.show_outlines", "Magic Boom Staff Show Outlines");
        add("better_experience.configuration.auto_potion_open", "Open Auto-Potion");
        add("better_experience.configuration.auto_potion_stack_size", "Auto-Potion Stack Size");
        add("better_experience.configuration.instantly_drink", "Instantly Drink");
        add("better_experience.configuration.infinite_ammo", "Infinite Ammo");
        add("better_experience.configuration.infinite_ammo_stack_size", "Infinite Ammo Stack Size");
        add("better_experience.configuration.modify_max_stack_size", "Modify Item Max Stack Size");
        add("better_experience.configuration.no_consume_summoner", "No Consume Summon Item");
        add("better_experience.configuration.slime_die_no_lava", "Slime Dies Without Lava");
        add("better_experience.configuration.additional_fall_distance", "Additional Fall Distance");
        add("better_experience.configuration.stone_sapling_tree_no_strict", "Stone Sapling Tree No Strict");
        add("better_experience.configuration.fill_life_on_respawn", "Fill Life on Respawn");
        add("better_experience.configuration.herb_growth_no_strict", "Herb Growth No Strict");
        add("better_experience.configuration.better_reinforced_tool", "Better Reinforced");
        add("better_experience.configuration.client_multi_fishing", "Client Multi-Fishing Using Hammers");
        add("better_experience.configuration.server_multi_fishing", "Server Multi-Fishing Using Hammers");
        add("better_experience.configuration.valid_bonemeal_target", "Bonemeal Can Be Used On Stone Saplings");
        add("better_experience.configuration.block_break_speed_multiplier", "Block Break Speed Multiplier");
        add("better_experience.configuration.forbidden_magic_boom_staff", "Forbidden Magic Boom Staff");



        // info
        add("better_experience.autofish.info.lack", "Auto Fish Machine Lacks connection: ");
        add("better_experience.info.forbidden_magic_boom_staff", "You can't use magic boom staff in this world, please open function in ESC-MOD-better_experience-config-Item");


        // container
        add("block.better_experience.autofish_machine", "Auto Fish Machine");



    }
}

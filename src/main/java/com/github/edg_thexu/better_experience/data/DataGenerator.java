package com.github.edg_thexu.better_experience.data;

import com.github.edg_thexu.better_experience.Better_experience;
import com.github.edg_thexu.better_experience.data.gen.ModItemModelProvider;
import com.github.edg_thexu.better_experience.data.gen.ModChineseProvider;
import com.github.edg_thexu.better_experience.data.gen.ModEnglishProvider;
import com.github.edg_thexu.better_experience.data.gen.loot.ModLootTableProvider;
import com.github.edg_thexu.better_experience.data.gen.recipe.ModRecipeProvider;
import com.github.edg_thexu.better_experience.data.gen.tag.ModBlockTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


import java.util.Map;
import java.util.concurrent.CompletableFuture;


@Mod.EventBusSubscriber(modid = Better_experience.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerator {
    public static Map<String, DataProvider> PROVIDERS = null;

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        net.minecraft.data.DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper helper = event.getExistingFileHelper();

        CompletableFuture<HolderLookup.Provider> lookup = event.getLookupProvider();
        boolean server = event.includeServer();
        ModBlockTagsProvider blockTagsProvider = new ModBlockTagsProvider(output, lookup, helper);
        generator.addProvider(server, blockTagsProvider);
        generator.addProvider(server,new ModRecipeProvider(output));
        generator.addProvider(server, ModLootTableProvider.getProvider(output, lookup));



        boolean client = event.includeClient();
        generator.addProvider(client, new ModChineseProvider(output));
        generator.addProvider(client, new ModEnglishProvider(output));
        generator.addProvider(client, new ModItemModelProvider(output, helper));

        PROVIDERS = generator.getProvidersView();

    }
}
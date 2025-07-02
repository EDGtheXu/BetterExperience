package com.github.edg_thexu.better_experience.data.gen.loot;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModLootTableProvider extends LootTableProvider {

    public ModLootTableProvider(PackOutput output, Set<ResourceLocation > pRequiredTables, List<SubProviderEntry> subProviders) {
        super(output, pRequiredTables, subProviders);

    }

    public static LootTableProvider getProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProviderFuture) {
        return new LootTableProvider(output, Collections.emptySet(),
                List.of(
                        new SubProviderEntry(ModBlockLootProvider::new, LootContextParamSets.BLOCK)
                ));
    }
}
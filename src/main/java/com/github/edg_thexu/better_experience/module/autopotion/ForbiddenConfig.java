package com.github.edg_thexu.better_experience.module.autopotion;

import com.github.edg_thexu.better_experience.Better_experience;
import com.github.edg_thexu.better_experience.networks.s2c.SyncDataS2C;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.resource.ContextAwareReloadListener;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Reader;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

public class ForbiddenConfig extends ContextAwareReloadListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    Set<Item> forbiddenItems;
    Map<MobEffect, Integer> amplifiers;
    List<EffectAmp> effectAmps;
    Set<String> modId;

    record EffectAmp(MobEffect effect, int amp) {
        public static final Codec<EffectAmp> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                BuiltInRegistries.MOB_EFFECT.byNameCodec().fieldOf("effect").forGetter(EffectAmp::effect),
                Codec.INT.optionalFieldOf("amp").forGetter(i->Optional.of(i.amp))
        ).apply(instance, (effect, amp)-> new EffectAmp(effect, amp.orElse(0))));
    }

    static ForbiddenConfig instance;
    public static ForbiddenConfig getInstance() {
        if(instance == null) {
            instance = new ForbiddenConfig(new HashSet<>(), new HashMap<>());
        }
        return instance;
    }

    public boolean isItemForbidden(Item item) {
        return forbiddenItems.contains(item);
    }

    public boolean isEffectForbidden(MobEffect effect, int amp) {
        return amplifiers.containsKey(effect) && amplifiers.get(effect) <= amp;
    }

    public boolean isModForbidden(String modId) {
        return this.modId.contains(modId);
    }

    // from default
    ForbiddenConfig(Set<Item> forbiddenItems, Map<MobEffect, Integer> forbiddenEffects) {
        this.forbiddenItems = forbiddenItems;
        this.amplifiers = forbiddenEffects;
        this.effectAmps = new ArrayList<>();
        this.modId = new HashSet<>();
    }
    // from codec
    ForbiddenConfig(Set<Item> forbiddenItems, List<EffectAmp> effectAmps, Set<String> modId) {
        this.forbiddenItems = forbiddenItems;
        this.amplifiers = new HashMap<>();
        this.effectAmps = effectAmps;
        this.modId = modId;
    }

    public static final MapCodec<ForbiddenConfig> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BuiltInRegistries.ITEM.byNameCodec().listOf().fieldOf("forbidden_items").forGetter(i->i.forbiddenItems.stream().toList()),
//            Codec.unboundedMap(BuiltInRegistries.MOB_EFFECT.byNameCodec(),Codec.INT).fieldOf("forbidden_effects").forGetter(i->i.amplifiers)
            EffectAmp.CODEC.listOf().fieldOf("forbidden_effects").forGetter(i->i.effectAmps),
            Codec.STRING.listOf().optionalFieldOf("forbidden_modid_effect").forGetter(i->Optional.of(i.modId.stream().toList()))
    ).apply(instance, (item, effect, modId)-> modId
            .map(strings -> new ForbiddenConfig(new HashSet<>(item), effect, new HashSet<>(strings)))
            .orElseGet(() -> new ForbiddenConfig(new HashSet<>(item), effect, new HashSet<>()))));

    @Override
    public @NotNull CompletableFuture<Void> reload(PreparationBarrier  stage,
                                                   @NotNull ResourceManager resourceManager,
                                                   @NotNull ProfilerFiller preparationsProfiler,
                                                   @NotNull ProfilerFiller reloadProfiler,
                                                   @NotNull Executor backgroundExecutor,
                                                   @NotNull Executor gameExecutor) {
        getInstance().forbiddenItems.clear();
        getInstance().amplifiers.clear();
        getInstance().effectAmps.clear();
        return CompletableFuture.supplyAsync(() -> {
            ResourceLocation location = Better_experience.space("potion_config.json");
            Optional<Resource> file = resourceManager.getResource(location);
            if(file.isPresent()){
                try (Reader reader = file.get().openAsReader()) {
                    JsonObject jsonobject = GsonHelper.fromJson(GSON, reader, JsonObject.class);
                    return CODEC.codec().decode(JsonOps.INSTANCE, jsonobject).getOrThrow().getFirst();
                } catch (RuntimeException | IOException ioexception) {
                    Better_experience.LOGGER.error("Failed to load potion config {}", location, ioexception);
                }
            }
            return new ForbiddenConfig(new HashSet<>(), new HashMap<>());
        }, backgroundExecutor).thenCompose(stage::wait).thenAcceptAsync(config->{
            this.forbiddenItems.addAll(config.forbiddenItems);
            this.amplifiers.putAll(config.amplifiers);
            this.effectAmps.addAll(config.effectAmps);
            this.modId.addAll(config.modId);
            Better_experience.LOGGER.info("ForbiddenConfig reloaded");
            if(ServerLifecycleHooks.getCurrentServer() != null) {
                SyncDataS2C.syncForbiddenConfig();
            }
        }, gameExecutor);
    }

    public static void handleServer(ForbiddenConfig config){
        getInstance().forbiddenItems.clear();
        getInstance().amplifiers.clear();
        getInstance().effectAmps.clear();
        getInstance().forbiddenItems.addAll(config.forbiddenItems);
//        getInstance().effectAmps.addAll(config.effectAmps);
//        config.effectAmps.forEach(effectAmp -> getInstance().amplifiers.put(effectAmp.effect, effectAmp.amp));
        getInstance().amplifiers = config.effectAmps.stream().map(effectAmp -> new AbstractMap.SimpleEntry<>(effectAmp.effect, effectAmp.amp))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        getInstance().modId.addAll(config.modId);
    }

    public static void sync(ServerPlayer player){
        SyncDataS2C.syncForbiddenConfig(player);
    }

//    @Override
//    protected JsonObject defaultConfig(RegistryAccess registryAccess) {
//        ForbiddenConfig defaultConfig = new ForbiddenConfig(new HashSet<>(), new HashSet<>());
//        return CODEC.codec().encodeStart( registryAccess.createSerializationContext(JsonOps.INSTANCE), defaultConfig).getOrThrow().getAsJsonObject();
//    }
//
//    @Override
//    protected void initConfig(JsonObject jsonObject) {
//        ForbiddenConfig config = CODEC.codec().decode(JsonOps.INSTANCE, jsonObject).getOrThrow().getFirst();
//        this.forbiddenItems = config.forbiddenItems;
//        this.forbiddenEffects = config.forbiddenEffects;
//    }
}

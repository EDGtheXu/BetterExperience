package com.github.edg_thexu.better_experience.mixed;

import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;

public interface IPlayer {
    BlockEntity betterExperience$getInteractBlockEntity();

    void betterExperience$setInteractBlockEntity(BlockEntity blockEntity);

    int betterExperience$getHammerUsingTicks();

    void betterExperience$setHammerUsingTicks(int ticks);

    List<FishingHook> betterExperience$getFishingHookList();

    void betterExperience$setFishingHookList(List<FishingHook> fishingHookList);
}

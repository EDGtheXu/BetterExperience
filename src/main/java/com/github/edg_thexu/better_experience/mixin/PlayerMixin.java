package com.github.edg_thexu.better_experience.mixin;

import com.github.edg_thexu.better_experience.mixed.IPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.LinkedList;
import java.util.List;

@Mixin(Player.class)
public class PlayerMixin implements IPlayer {

    @Unique
    BlockEntity betterExperience$interactBlockEntity;

    @Unique
    List<FishingHook> betterExperience$fishingHookList = new LinkedList<>();

    @Unique
    int betterExperience$hammerUsingTicks;

    @Override
    public BlockEntity betterExperience$getInteractBlockEntity() {
        return betterExperience$interactBlockEntity;
    }

    @Override
    public void betterExperience$setInteractBlockEntity(BlockEntity blockEntity) {
        betterExperience$interactBlockEntity = blockEntity;
    }

    @Override
    public int betterExperience$getHammerUsingTicks() {
        return betterExperience$hammerUsingTicks;
    }

    @Override
    public void betterExperience$setHammerUsingTicks(int ticks) {
        betterExperience$hammerUsingTicks = ticks;
    }

    @Override
    public List<FishingHook> betterExperience$getFishingHookList() {
        return betterExperience$fishingHookList;
    }

    @Override
    public void betterExperience$setFishingHookList(List<FishingHook> fishingHookList) {
        betterExperience$fishingHookList = fishingHookList;
    }
}

package com.github.edg_thexu.better_experience.mixin;

import com.github.edg_thexu.better_experience.mixed.IPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Player.class)
public class PlayerMixin implements IPlayer {

    @Unique
    BlockEntity betterExperience$interactBlockEntity;

    @Override
    public BlockEntity betterExperience$getInteractBlockEntity() {
        return betterExperience$interactBlockEntity;
    }

    @Override
    public void betterExperience$setInteractBlockEntity(BlockEntity blockEntity) {
        betterExperience$interactBlockEntity = blockEntity;
    }
}

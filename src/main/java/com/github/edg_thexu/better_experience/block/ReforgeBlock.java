package com.github.edg_thexu.better_experience.block;

import com.github.edg_thexu.better_experience.init.ModBlocks;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ReforgeBlock extends BaseEntityBlock {

    public ReforgeBlock(Properties properties) {
        super(properties);
    }


    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ReforgeBlockEntity(blockPos, blockState);
    }


    public static class ReforgeBlockEntity extends BlockEntity {

        public ReforgeBlockEntity(BlockPos pos, BlockState blockState) {
            super(ModBlocks.REFORGE_BLOCK_ENTITY.get(), pos, blockState);
        }
    }
}

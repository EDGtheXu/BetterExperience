package com.github.edg_thexu.better_experience.block;

import com.github.edg_thexu.better_experience.init.ModBlocks;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.confluence.mod.common.menu.NPCReforgeMenu;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ForgeBlock extends BaseEntityBlock {

    public ForgeBlock(Properties properties) {
        super(properties);
    }
    public static final MapCodec<ForgeBlock> CODEC = simpleCodec(ForgeBlock::new);

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ForgeBlockEntity(blockPos, blockState);
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack stack, BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        if(state.hasBlockEntity()){
            player.openMenu(new SimpleMenuProvider((id, inventory, p)-> new NPCReforgeMenu(id, inventory), Component.translatable(ModBlocks.AUTO_SELL_BLOCK.get().getDescriptionId())));
        }
        return ItemInteractionResult.SUCCESS;
    }

    public static class ForgeBlockEntity extends BlockEntity {

        public ForgeBlockEntity(BlockPos pos, BlockState blockState) {
            super(ModBlocks.FORGE_BLOCK_ENTITY.get(), pos, blockState);
        }
    }
}

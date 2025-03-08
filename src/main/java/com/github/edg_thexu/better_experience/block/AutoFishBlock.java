package com.github.edg_thexu.better_experience.block;

import com.github.edg_thexu.better_experience.init.ModBlocks;
import com.github.edg_thexu.better_experience.mixed.IFishingHook;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.confluence.mod.common.init.item.FishingPoleItems;
import org.confluence.mod.common.item.fishing.AbstractFishingPole;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class AutoFishBlock extends BaseEntityBlock {

    public AutoFishBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return null;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new AutoFishMachineEntity(blockPos, blockState);
    }


    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        BlockEntity entity = level.getBlockEntity(pos);
        if(entity instanceof AutoFishMachineEntity entity1){
            if(entity1.findTarget())
                entity1.owner = player;
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }


    @Override
    public <T extends BlockEntity> BlockEntityTicker getTicker(@NotNull Level pLevel, @NotNull BlockState pState, @NotNull BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide ? null : createTickerHelper(pBlockEntityType, ModBlocks.AUTO_FISH_BLOCK_ENTITY.get(),  (level, pos, state, entity)-> {
            entity.ticks++;
            Player player = entity.owner;

            // 20tick 触发自动钓鱼
            if(level instanceof ServerLevel serverLevel && player!= null && entity.target != null && entity.ticks % 20 == 0){
                ItemStack stack = FishingPoleItems.GOLDEN_FISHING_ROD.toStack();

                if(stack.getItem() instanceof AbstractFishingPole pole) {
                    FishingHook hook;
                    try {
                        Method funcField = AbstractFishingPole.class.getDeclaredMethod("getHook", ItemStack.class, Player.class, Level.class, int.class, int.class);
                        funcField.setAccessible(true);
                        hook = (FishingHook) funcField.invoke(pole,stack, player, level, 50,5);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }

                    ((IFishingHook) hook).betterExperience$setPos(new Vec3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5));
                    ((IFishingHook) hook).betterExperience$setSimulation(true);

                    hook.setPos(entity.target);

                    try {
                        Field nibbleField = FishingHook.class.getDeclaredField("nibble");
                        nibbleField.setAccessible(true);
                        nibbleField.setInt(hook, 10);
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        e.printStackTrace();
                    }

                    hook.retrieve(stack);
                }
            }


        });
    }
    public static class AutoFishMachineEntity extends BlockEntity {

        int ticks = 0;
        Player owner;
        // 水中位置
        Vec3 target;
        public AutoFishMachineEntity(BlockPos pos, BlockState blockState) {
            super(ModBlocks.AUTO_FISH_BLOCK_ENTITY.get(), pos, blockState);
        }

        public boolean findTarget(){
            // 寻找水中位置
            BlockPos pos = this.getBlockPos();
            // 水下正下方
            for(int i = 1; i < 10; i++){
                if(this.getLevel().getBlockState(pos.below(i)).is(Blocks.WATER)){
                    target = new Vec3(pos.getX() + 0.5, pos.getY() + i, pos.getZ() + 0.5);
                    return true;
                }
            }

            return false;
        }

    }
}

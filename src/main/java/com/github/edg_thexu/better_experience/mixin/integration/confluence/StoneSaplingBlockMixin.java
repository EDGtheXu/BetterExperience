package com.github.edg_thexu.better_experience.mixin.integration.confluence;

import com.github.edg_thexu.better_experience.config.CommonConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import org.confluence.mod.common.block.natural.sapling.StoneSaplingBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
@Pseudo
@Mixin(targets = "org.confluence.mod.common.block.natural.sapling.StoneSaplingBlock")
//@Mixin(StoneSaplingBlock.class)
public class StoneSaplingBlockMixin {


    @Inject(method = "randomTick", at = @At("HEAD"), cancellable = true)
    private void randomTickMixin(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        if(CommonConfig.STONE_SAPLING_TREE_NO_STRICT.get()){
            if (level.isAreaLoaded(pos, 1)) {
                ((StoneSaplingBlock)(Object)this).advanceTree(level, pos, state, random);
            }
            ci.cancel();
        }
    }
    @Inject(method = "isValidBonemealTarget", at = @At("RETURN"), cancellable = true)
    private void isValidBonemealTargetMixin(LevelReader level, BlockPos pos, BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if(CommonConfig.VALID_BONEMEAL_TARGET.get())
            cir.setReturnValue(true);
    }
}

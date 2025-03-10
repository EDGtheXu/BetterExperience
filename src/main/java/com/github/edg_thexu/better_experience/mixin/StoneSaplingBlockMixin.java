package com.github.edg_thexu.better_experience.mixin;

import com.github.edg_thexu.better_experience.config.ServerConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import org.confluence.mod.common.block.natural.sapling.StoneSaplingBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StoneSaplingBlock.class)
public class StoneSaplingBlockMixin {


    @Inject(method = "randomTick", at = @At("HEAD"), cancellable = true)
    private void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        if(ServerConfig.STONE_SAPLING_TREE_NO_STRICT.get()){
            if (level.isAreaLoaded(pos, 1)) {
                ((StoneSaplingBlock)(Object)this).advanceTree(level, pos, state, random);
            }
            ci.cancel();
        }
    }
}

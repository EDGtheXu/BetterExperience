package com.github.edg_thexu.better_experience.module.boomstaff;

import com.github.edg_thexu.better_experience.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;

import static net.minecraft.world.level.block.Block.getId;

public class ExplodeManager {
    private int maxTickToHandle = 20;
    List<BlockQueue> playerBlockQueneList;

    private static ExplodeManager instance;

    public static ExplodeManager getInstance() {
        if (instance == null) {
            instance = new ExplodeManager();
            instance.playerBlockQueneList = new CopyOnWriteArrayList<>();
        }
        return instance;
    }

    public void addBlockToQueue(BlockQueue blockPosQueue) {
        playerBlockQueneList.add(blockPosQueue);
    }

    public void tickHandle(){

        for (BlockQueue blockPosQueue : playerBlockQueneList) {
            Queue<Tuple<BlockPos, Boolean>> blockQueue = blockPosQueue.blockQueue;
            if(blockQueue.isEmpty()) {
                playerBlockQueneList.remove(blockPosQueue);
                continue;
            }
            Player player = blockPosQueue.player;
            Vec3 center = blockPosQueue.center;
            Level level = player.level();

            List<ItemStack> allDrops = new ArrayList<>();
            double ymax = center.y;
            for (int i = 0; i < maxTickToHandle; i++) {
                if (blockQueue.isEmpty()) {
                    break;
                }
                Tuple<BlockPos, Boolean> blockPos = blockQueue.poll();
                BlockPos pos = blockPos.getA();
                ymax = pos.getY();
                boolean isDrop = blockPos.getB();
                BlockState state = level.getBlockState(pos);
                Block block = state.getBlock();
                BlockEntity entity2 = level.getBlockEntity(pos);
                if(player instanceof ServerPlayer sp) {
                    sp.gameMode.destroyAndAck(pos, 2, "destroyed");
                    if(level.random.nextFloat() < 0.1f)
                        level.levelEvent(2001, pos, getId(state));

                    if(isDrop) {
                        var drops = Block.getDrops(state, (ServerLevel) level, pos, entity2, player, player.getOffhandItem());
//                        for (var drop : drops) {
//                            ItemEntity entity = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), drop);
//                            entity.setDeltaMovement(center.normalize().scale(0.1));
//                            level.addFreshEntity(entity);
//                        }
                        allDrops.addAll(drops);
                    }
                }
                if(level.random.nextFloat() < 0.05f) {
                    level.playSound(null, pos, SoundEvents.GENERIC_EXPLODE.value(), SoundSource.BLOCKS, 0.2f,0.6f);
                    ((ServerLevel)level).sendParticles(ParticleTypes.EXPLOSION, pos.getX(), pos.getY(), pos.getZ(), 1, 0.5, 0.1,0,0);
                }
            }
            ModUtils.unionItemStacks(allDrops);

            for (var drop : allDrops) {
                if(drop.isEmpty()) continue;
                ItemEntity entity = new ItemEntity(level, center.x,  ymax+1, center.z, drop);
                entity.setDeltaMovement(center.normalize().scale(0.1));
                level.addFreshEntity(entity);
            }
            if (blockQueue.isEmpty()) {
                playerBlockQueneList.remove(player);
            }
        }
    }


    public record BlockQueue(Queue<Tuple<BlockPos, Boolean>> blockQueue, Vec3 center, Player player) {

    }

}

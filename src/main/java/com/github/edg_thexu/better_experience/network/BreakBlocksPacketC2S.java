package com.github.edg_thexu.better_experience.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.confluence.mod.Confluence;

public record BreakBlocksPacketC2S(BlockPos p1, BlockPos p2) implements CustomPacketPayload {

    public static final StreamCodec<ByteBuf, BreakBlocksPacketC2S> STREAM_CODEC = StreamCodec.composite(
                    BlockPos.STREAM_CODEC, BreakBlocksPacketC2S::p1,
                    BlockPos.STREAM_CODEC, BreakBlocksPacketC2S::p2,
                    BreakBlocksPacketC2S::new
            );

    public static final Type<BreakBlocksPacketC2S> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(Confluence.MODID, "break_blocks_packet_c2s"));

    public Type<? extends CustomPacketPayload> type() {
        return TYPE;

    }

    public static void handle(BreakBlocksPacketC2S packet, final IPayloadContext context) {
        context.enqueueWork(() -> {
            int x1 = Math.min(packet.p1().getX(), packet.p2().getX());
            int y1 = Math.min(packet.p1().getY(), packet.p2().getY());
            int z1 = Math.min(packet.p1().getZ(), packet.p2().getZ());
            int x2 = Math.max(packet.p1().getX(), packet.p2().getX());
            int y2 = Math.max(packet.p1().getY(), packet.p2().getY());
            int z2 = Math.max(packet.p1().getZ(), packet.p2().getZ());
            int centerX = (x2 + x1) / 2;
            int centerY = (y2 + y1) / 2;
            int centerZ = (z2 + z1) / 2;
            ServerPlayer player = (ServerPlayer) context.player();
            Level level = player.level();
            for(int x = x1; x < x2; x++){
                for(int y = y1; y < y2; y++){
                    for(int z = z1; z < z2; z++){
                        BlockPos pos = new BlockPos(x, y, z);
                        BlockState state = level.getBlockState(pos);

                        if(state.requiresCorrectToolForDrops() && !player.getOffhandItem().isCorrectToolForDrops(state)){
                            continue;
                        }

                        var drops = Block.getDrops(level.getBlockState(pos),(ServerLevel) level,  pos, level.getBlockEntity(pos), player, player.getOffhandItem());
                        player.gameMode.destroyAndAck(pos, 0, "destroyed");
                        for(var drop : drops){
                            ItemEntity entity = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), drop);
                            Vec3 center = new Vec3(centerX - x, centerY - y, centerZ - z);
                            entity.setDeltaMovement(center.normalize().scale(0.1));
                            level.addFreshEntity(entity);
                        }

                    }
                }
            }

        });
    }
}

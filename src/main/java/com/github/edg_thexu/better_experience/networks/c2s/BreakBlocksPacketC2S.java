package com.github.edg_thexu.better_experience.networks.c2s;

import com.github.edg_thexu.better_experience.Better_experience;
import com.github.edg_thexu.better_experience.attachment.EnderChestAttachment;
import com.github.edg_thexu.better_experience.config.CommonConfig;
import com.github.edg_thexu.better_experience.item.MagicBoomStaff;
import com.github.edg_thexu.better_experience.module.boomstaff.ExplodeManager;
import com.github.edg_thexu.better_experience.networks.s2c.EnderChestItemsS2C;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;


import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Supplier;

public class BreakBlocksPacketC2S{

//    public static final StreamCodec<ByteBuf, BreakBlocksPacketC2S> STREAM_CODEC = StreamCodec.composite(
//                    BlockPos.STREAM_CODEC, BreakBlocksPacketC2S::p1,
//                    BlockPos.STREAM_CODEC, BreakBlocksPacketC2S::p2,
//                    BreakBlocksPacketC2S::new
//            );

    BlockPos p1;
    BlockPos p2;
    public BreakBlocksPacketC2S(BlockPos p1, BlockPos p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    public BreakBlocksPacketC2S(FriendlyByteBuf buf) {
        this(buf.readBlockPos(), buf.readBlockPos());
    }

    public static BreakBlocksPacketC2S decode(FriendlyByteBuf buffer) {
        return new BreakBlocksPacketC2S(buffer);
    }

    public static void encode(BreakBlocksPacketC2S packet, FriendlyByteBuf buf) {
        buf.writeBlockPos(packet.p1);
        buf.writeBlockPos(packet.p2);
    }

    public BlockPos p1() {
        return p1;
    }

    public BlockPos p2() {
        return p2;
    }

    public static void handle(BreakBlocksPacketC2S packet, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            if(CommonConfig.FORBIDDEN_MAGIC_BOOM_STAFF.get()){
                context.getSender().sendSystemMessage(Component.translatable("better_experience.info.forbidden_magic_boom_staff").withStyle(style -> style.withColor(0xbc6538)));
                return;
            }
            ItemStack stack = context.getSender().getMainHandItem();
            if(stack.getItem() instanceof MagicBoomStaff staff){
                context.getSender().getCooldowns().addCooldown(staff, 20);
            }
            int x1 = Math.min(packet.p1().getX(), packet.p2().getX());
            int y1 = Math.min(packet.p1().getY(), packet.p2().getY());
            int z1 = Math.min(packet.p1().getZ(), packet.p2().getZ());
            int x2 = Math.max(packet.p1().getX(), packet.p2().getX());
            int y2 = Math.max(packet.p1().getY(), packet.p2().getY());
            int z2 = Math.max(packet.p1().getZ(), packet.p2().getZ());
            int centerX = (x2 + x1) / 2;
            int centerY = (y2 + y1) / 2;
            int centerZ = (z2 + z1) / 2;
            ServerPlayer player = (ServerPlayer) context.getSender();
            Level level = player.level();

            Queue<Tuple<BlockPos, Boolean>> blocks = new LinkedList<>();
            for(int y = y2; y >= y1; y--){
                for(int x = x1; x <= x2; x++){
                    for(int z = z1; z <= z2; z++){
                        BlockPos pos = new BlockPos(x, y, z);
                        BlockState state = level.getBlockState(pos);
                        Block block = state.getBlock();
                        if(state.isAir() || state.is(Blocks.BEDROCK)) continue;
                        boolean isDrop = true;
                        if(state.requiresCorrectToolForDrops() && !player.getOffhandItem().isCorrectToolForDrops(state)){
                            if(block != Blocks.SNOW_BLOCK){
                                continue;
                            }
                            isDrop = false;
                        }
                        BlockEntity entity2 = level.getBlockEntity(pos);
                        if(entity2 instanceof ChestBlockEntity entity1 && !entity1.isEmpty()){
                            continue;
                        }
                        blocks.add(new Tuple<>(pos, isDrop));
                    }
                }
            }
            ExplodeManager.BlockQueue queue = new ExplodeManager.BlockQueue(blocks, new Vec3(centerX, centerY, centerZ), player);
            ExplodeManager.getInstance().addBlockToQueue(queue);

        });
    }
}

package com.github.edg_thexu.better_experience.networks.c2s;

import com.github.edg_thexu.better_experience.Better_experience;
import com.github.edg_thexu.better_experience.block.AutoFishBlock;
import com.github.edg_thexu.better_experience.mixed.IPlayer;
import com.github.edg_thexu.better_experience.module.faststorage.StorageManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record ServerBoundPacketC2S(int code) implements CustomPacketPayload {

    public static final StreamCodec<ByteBuf, ServerBoundPacketC2S> STREAM_CODEC =
            ByteBufCodecs.INT.map(ServerBoundPacketC2S::new, ServerBoundPacketC2S::code);

    public static final Type<ServerBoundPacketC2S> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(Better_experience.MODID, "serverbound_packet_c2s"));

    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ServerBoundPacketC2S packet, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            ServerLevel level = (ServerLevel) player.level();
            if(packet.code == 1){
                // 自动钓鱼机器开始
                if(((IPlayer)player).betterExperience$getInteractBlockEntity() instanceof AutoFishBlock.AutoFishMachineEntity entity){
                    entity.tryStart(player);
                }
            }
            else if(packet.code == 2){
                // 自动钓鱼机器停止
                if(((IPlayer)player).betterExperience$getInteractBlockEntity() instanceof AutoFishBlock.AutoFishMachineEntity entity){
                    entity.stop();
                    player.sendSystemMessage(Component.literal("stopped fishing"));
                }
            }else if(packet.code == 3){
                // 复现钓鱼bug
                ((IPlayer)player).betterExperience$setHammerUsingTicks((int) (20 / player.getAttributeValue(Attributes.ATTACK_SPEED)));

            }else if(packet.code == 4){
                // 一键堆叠
                StorageManager.saveAll(player);
            }
        });
    }


    public static void notifyStart(){
        PacketDistributor.sendToServer(new ServerBoundPacketC2S(1));
    }
}

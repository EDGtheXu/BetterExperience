package com.github.edg_thexu.better_experience.network.C2S;

import com.github.edg_thexu.better_experience.Better_experience;
import com.github.edg_thexu.better_experience.attachment.AutoPotionAttachment;
import com.github.edg_thexu.better_experience.module.autopotion.PlayerInventoryManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record PotionApplyPacketC2S(AutoPotionAttachment attachment) implements CustomPacketPayload {

    public static final StreamCodec<ByteBuf, PotionApplyPacketC2S> STREAM_CODEC = ByteBufCodecs.fromCodec(
            CompoundTag.CODEC.xmap(tag->{
                        AutoPotionAttachment attachment1 = new AutoPotionAttachment();
                        attachment1.deserializeNBT(null, tag);
                        return new PotionApplyPacketC2S(attachment1);
                    }, packet -> packet.attachment().serializeNBT(null)
            ));

    public static final Type<PotionApplyPacketC2S> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(Better_experience.MODID, "potion_auto_apply_packet_c2s"));

    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(PotionApplyPacketC2S packet, final IPayloadContext context) {
        context.enqueueWork(() -> {
            PlayerInventoryManager.apply(packet.attachment, context.player());
        });
    }
}

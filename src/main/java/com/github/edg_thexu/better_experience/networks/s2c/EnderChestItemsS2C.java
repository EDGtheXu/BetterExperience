package com.github.edg_thexu.better_experience.networks.s2c;

import com.github.edg_thexu.better_experience.Better_experience;
import com.github.edg_thexu.better_experience.attachment.EnderChestAttachment;
import com.github.edg_thexu.better_experience.init.ModAttachments;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record EnderChestItemsS2C(EnderChestAttachment attachment) implements CustomPacketPayload {

    public static final StreamCodec<ByteBuf, EnderChestItemsS2C> STREAM_CODEC = ByteBufCodecs.fromCodec(
            CompoundTag.CODEC.xmap(tag->{
                EnderChestAttachment attachment1 = new EnderChestAttachment();
                        attachment1.deserializeNBT(null,tag);
                        return new EnderChestItemsS2C(attachment1);
                    }, packet -> packet.attachment().serializeNBT(null)
            ));

    public static final Type<EnderChestItemsS2C> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(Better_experience.MODID, "ender_chest_items_packet_s2c"));

    public Type<? extends CustomPacketPayload> type() {
        return TYPE;

    }

    public static void handle(EnderChestItemsS2C packet, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Minecraft.getInstance().player.getData(ModAttachments.ENDER_CHEST).refresh(packet.attachment);
        });
    }
}

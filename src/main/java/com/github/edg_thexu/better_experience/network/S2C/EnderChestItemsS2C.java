package com.github.edg_thexu.better_experience.network.S2C;

import com.github.edg_thexu.better_experience.attachment.EnderChestAttachment;
import com.github.edg_thexu.better_experience.init.ModAttachments;
import com.github.edg_thexu.better_experience.item.MagicBoomStaff;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.confluence.mod.Confluence;
import org.confluence.mod.common.init.ModDataComponentTypes;

public record EnderChestItemsS2C(EnderChestAttachment attachment) implements CustomPacketPayload {

    public static final StreamCodec<ByteBuf, EnderChestItemsS2C> STREAM_CODEC = ByteBufCodecs.fromCodec(
            CompoundTag.CODEC.xmap(tag->{
                EnderChestAttachment attachment1 = new EnderChestAttachment();
                        attachment1.deserializeNBT(null,tag);
                        return new EnderChestItemsS2C(attachment1);
                    }, packet -> packet.attachment().serializeNBT(null)
            ));

    public static final Type<EnderChestItemsS2C> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(Confluence.MODID, "ender_chest_items_packet_s2c"));

    public Type<? extends CustomPacketPayload> type() {
        return TYPE;

    }

    public static void handle(EnderChestItemsS2C packet, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Minecraft.getInstance().player.getData(ModAttachments.ENDER_CHEST).refresh(packet.attachment);
        });
    }
}

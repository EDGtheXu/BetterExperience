package com.github.edg_thexu.better_experience.network.C2S;

import com.github.edg_thexu.better_experience.Better_experience;
import com.github.edg_thexu.better_experience.module.autopotion.PlayerInventoryManager;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record PotionApplyPacketC2S(ItemStack itemStack) implements CustomPacketPayload {

    public static final StreamCodec<RegistryFriendlyByteBuf, PotionApplyPacketC2S> STREAM_CODEC =
            ItemStack.STREAM_CODEC.map(PotionApplyPacketC2S::new, PotionApplyPacketC2S::itemStack);

    public static final Type<PotionApplyPacketC2S> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(Better_experience.MODID, "potion_auto_apply_packet_c2s"));

    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(PotionApplyPacketC2S packet, final IPayloadContext context) {
        context.enqueueWork(() -> {
            PlayerInventoryManager.apply(packet.itemStack, context.player());

        });
    }
}

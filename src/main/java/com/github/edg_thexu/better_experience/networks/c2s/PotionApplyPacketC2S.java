package com.github.edg_thexu.better_experience.networks.c2s;

import com.github.edg_thexu.better_experience.attachment.AutoPotionAttachment;
import com.github.edg_thexu.better_experience.config.CommonConfig;
import com.github.edg_thexu.better_experience.module.autopotion.PlayerInventoryManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PotionApplyPacketC2S {

    AutoPotionAttachment attachment;

    public PotionApplyPacketC2S(AutoPotionAttachment attachment) {
        this.attachment = attachment;
    }

    public PotionApplyPacketC2S(FriendlyByteBuf buf) {
        CompoundTag tag = buf.readAnySizeNbt();
        attachment = new AutoPotionAttachment();
        attachment.deserializeNBT(tag);
    }

    public static PotionApplyPacketC2S decode(FriendlyByteBuf buffer) {
        return new PotionApplyPacketC2S(buffer);
    }

    public static void encode(PotionApplyPacketC2S packet, FriendlyByteBuf buf) {
        buf.writeNbt(packet.attachment.serializeNBT());
    }

    public static void handle(PotionApplyPacketC2S packet, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            if(CommonConfig.AUTO_POTION_OPEN.get()) { // 在服务端进行判断
                PlayerInventoryManager.apply(packet.attachment, context.getSender());
            }else{
                // 清除效果
                PlayerInventoryManager.apply(new AutoPotionAttachment(), context.getSender());
            }
        });
    }
}

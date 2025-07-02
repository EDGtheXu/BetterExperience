package com.github.edg_thexu.better_experience.networks.s2c;

import com.github.edg_thexu.better_experience.attachment.EnderChestAttachment;
import com.github.edg_thexu.better_experience.init.ModAttachments;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class EnderChestItemsS2C{
    public enum TypeIndex {
        ENDER,
        PIG,
        SAFE
    }

    EnderChestAttachment attachment;
    TypeIndex typeIndex;

    public EnderChestItemsS2C(EnderChestAttachment attachment, TypeIndex typeIndex) {
        this.attachment = attachment;
        this.typeIndex = typeIndex;
    }

    public EnderChestItemsS2C(FriendlyByteBuf buf) {
        this.typeIndex = buf.readEnum(TypeIndex.class);
        EnderChestAttachment attachment = new EnderChestAttachment();
        attachment.deserializeNBT(buf.readAnySizeNbt());
        this.attachment = attachment;
    }

    public static EnderChestItemsS2C decode(FriendlyByteBuf buffer) {
        return new EnderChestItemsS2C(buffer);
    }

    public static void encode(EnderChestItemsS2C packet, FriendlyByteBuf buf) {
        buf.writeEnum(packet.typeIndex);
        buf.writeNbt(packet.attachment.serializeNBT());
    }


    public static void handle(EnderChestItemsS2C packet, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            if(packet.typeIndex == TypeIndex.ENDER) {

                Minecraft.getInstance().player.getCapability(ModAttachments.ENDER_CHEST).ifPresent(cap->{
                    cap.refresh(packet.attachment);
                });
            }
//            else if(packet.typeIndex == TypeIndex.PIG){
//                Minecraft.getInstance().player.getData(ModAttachments.PIG_CHEST).refresh(packet.attachment);
//            }else if(packet.typeIndex == TypeIndex.SAFE){
//
//                Minecraft.getInstance().player.getData(ModAttachments.SAFE_CHEST).refresh(packet.attachment);
//            }
        });
    }
}

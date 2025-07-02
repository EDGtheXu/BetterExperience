package com.github.edg_thexu.better_experience.networks.s2c;

import com.github.edg_thexu.better_experience.config.CommonConfig;
import com.github.edg_thexu.better_experience.module.autopotion.PlayerInventoryManager;
import com.github.edg_thexu.better_experience.utils.ModUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;


import java.util.function.Supplier;

public class ClientBoundConfigPacket {
    
    int message;
    public ClientBoundConfigPacket(int message) {
        this.message = message;
    }

    public ClientBoundConfigPacket(FriendlyByteBuf buf) {
        this.message = buf.readInt();
    }

    public static ClientBoundConfigPacket decode(FriendlyByteBuf buffer) {
        return new ClientBoundConfigPacket(buffer);
    }

    public static void encode(ClientBoundConfigPacket packet, FriendlyByteBuf buf) {
        buf.writeInt(packet.message);
    }


    public static void handle(ClientBoundConfigPacket packet,  Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            if(packet.message == 0) {
                PlayerInventoryManager.getInstance().serverOpenAutoPotion = false;
            }else if(packet.message == 1) {
                PlayerInventoryManager.getInstance().serverOpenAutoPotion = true;
            }

        });
    }

    public static void sync(ServerPlayer player){
        ModUtils.sendToPlayer(player, new ClientBoundConfigPacket(CommonConfig.AUTO_POTION_OPEN.get()? 1 : 0));
    }

    public static void syncAll(){
        ModUtils.sendToAllPlayers(new ClientBoundConfigPacket(CommonConfig.AUTO_POTION_OPEN.get()? 1 : 0));
    }
}

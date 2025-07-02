package com.github.edg_thexu.better_experience.networks;


import com.github.edg_thexu.better_experience.Better_experience;
import com.github.edg_thexu.better_experience.intergration.jei.JeiHelper;
import com.github.edg_thexu.better_experience.networks.c2s.BreakBlocksPacketC2S;
import com.github.edg_thexu.better_experience.networks.c2s.PotionApplyPacketC2S;
import com.github.edg_thexu.better_experience.networks.c2s.SearchJeiIngredientsPacketC2S;
import com.github.edg_thexu.better_experience.networks.c2s.ServerBoundPacketC2S;
import com.github.edg_thexu.better_experience.networks.s2c.ClientBoundConfigPacket;
import com.github.edg_thexu.better_experience.networks.s2c.EnderChestItemsS2C;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;


public final class NetworkHandler {
    public static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(Better_experience.space("main"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

    public static void register() {
        int packetId = 0;

        CHANNEL.registerMessage(packetId++,  PotionApplyPacketC2S.class,  PotionApplyPacketC2S::encode,  PotionApplyPacketC2S::decode,  PotionApplyPacketC2S::handle);
        CHANNEL.registerMessage(packetId++,  BreakBlocksPacketC2S.class,  BreakBlocksPacketC2S::encode,  BreakBlocksPacketC2S::decode,  BreakBlocksPacketC2S::handle);
        CHANNEL.registerMessage(packetId++,  ServerBoundPacketC2S.class,  ServerBoundPacketC2S::encode,  ServerBoundPacketC2S::decode,  ServerBoundPacketC2S::handle);
        if(JeiHelper.isLoaded()) {
            CHANNEL.registerMessage(packetId++,  SearchJeiIngredientsPacketC2S.class,  SearchJeiIngredientsPacketC2S::encode,  SearchJeiIngredientsPacketC2S::decode,  SearchJeiIngredientsPacketC2S::handle);
        }

        CHANNEL.registerMessage(packetId++,  EnderChestItemsS2C.class,  EnderChestItemsS2C::encode,  EnderChestItemsS2C::decode,  EnderChestItemsS2C::handle);
        CHANNEL.registerMessage(packetId++,  ClientBoundConfigPacket.class,  ClientBoundConfigPacket::encode,  ClientBoundConfigPacket::decode,  ClientBoundConfigPacket::handle);
    }
}

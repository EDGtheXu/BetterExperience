package com.github.edg_thexu.better_experience.networks.c2s;

import com.github.edg_thexu.better_experience.Better_experience;
import com.github.edg_thexu.better_experience.block.AutoFishBlock;
import com.github.edg_thexu.better_experience.data.component.ItemContainerComponent;
import com.github.edg_thexu.better_experience.intergration.jei.JeiHelper;
import com.github.edg_thexu.better_experience.menu.PotionBagMenu;
import com.github.edg_thexu.better_experience.mixed.IPlayer;
import com.github.edg_thexu.better_experience.module.faststorage.StorageManager;
import com.github.edg_thexu.better_experience.registries.recipehandler.IRecipeHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
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

public record SearchJeiIngredientsPacketC2S(IRecipeHandler<?> handler) implements CustomPacketPayload {

    public static final StreamCodec<RegistryFriendlyByteBuf, SearchJeiIngredientsPacketC2S> STREAM_CODEC = IRecipeHandler.STREAM_CODEC
            .map(SearchJeiIngredientsPacketC2S::new, SearchJeiIngredientsPacketC2S::handler);

    public static final Type<SearchJeiIngredientsPacketC2S> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(Better_experience.MODID, "search_jei_ingredients_packet_c2s"));

    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(SearchJeiIngredientsPacketC2S packet, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            ServerLevel level = (ServerLevel) player.level();
                // 搜寻附近材料并收入背包
            JeiHelper.findIngredients(packet.handler, player);

        });
    }

}

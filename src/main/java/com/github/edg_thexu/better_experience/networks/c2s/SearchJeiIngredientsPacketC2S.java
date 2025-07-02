package com.github.edg_thexu.better_experience.networks.c2s;

import com.github.edg_thexu.better_experience.intergration.jei.JeiHelper;
import com.github.edg_thexu.better_experience.registries.recipehandler.IRecipeHandler;

import com.github.edg_thexu.better_experience.registries.recipehandler.RecipeHandlerProviderTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SearchJeiIngredientsPacketC2S {

    IRecipeHandler<?> handler;


    public SearchJeiIngredientsPacketC2S(IRecipeHandler<?> handler) {
        this.handler = handler;

    }

    public SearchJeiIngredientsPacketC2S(FriendlyByteBuf buf) {
        this.handler = buf.readJsonWithCodec(RecipeHandlerProviderTypes.TYPED_CODEC.get());
    }

    public static SearchJeiIngredientsPacketC2S decode(FriendlyByteBuf buffer) {
        return new SearchJeiIngredientsPacketC2S(buffer);
    }

    public static void encode(SearchJeiIngredientsPacketC2S packet, FriendlyByteBuf buf) {
        buf.writeJsonWithCodec(RecipeHandlerProviderTypes.TYPED_CODEC.get(), packet.handler);
    }
    
    public static void handle(SearchJeiIngredientsPacketC2S packet, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            Player player = context.getSender();
            ServerLevel level = (ServerLevel) player.level();
                // 搜寻附近材料并收入背包
            JeiHelper.findIngredients(packet.handler, player);

        });
    }

}

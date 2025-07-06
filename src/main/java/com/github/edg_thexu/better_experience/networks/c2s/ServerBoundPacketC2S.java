package com.github.edg_thexu.better_experience.networks.c2s;

import com.github.edg_thexu.better_experience.block.AutoFishBlock;
import com.github.edg_thexu.better_experience.data.component.ItemContainerComponent;
import com.github.edg_thexu.better_experience.init.ModDataComponentTypes;
import com.github.edg_thexu.better_experience.menu.PotionBagMenu;
import com.github.edg_thexu.better_experience.mixed.IPlayer;
import com.github.edg_thexu.better_experience.module.faststorage.StorageManager;
import com.github.edg_thexu.better_experience.utils.ModUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerBoundPacketC2S {

    int code;

    public ServerBoundPacketC2S(int code) {
        this.code = code;
    }

    public ServerBoundPacketC2S(FriendlyByteBuf buf) {
        this.code = buf.readInt();
    }

    public static ServerBoundPacketC2S decode(FriendlyByteBuf buffer) {
        return new ServerBoundPacketC2S(buffer);
    }

    public static void encode(ServerBoundPacketC2S packet, FriendlyByteBuf buf) {
        buf.writeInt(packet.code);
    }
    
    public static void handle(ServerBoundPacketC2S packet, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            Player player = context.getSender();
            ServerLevel level = (ServerLevel) player.level();
            if(packet.code == 1){
                // 自动钓鱼机器开始
                if(((IPlayer)player).betterExperience$getInteractBlockEntity() instanceof AutoFishBlock.AutoFishMachineEntity entity){
                    entity.tryStart(player);
                }
            }
            else if(packet.code == 2){
                // 自动钓鱼机器停止
                if(((IPlayer)player).betterExperience$getInteractBlockEntity() instanceof AutoFishBlock.AutoFishMachineEntity entity){
                    entity.stop();
                    player.sendSystemMessage(Component.literal("stopped fishing"));
                }
            }else if(packet.code == 3){
                // 复现钓鱼bug
                ((IPlayer)player).betterExperience$setHammerUsingTicks((int) (20 / player.getAttributeValue(Attributes.ATTACK_SPEED)));

            }else if(packet.code == 4){
                // 一键堆叠
                StorageManager.saveAll(player);
            }else if(packet.code == 5){
                // 改变药水收纳状态
                if(player.containerMenu instanceof PotionBagMenu  menu &&  menu.container instanceof ItemContainerComponent component){
                    component.setAutoCollect(!component.isAutoCollect());
                    ItemStack stack = player.getMainHandItem();
                    component.writeToNBT(stack.getOrCreateTag());
                }

            } else if(packet.code == 15){
                // 保存药水袋数据
                if(player.containerMenu instanceof PotionBagMenu menu && menu.container instanceof ItemContainerComponent component){
                    ItemStack stack = player.getMainHandItem();
                    component.writeToNBT(stack.getOrCreateTag());
                }

            }
        });
    }


    public static void notifyStart(){
        ModUtils.sendToServer(new ServerBoundPacketC2S(1));
    }
}

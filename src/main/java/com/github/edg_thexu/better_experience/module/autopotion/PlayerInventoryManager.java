package com.github.edg_thexu.better_experience.module.autopotion;

import com.github.edg_thexu.better_experience.config.ServerConfig;
import com.github.edg_thexu.better_experience.network.PotionApplyPacketC2S;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import org.confluence.mod.common.item.potion.EffectPotionItem;

import java.lang.reflect.Method;

public class PlayerInventoryManager {

    public int detectInternal;
    public int _detectInternal = 100;
    public void detect(Player player){
        if(!player.level().isClientSide())
            return;
        if(!ServerConfig.AUTO_POTION_OPEN.get())
            return;
        if(--detectInternal > 0){
            return;
        }
        detectInternal = _detectInternal;
        Inventory inventory = player.getInventory();
        for(int i = 0; i < inventory.getContainerSize(); i++) {
            try {
                ItemStack stack = inventory.getItem(i);
                if(stack.getCount() >= ServerConfig.AUTO_POTION_STACK_SIZE.get() && stack.getItem() instanceof EffectPotionItem potion){
                    PacketDistributor.sendToServer(new PotionApplyPacketC2S(stack));
                }
            }catch (Exception ignored){

            }
        }
    }

    public static void apply(ItemStack stack, Player player){
        try {
            if(stack.getCount() >= 10 && stack.getItem() instanceof EffectPotionItem potion){
                Method method = potion.getClass().getDeclaredMethod("apply", ItemStack.class, Level.class, LivingEntity.class);
                method.setAccessible(true);
                method.invoke(potion, stack, player.level(), player);
            }
        }catch (Exception ignored){

        }
    }

}

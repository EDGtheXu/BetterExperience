package com.github.edg_thexu.better_experience.item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class UniversalController extends Item {

    public UniversalController(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if(!level.isClientSide() && usedHand == InteractionHand.MAIN_HAND) {
            long time = level.getDayTime() % 24000;
            long finalTime = level.getDayTime() / 24000 * 24000;
            if(time < 6000){
                time = 6000;
            }else if(time < 13798){
                time = 13798;
            }else if(time < 18000){
                time = 18000;
            }else {
                time = 24000;
            }
            ((ServerLevel)level).setDayTime(finalTime + time);

        }
        return InteractionResultHolder.success(player.getItemInHand(usedHand));
    }

}

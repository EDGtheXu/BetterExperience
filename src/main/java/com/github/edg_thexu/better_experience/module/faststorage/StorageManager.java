package com.github.edg_thexu.better_experience.module.faststorage;

import com.github.edg_thexu.better_experience.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import org.confluence.mod.common.init.ModAttachmentTypes;
import org.confluence.mod.common.init.ModTags;
import org.confluence.mod.common.init.block.FunctionalBlocks;

public class StorageManager {

    public static int range = 5;

    /**
     * 物品存放到周围的箱子
     * @param player 玩家
     */
    public static void saveAll(Player player){
        assert player.level() instanceof ServerLevel;
        ServerLevel level = (ServerLevel) player.level();
        BlockPos center = player.blockPosition();
        for(int i = -range; i <= range; i++){
            for(int j = -range; j <= range; j++){
                for(int k = -range; k <= range; k++){
                    BlockPos pos = center.offset(i, j, k);
                    BlockEntity blockEntity = level.getBlockEntity(pos);
                    if(blockEntity instanceof ChestBlockEntity entity){
                        for(ItemStack stack : player.getInventory().items){
                            for(int slot = 0; slot < entity.getContainerSize(); slot++){
                                ItemStack containerStack = entity.getItem(slot);
                                if(ItemStack.isSameItemSameComponents(stack, containerStack)){
                                    int m  = containerStack.getCount() + stack.getCount();
                                    int n = containerStack.getMaxStackSize();
                                    if (m <= n) {
                                        stack.setCount(0);
                                        containerStack.setCount(m);
                                    } else if (containerStack.getCount() < k) {
                                        stack.shrink(n - containerStack.getCount());
                                        containerStack.setCount(n);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 猪猪存钱罐自动存钱
     * @param player 玩家
     */
    public static void saveMoneyToPiggy(Player player){
        if(player.level().isClientSide || (player.tickCount & 63) != 0){
            return;
        }
        if(player.getInventory().hasAnyMatching(it->it.getItem() == FunctionalBlocks.PIGGY_BANK.get().asItem())) {
            var data = player.getData(ModAttachmentTypes.PIGGY_BANK);
            for (var itemstack : player.getInventory().items) {
                if (itemstack.is(ModTags.Items.COINS)) {
                    ModUtils.tryPlaceBackItemStackToItemStacks(itemstack, data.getItems());
                }

            }
        }
    }
}

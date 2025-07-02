package com.github.edg_thexu.better_experience.module.faststorage;

import com.github.edg_thexu.better_experience.intergration.confluence.ConfluenceHelper;
import com.github.edg_thexu.better_experience.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.PlayerEnderChestContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;


import java.util.ArrayList;
import java.util.List;

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
                                if(ItemStack.isSameItemSameTags(stack, containerStack)){
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
        if(!ConfluenceHelper.isLoaded()){
            return;
        }
        if(player.level().isClientSide || (player.tickCount & 63) != 0){
            return;
        }
        // 当背包中有猪猪存钱罐时
//        if(player.getInventory().hasAnyMatching(it->it.getItem() == FunctionalBlocks.PIGGY_BANK.get().asItem())) {
//            // 将钱存入存钱罐
//            var data = player.getData(ModAttachmentTypes.PIGGY_BANK);
//            for (var itemstack : player.getInventory().items) {
//                if (itemstack.is(ModTags.Items.COINS)) {
//                    ModUtils.tryPlaceBackItemStackToItemStacks(itemstack, data.getItems());
//                }
//            }
//            // 如果存钱罐钱可以升级，则升级
//            int c = 0;
//            boolean dirty = false;
//            for(var item : data.getItems()){
//                if(item.is(ModTags.Items.COINS)) {
//                    if (item.getCount() == 100) {
//                        int index = PlayerUtils.COIN_2_INDEX.applyAsInt(item.getItem()) - 1;
//                        if (index >= 0) {
//                            data.getItems().set(c, new ItemStack(PlayerUtils.INDEX_2_COIN.apply(3 - index)));
//                            dirty = true;
//                        }
//
//                    }
//                }
//                c++;
//            }
//            // 合并所有钱
//            if(dirty){
//                List<ItemStack> coins = new ArrayList<>();
//                for(var item : data.getItems()){
//                    if(item.is(ModTags.Items.COINS)) {
//                        coins.add(item);
//                    }
//                }
//                ModUtils.unionItemStacks(coins);
//            }
//        }
    }

    /**
     * 无需防止就可以打开存钱罐
     */
    public static void openPiggy(Level level, Player player, InteractionHand usedHand){
//        ItemStack stack = player.getItemInHand(usedHand);
//        if(usedHand == InteractionHand.MAIN_HAND ){
//            if(ConfluenceHelper.isLoaded()) {
//                if(stack.is(FunctionalBlocks.PIGGY_BANK.asItem())) {
//                    PlayerPiggyBankContainer container = player.getData(ModAttachmentTypes.PIGGY_BANK);
//                    player.openMenu(new SimpleMenuProvider((id, inventory, player1) -> {
//                        return new ChestMenu(MenuType.GENERIC_9x6, id, inventory, container, 6);
//                    }, Component.translatable("container.confluence.piggy_bank")));
//                }
//                else if(stack.is(FunctionalBlocks.SAFE.asItem())){
//                    PlayerSafeContainer container = player.getData(ModAttachmentTypes.SAFE);
//                    player.openMenu(new SimpleMenuProvider((id, inventory, player1) -> {
//                        return new ChestMenu(MenuType.GENERIC_9x6, id, inventory, container, 6);
//                    }, Component.translatable("container.confluence.safe")));
//                }
//            }
//            if(stack.is(Blocks.ENDER_CHEST.asItem())){
//                PlayerEnderChestContainer playerenderchestcontainer = player.getEnderChestInventory();
//
//                player.openMenu(new SimpleMenuProvider((id, inventory, player1) -> {
//                    return ChestMenu.threeRows(id, inventory, playerenderchestcontainer);
//                },  Component.translatable("container.enderchest")));
//            }
//        }
    }
}

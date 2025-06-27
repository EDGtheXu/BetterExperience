package com.github.edg_thexu.better_experience.menu;

import com.github.edg_thexu.better_experience.init.ModMenus;
import com.github.edg_thexu.better_experience.module.autopotion.PlayerInventoryManager;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class PotionBagMenu extends AbstractContainerMenu {
    Container container;

    int containerRows;
    public PotionBagMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new SimpleContainer(18));
    }

    public PotionBagMenu(int containerId, Inventory playerInventory, Container container) {
        super(ModMenus.POTION_BAG_MENU.get(), containerId);

        this.container = container;
        this.containerRows = (int) Math.ceil(container.getContainerSize() / 9.0);
        container.startOpen(playerInventory.player);
        int i = (this.containerRows - 4) * 18;

        int i1;
        int j1;
        for(i1 = 0; i1 < this.containerRows; ++i1) {
            for(j1 = 0; j1 < 9; ++j1) {
                this.addSlot(new Slot(container, j1 + i1 * 9, 8 + j1 * 18, 18 + i1 * 18){
                    @Override
                    public boolean mayPlace(ItemStack stack) {
                        return
//                                stack.get(DataComponents.FOOD) != null ||
//                                stack.get(DataComponents.POTION_CONTENTS) != null ||
                                PlayerInventoryManager.canApply.test(stack);
                    }
                });
            }
        }

        for(i1 = 0; i1 < 3; ++i1) {
            for(j1 = 0; j1 < 9; ++j1) {
                this.addSlot(new Slot(playerInventory, j1 + i1 * 9 + 9, 8 + j1 * 18, 103 + i1 * 18 + i));
            }
        }

        for(i1 = 0; i1 < 9; ++i1) {
            this.addSlot(new Slot(playerInventory, i1, 8 + i1 * 18, 161 + i));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < containerRows * 9) {
                if (!this.moveItemStackTo(itemstack1, containerRows * 9, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, containerRows * 9, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(Player player) {
        return container.stillValid(player);
    }
}

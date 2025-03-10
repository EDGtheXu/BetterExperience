package com.github.edg_thexu.better_experience.menu;

import com.github.edg_thexu.better_experience.init.ModMenus;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.ItemStack;
import org.confluence.mod.common.item.fishing.BaitItem;
import org.confluence.terra_curio.common.item.curio.BaseCurioItem;

public class AutoFishMenu extends ChestMenu {
    public final ContainerData access;


    public AutoFishMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new SimpleContainer(36), new SimpleContainerData(2));

    }

    public AutoFishMenu(int containerId, Inventory playerInventory, Container container, ContainerData pAccess) {
        super(ModMenus.AUTO_FISH_MENU.get(), containerId, playerInventory, container, 3);
        checkContainerDataCount(pAccess, 2);
        this.access = pAccess;
        int startX = 64;
        addSlot(new Slot(container, 27, startX, -16) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.getItem() instanceof FishingRodItem;
            }
        });
        addSlot(new Slot(container, 28, startX + 18, -16) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.getItem() instanceof BaitItem;
            }
        });
        addSlot(new Slot(container, 29, startX + 36, -16) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                // todo: 渔力饰品
                return stack.getItem() instanceof BaseCurioItem;
            }
        });

        addDataSlots(access);
    }

    public ItemStack quickMoveStack(Player player, int index) {

        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            for(int i = 27 + 36; i < slots.size() ; i ++){
                if(index != i) {
                    if (!this.moveItemStackTo(itemstack1, i, i + 1, false)) {
                        continue;
                    }
                }
                else {
                    if (!this.moveItemStackTo(itemstack1, 36, 36 + 27, true)) {
                        break;
                    }
                }
                slots.get(i).setChanged();
                return itemstack1;
            }
        }

        return super.quickMoveStack(player, index);
    }
}
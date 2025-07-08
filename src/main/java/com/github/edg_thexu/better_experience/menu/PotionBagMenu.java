package com.github.edg_thexu.better_experience.menu;

import com.github.edg_thexu.better_experience.data.component.ItemContainerComponent;
import com.github.edg_thexu.better_experience.init.ModDataComponentTypes;
import com.github.edg_thexu.better_experience.init.ModMenus;
import com.github.edg_thexu.better_experience.item.PotionBag;
import com.github.edg_thexu.better_experience.module.autopotion.PlayerInventoryManager;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;

import java.util.Iterator;

public class PotionBagMenu extends AbstractContainerMenu {
    public Container container;
    public ItemContainerComponent component;

    int containerRows;
    public PotionBagMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new ItemContainerComponent(18));
    }

    public PotionBagMenu(int containerId, Inventory playerInventory, ItemContainerComponent component) {
        super(ModMenus.POTION_BAG_MENU.get(), containerId);
        this.component = component;

        ItemContainerContents contents = component.container;
        Iterator<ItemStack> items =  contents.nonEmptyItems().iterator();
        this.container = new SimpleContainer(component.size);
        this.containerRows = (int) Math.ceil(container.getContainerSize() / 9.0);
        container.startOpen(playerInventory.player);
        int i = (this.containerRows - 4) * 18;

        int i1;
        int j1;
        for(i1 = 0; i1 < this.containerRows; ++i1) {
            for(j1 = 0; j1 < 9; ++j1) {
                ItemStack stack = items.hasNext()? items.next() : ItemStack.EMPTY;
                Slot slot = new Slot(container, j1 + i1 * 9, 8 + j1 * 18, 18 + i1 * 18){
                    @Override
                    public boolean mayPlace(ItemStack stack) {
                        return canPlace(stack);
                    }
                };
                this.addSlot(slot);
                slot.set(stack);
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

    public static boolean canPlace(ItemStack stack){
        return !PlayerInventoryManager.getApplyEffect(stack, true).isEmpty();
    }

    public boolean clickMenuButton(Player player, int id) {
        return false;
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

    @Override
    public void removed(Player player) {
        super.removed(player);
        if(!player.level().isClientSide) {
            ItemStack itemStack = player.getMainHandItem();
            if (itemStack.getItem() instanceof PotionBag) {
                NonNullList<ItemStack> items = NonNullList.create();
                for(int i = 0; i < container.getContainerSize(); i++){
                    items.add(container.getItem(i));
                }
                ItemContainerComponent old = itemStack.get(ModDataComponentTypes.ITEM_CONTAINER_COMPONENT);

                boolean isAutoCollect = false;
                if (old != null) {
                    isAutoCollect = old.isAutoCollect();
                }
                ItemContainerComponent component = new ItemContainerComponent(ItemContainerContents.fromItems(items), isAutoCollect, this.component.size);
                itemStack.set(ModDataComponentTypes.ITEM_CONTAINER_COMPONENT, component);
            }
        }

    }
}

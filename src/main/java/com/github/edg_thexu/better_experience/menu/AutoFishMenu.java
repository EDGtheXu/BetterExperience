package com.github.edg_thexu.better_experience.menu;

import com.github.edg_thexu.better_experience.init.ModMenus;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import org.confluence.mod.common.item.fishing.AbstractFishingPole;
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
        addSlot(new Slot(container, 27, 50, -16){
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.getItem() instanceof AbstractFishingPole;
            }
        });
        addSlot(new Slot(container, 28, 70, -16){
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.getItem() instanceof BaitItem;
            }
        });
        addSlot(new Slot(container, 29, 90, -16){
            @Override
            public boolean mayPlace(ItemStack stack) {
                // todo: 渔力饰品
                return stack.getItem() instanceof BaseCurioItem;
            }
        });

        addDataSlots(access);
    }


}

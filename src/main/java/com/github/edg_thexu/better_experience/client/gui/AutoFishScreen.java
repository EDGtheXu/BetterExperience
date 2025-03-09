package com.github.edg_thexu.better_experience.client.gui;

import com.github.edg_thexu.better_experience.menu.AutoFishMenu;
import com.github.edg_thexu.better_experience.module.autofish.AutoFishManager;
import com.github.edg_thexu.better_experience.network.C2S.ServerBoundPacketC2S;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;
import org.confluence.mod.common.item.fishing.BaitItem;

public class AutoFishScreen extends ContainerScreen {

    Button startBt;

    public AutoFishScreen(ChestMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void init() {
        super.init();

        startBt = Button.builder(Component.literal("Start"), p->{
            if(menu instanceof AutoFishMenu menu){
                if(menu.access.get(0) == 0) {
                    ServerBoundPacketC2S.notifyStart();
                }
                else if (menu.access.get(0) != 0) {
                    PacketDistributor.sendToServer(new ServerBoundPacketC2S(2));
                }
            }
        }).pos(leftPos, topPos - 30).size(30,20).build();

        this.addRenderableWidget(startBt);

    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 4210752, false);
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderTransparentBackground(guiGraphics);
        this.renderBg(guiGraphics, partialTick, mouseX, mouseY);
    }


    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        ItemStack pole = menu.getContainer().getItem(27);
        Item bait = menu.getContainer().getItem(28).getItem();
        ItemStack curios = menu.getContainer().getItem(29);

        float power = AutoFishManager.computeFishingPower(null, pole, bait instanceof BaitItem? (BaitItem) bait : null, curios);

        guiGraphics.drawString(font, "Power: " + (int) power, leftPos + 30, topPos + 5 , 0xFFFFFFFF);

        guiGraphics.drawString(font, "Remain: " +( (AutoFishMenu)menu).access.get(1), leftPos + 100, topPos + 5 , 0xFFFFFFFF);

    }

}

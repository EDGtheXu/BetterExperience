package com.github.edg_thexu.better_experience.client.gui;

import com.github.edg_thexu.better_experience.menu.AutoFishMenu;
import com.github.edg_thexu.better_experience.network.C2S.ServerBoundPacketC2S;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ChestMenu;
import net.neoforged.neoforge.network.PacketDistributor;

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
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);



    }

}

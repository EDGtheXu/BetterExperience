package com.github.edg_thexu.better_experience.client.gui.container;

import com.github.edg_thexu.better_experience.Better_experience;
import com.github.edg_thexu.better_experience.client.gui.widget.FloatButton;
import com.github.edg_thexu.better_experience.intergration.confluence.ConfluenceHelper;
import com.github.edg_thexu.better_experience.intergration.terra_curios.TCHelper;
import com.github.edg_thexu.better_experience.menu.AutoFishMenu;
import com.github.edg_thexu.better_experience.module.autofish.AutoFishManager;
import com.github.edg_thexu.better_experience.networks.c2s.ServerBoundPacketC2S;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.gui.screens.inventory.CyclingSlotBackground;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

public class AutoFishScreen extends ContainerScreen {

    FloatButton startBt;

    public AutoFishScreen(ChestMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);

        iconList1 = ConfluenceHelper.isLoaded()? List.of(Better_experience.space("confluence","item/fishing_pole/golden_fishing_rod")):
                List.of(Better_experience.space("minecraft","item/fishing_rod"));
//        minecraft\textures\gui\sprites\spectator\close.png
        iconList2 = ConfluenceHelper.isLoaded()? List.of(Better_experience.space("confluence","item/bait/worm")) : List.of(
                Better_experience.space("minecraft","textures\\gui\\sprites\\spectator\\close")
        );
        iconList3 = TCHelper.isLoaded()? List.of(Better_experience.space("terra_curio","item/curio/angler_earring")) : List.of(
                Better_experience.space("minecraft","textures\\gui\\sprites\\spectator\\close")
        );

    }

    private final CyclingSlotBackground icon1 = new CyclingSlotBackground(27+ 36);
    private final CyclingSlotBackground icon2 = new CyclingSlotBackground(28+ 36);
    private final CyclingSlotBackground icon3 = new CyclingSlotBackground(29+ 36);

    List<ResourceLocation> iconList1;
    List<ResourceLocation> iconList2;
    List<ResourceLocation> iconList3;

    @Override
    protected void init() {
        super.init();

        startBt = (FloatButton) FloatButton.builder(Component.literal("S"), p->{
            if(menu instanceof AutoFishMenu menu){
                if(menu.access.get(0) == 0) {
                    ServerBoundPacketC2S.notifyStart();
                }
                else if (menu.access.get(0) != 0) {
                    PacketDistributor.sendToServer(new ServerBoundPacketC2S(2));
                }
            }
        }).pos(leftPos + 75, topPos + 3).size(30,13).build();


        this.addRenderableWidget(startBt);

    }

    @Override
    protected void containerTick() {
        this.icon1.tick(iconList1);
        this.icon2.tick(iconList2);
        this.icon3.tick(iconList3);

        if(menu instanceof AutoFishMenu menu){
            if (menu.access.get(0) == 0 && startBt.isSelected()) {
                if(System.currentTimeMillis() -  startBt.lastClickTime > 200){
                    startBt.setSelected(false);
                }
            }else if(menu.access.get(0) != 0 && !startBt.isSelected()){
                if(System.currentTimeMillis() -  startBt.lastClickTime > 200) {
                    startBt.setSelected(true);
                }
            }
        }
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
        guiGraphics.blit(Better_experience.space("textures/gui/tri_slots.png"),leftPos+60, topPos-25, 0,0,60,26,60,26);

        guiGraphics.setColor(0.6f,0.6f,0.6f, 0.4f);

        this.icon1.render(this.menu, guiGraphics, partialTick, this.leftPos, this.topPos);
        this.icon2.render(this.menu, guiGraphics, partialTick, this.leftPos, this.topPos);
        this.icon3.render(this.menu, guiGraphics, partialTick, this.leftPos, this.topPos);
        guiGraphics.setColor(1f,1f,1f, 1f);

    }


    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        ItemStack pole = menu.getContainer().getItem(27);
        Item bait = menu.getContainer().getItem(28).getItem();
        ItemStack curios = menu.getContainer().getItem(29);

        float power = AutoFishManager.computeFishingPower(null, pole, bait, curios);

        guiGraphics.drawString(font, "Power: " + (int) power, leftPos + 10, topPos - 10 , 0xFFFFFFFF);

        guiGraphics.drawString(font, "Remain: " +( (AutoFishMenu)menu).access.get(1), leftPos + 125, topPos - 10 , 0xFFFFFFFF);

    }

}

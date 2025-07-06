package com.github.edg_thexu.better_experience.client.gui.container;

import com.github.edg_thexu.better_experience.Better_experience;
import com.github.edg_thexu.better_experience.data.component.ItemContainerComponent;
import com.github.edg_thexu.better_experience.init.ModDataComponentTypes;
import com.github.edg_thexu.better_experience.menu.PotionBagMenu;
import com.github.edg_thexu.better_experience.networks.c2s.ServerBoundPacketC2S;
import com.github.edg_thexu.better_experience.utils.ModUtils;
import com.github.edg_thexu.cafelib.api.datacomponent.IDataComponentType;
import com.github.edg_thexu.cafelib.client.gui.widget.FloatButton;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class PotionBagScreen extends AbstractContainerScreen<PotionBagMenu> {
    private static final ResourceLocation CONTAINER_BACKGROUND = Better_experience.defaultSpacee("textures/gui/container/generic_54.png");
    private final int containerRows;

    FloatButton collectButton;
    public PotionBagScreen(PotionBagMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);

        this.containerRows = 2;
        this.imageHeight = 114 + this.containerRows * 18;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void init() {
        super.init();
        collectButton = (FloatButton) FloatButton.builder(Component.literal("A"), p->{
//            PacketDistributor.sendToServer(new ServerBoundPacketC2S(5));
            ModUtils.sendToServer(new ServerBoundPacketC2S(5));
                }).tooltip(Tooltip.create(Component.translatable("better_experience.gui.potion_screen.auto_collect.message")
                        .withStyle(Style.EMPTY.withColor(0xFFFFFF))
        )).bounds(this.leftPos - 32, this.topPos, 30, 15).build();
        this.addRenderableWidget(collectButton);
        ItemStack stack = minecraft.player.getMainHandItem();

//        var data = stack.get(ModDataComponentTypes.ITEM_CONTAINER_COMPONENT);
        ItemContainerComponent data = IDataComponentType.getData(stack, ModDataComponentTypes.ITEM_CONTAINER_COMPONENT.get());
        if(data != null){
            collectButton.setSelected(data.isAutoCollect());
        }
    }

    @Override
    protected void containerTick() {

    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 4210752, false);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        this.renderBackground(guiGraphics);
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(CONTAINER_BACKGROUND, i, j, 0, 0, this.imageWidth, this.containerRows * 18 + 17);
        guiGraphics.blit(CONTAINER_BACKGROUND, i, j + this.containerRows * 18 + 17, 0, 126, this.imageWidth, 96);
    }


    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);


    }

    @Override
    public void onClose() {
        // 同步物品数据
        ModUtils.sendToServer(new ServerBoundPacketC2S(15));
        super.onClose();
    }

}

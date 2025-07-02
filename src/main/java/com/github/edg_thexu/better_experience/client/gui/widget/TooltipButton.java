package com.github.edg_thexu.better_experience.client.gui.widget;

import com.github.edg_thexu.better_experience.client.RenderUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TooltipButton extends Button {
    List<ClientTooltipComponent> tooltips;

    protected TooltipButton(int x, int y, int width, int height, Component message, OnPress onPress, CreateNarration createNarration) {
        super(x, y, width, height, message, onPress, createNarration);
    }

    public TooltipButton(Builder builder) {
        super(builder);
    }

    public static Builder builder(Component message, OnPress onPress) {
        return new Builder(message, onPress);
    }

    public void setTooltips(List<ClientTooltipComponent> tooltips) {
        this.tooltips = tooltips;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
//        super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
        if(this.isHovered() && this.tooltips!= null && !this.tooltips.isEmpty()){
            RenderUtil.customTooltip(guiGraphics, mouseX, mouseY, this.tooltips);
        }
    }

    public static class Builder extends Button.Builder {

        protected List<ClientTooltipComponent> tooltips;

        public Builder(Component message, OnPress onPress) {
            super(message, onPress);
        }

        public Builder setTooltips(List<ClientTooltipComponent> tooltips) {
            this.tooltips = tooltips;
            return this;
        }

        @Override
        public @NotNull TooltipButton build() {
            TooltipButton button = new TooltipButton(this);
            button.setTooltips(tooltips);
            return button;
        }
    }
}

package com.github.edg_thexu.better_experience.client.gui.widget;

import com.github.edg_thexu.better_experience.intergration.terra_entity.TEHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.client.ModRenderTypes;
import org.confluence.terraentity.client.util.ShaderUtil;
import org.confluence.terraentity.mixed.IShaderInstance;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FloatButton extends TooltipButton {

    private boolean selected;
    public long lastClickTime;
    private int duration = 300;

    protected FloatButton(int x, int y, int width, int height, Component message, OnPress onPress, CreateNarration createNarration) {
        super(x, y, width, height, message, onPress, createNarration);
    }

    public FloatButton(Builder builder) {
        super(builder);
    }

    public static Builder builder(Component message, OnPress onPress) {
        return new Builder( message, onPress);
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        Minecraft minecraft = Minecraft.getInstance();
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        guiGraphics.blitSprite(SPRITES.get(this.active, false), this.getX(), this.getY(), this.getWidth(), this.getHeight());


        long elapsedTime = System.currentTimeMillis() - this.lastClickTime;
        float progress = Mth.clamp(elapsedTime / (float) this.duration, 0.0F, 1.0F);

        if(!this.selected){
            progress = 1 - progress;
        }
//        progress = progress < 0.5 ? 4 * progress * progress * progress : (float) (1 - Math.pow(-2 * progress + 2, 2) / 2);
//        progress = (float) Math.pow(progress, 0.5f);
        progress = 1 - (1 - progress) * (1 - progress);

        int width = (int) (this.getWidth() * 0.5f * progress);
        int i = this.getFGColor();
        // 绿色背景颜色
        guiGraphics.setColor(0F, 1F, 0F, 1.0F);
        int w = (int) (width + this.getWidth() * 0.25f);
        guiGraphics.blitSprite(SPRITES.get(this.active, false), this.getX(), this.getY(), w, this.getHeight());
        AbstractWidget.renderScrollingString(guiGraphics, minecraft.font, Component.literal("ON"),
                (int) (this.getX() + this.width * 0.25f + 2),
                this.getX() + 2, this.getY(),
                (int) (this.getX() + this.width * 0.5F), this.getY() + this.height,
                i | Mth.ceil(this.alpha * 255.0F) << 24);

        // 红色背景颜色
        guiGraphics.setColor(1F, 0F, 0F, 1.0F);
        guiGraphics.blitSprite(SPRITES.get(this.active, false), this.getX() + w, this.getY(), this.width - w, this.getHeight());


        // 滑块
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableDepthTest();
        guiGraphics.setColor(1 - progress * 0.2f, 1.0F, 1 - progress * 0.2F, 1.0F);


        if(TEHelper.isLoaded()) {
            // 流动速度
            float speed = 0.01f;
            ((IShaderInstance) ModRenderTypes.Shaders.floatBarShader).getTerra_entity$Time().set(System.currentTimeMillis() % 100000 * speed);
            // 噪声强度
            ((IShaderInstance) ModRenderTypes.Shaders.floatBarShader).getTerra_entity$Radius().set(progress * 0.5F);

//            RenderSystem.setShaderTexture(0, ResourceLocation.withDefaultNamespace("textures/gui/sprites/widget/button.png"));
            RenderSystem.setShaderTexture(0, ResourceLocation.withDefaultNamespace("textures/gui/sprites/container/beacon/button.png"));

            RenderSystem.setShaderTexture(1, TerraEntity.space("textures/gui/noise.png"));
            RenderSystem.setShader(() -> ModRenderTypes.Shaders.floatBarShader);
            float ww = this.getWidth() * 0.45f;
            ShaderUtil.shaderBlit(guiGraphics.pose().last().pose(),
                    this.getX() + 1 + width, this.getY() + 1,
                    0, 0F,
                    (int) ww, this.getHeight() - 2,
                    (int)ww, 22
            );
            // 滑块上的文本
            guiGraphics.setColor(1 - progress, 1.0F, 1 - progress, 1.0F);
            AbstractWidget.renderScrollingString(guiGraphics, minecraft.font, this.getMessage(),
                    (int) (width + this.getX() + this.width * 0.25f ),
                    width + 2 + this.getX(), this.getY(),
                    (int) (width + this.getX() + this.width * 0.5F) - 2, this.getY() + this.height,
                    i | Mth.ceil(this.alpha * 255.0F) << 24);

            guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);

        }else{
            guiGraphics.blitSprite(SPRITES.get(this.active, false), this.getX() + 1 + width, this.getY() + 1, (int) (this.getWidth() * 0.45f), this.getHeight() - 2);

        }

//        this.renderScrollingString(guiGraphics, minecraft.font, width, i | Mth.ceil(this.alpha * 255.0F) << 24);
//        this.renderString(guiGraphics, minecraft.font, i | Mth.ceil(this.alpha * 255.0F) << 24);

    }


    @Override
    protected boolean clicked(double mouseX, double mouseY) {
        return super.clicked(mouseX, mouseY) && System.currentTimeMillis() - this.lastClickTime > 300;
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        this.lastClickTime = System.currentTimeMillis();
        this.selected = !this.selected;
        super.onClick(mouseX, mouseY);
    }

    public boolean isSelected() {
        return selected;
    }
    public void setSelected(boolean selected) {
        this.lastClickTime = System.currentTimeMillis();
        this.selected = selected;
    }

    public void setSelectedDirectly(boolean selected) {
        this.selected = selected;
    }

    public static class Builder extends TooltipButton.Builder {

        private List<ClientTooltipComponent> tooltips;
        private int duration = 300;

        public Builder(Component message, OnPress onPress) {
            super(message, onPress);
        }

        public Builder setDuration(int duration) {
            this.duration = duration;
            return this;
        }

        @Override
        public @NotNull FloatButton build() {
            FloatButton button = new FloatButton(this);
            button.setTooltips(tooltips);
            button.duration = duration;
            return button;
        }
    }

}

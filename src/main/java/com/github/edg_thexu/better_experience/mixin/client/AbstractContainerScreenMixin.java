package com.github.edg_thexu.better_experience.mixin.client;

import com.github.edg_thexu.better_experience.client.gui.hud.PotionScreenManager;
import com.github.edg_thexu.better_experience.module.autopotion.PlayerInventoryManager;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin<T extends AbstractContainerMenu> {

    @Shadow @Final protected T menu;

    @Shadow protected abstract void init();

    @Unique
    private PotionScreenManager betterExperience$potionScreenManager;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(AbstractContainerMenu menu, Inventory playerInventory, Component title, CallbackInfo ci) {
        AbstractContainerScreen screen = (AbstractContainerScreen) (Object) this;
        if(PotionScreenManager.shouldShowPotionManager(screen)){
            betterExperience$potionScreenManager = new PotionScreenManager(screen);
        }

    }

    @Inject(method = "init", at = @At("RETURN"))
    private void initMixin(CallbackInfo ci) {
        AbstractContainerScreen screen = (AbstractContainerScreen) (Object) this;
        if(PotionScreenManager.shouldShowPotionManager(screen)){
            betterExperience$potionScreenManager.init();
        }

    }
    @Inject(method = "render", at = @At("RETURN"))
    private void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick, CallbackInfo ci) {
        if(betterExperience$potionScreenManager!= null){
            betterExperience$potionScreenManager.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        }
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"))
    private void mouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        if(betterExperience$potionScreenManager != null){
            betterExperience$potionScreenManager.click(mouseX, mouseY, button);
        }
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen;renderSlot(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/world/inventory/Slot;)V"))
    private void renderMixin(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci, @Local Slot slot) {
        Container container = null;
        if(menu instanceof ChestMenu menu1){
            container = menu1.getContainer();
        }
        int x = slot.x;
        int y = slot.y;
        PlayerInventoryManager.renderApply((AbstractContainerScreen<?>) (Object) this, container, slot.getItem() , guiGraphics, x, y, partialTick);

    }

}

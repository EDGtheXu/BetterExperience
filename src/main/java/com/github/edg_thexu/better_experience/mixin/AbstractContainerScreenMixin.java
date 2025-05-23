package com.github.edg_thexu.better_experience.mixin;

import com.github.edg_thexu.better_experience.module.autopotion.PlayerInventoryManager;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerScreen.class)
public class AbstractContainerScreenMixin<T extends AbstractContainerMenu> {

    @Shadow @Final protected T menu;

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

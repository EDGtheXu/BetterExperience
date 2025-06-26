package com.github.edg_thexu.better_experience.mixin.client;

import com.github.edg_thexu.better_experience.client.gui.hud.PotionScreenManager;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.confluence.mod.client.gui.container.ExtraInventoryScreen;
import org.confluence.mod.common.menu.ExtraInventoryMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
@Pseudo
@Mixin(targets = "org.confluence.mod.client.gui.container.ExtraInventoryScreen")
//@Mixin(ExtraInventoryScreen.class)
public class ExtraInventoryScreenMixin {

//    @Unique
//    private PotionScreenManager betterExperience$potionScreenManager;
//
//    @Inject(method = "<init>", at = @At("RETURN"))
//    private void init(ExtraInventoryMenu menu, Inventory playerInventory, Component title, CallbackInfo ci) {
//        betterExperience$potionScreenManager = new PotionScreenManager((ExtraInventoryScreen) (Object) this);
//    }
//
//    @Inject(method = "render", at = @At("RETURN"))
//    private void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick, CallbackInfo ci) {
//        betterExperience$potionScreenManager.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
//    }
//
//    @Inject(method = "mouseClicked", at = @At("HEAD"))
//    private void mouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
//        betterExperience$potionScreenManager.click(mouseX, mouseY, button);
//    }

}

package com.github.edg_thexu.better_experience.mixin.client;

import com.github.edg_thexu.better_experience.client.gui.hud.PotionScreenManager;
import com.github.edg_thexu.better_experience.intergration.confluence.ConfluenceHelper;
import com.github.edg_thexu.better_experience.mixed.SelfGetter;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//@Mixin(Screen.class)
//public abstract class ScreenMixin  implements SelfGetter<Screen> {
//
//    @Shadow protected abstract <T extends GuiEventListener & Renderable & NarratableEntry> T addRenderableWidget(T widget);
//
//    @Inject(method = "init(Lnet/minecraft/client/Minecraft;II)V", at = @At(value = "RETURN"))
//    private void initMixin(CallbackInfo ci) {
//        if(ConfluenceHelper.isLoaded() && this.te$getSelf() instanceof ExtraInventoryScreen){
//            addRenderableWidget(PotionScreenManager.getInstance().fastStorageBtn);
//        }
//    }
//}

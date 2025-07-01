package com.github.edg_thexu.better_experience.mixin.integration.jei;

import com.github.edg_thexu.better_experience.intergration.jei.IRecipeGui;
import com.github.edg_thexu.better_experience.intergration.jei.IRecipeGuiLayouts;
import mezz.jei.common.util.ImmutableRect2i;
import mezz.jei.gui.recipes.RecipeGuiLayouts;
import mezz.jei.gui.recipes.RecipesGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//@Mixin(RecipesGui.class)
@Pseudo
@Mixin(targets = "mezz.jei.gui.recipes.RecipesGui")
public class RecipeGuiMixin implements IRecipeGui {

    @Shadow @Final private RecipeGuiLayouts layouts;
    @Shadow private ImmutableRect2i idealArea;
    @Unique EditBox betterExperience$countBox;

    @Inject(method = "mouseClicked", at = @At("HEAD"))
    public void mouseClicked(double mouseX, double mouseY, int mouseButton, CallbackInfoReturnable<Boolean> cir) {
        ((IRecipeGuiLayouts) this.layouts).betterExperience$click(mouseX, mouseY, mouseButton);

        if(betterExperience$countBox != null && betterExperience$countBox.isMouseOver(mouseX, mouseY)){
            ((Screen)(Object)this).setFocused(betterExperience$countBox);
            if(mouseButton == 1){
                betterExperience$countBox.setValue("");
            }
        }

    }

    @Inject(method = "init", at = @At("RETURN"))
    public void init(CallbackInfo ci) {
        ((Screen)(Object)this).renderables.add(betterExperience$countBox);

        ((Screen)(Object)this).setFocused(betterExperience$countBox);

        int left = this.idealArea.getX();
        int top = this.idealArea.getY();
        int width = this.idealArea.getWidth();
        int height = this.idealArea.getHeight();
        betterExperience$countBox = new EditBox(Minecraft.getInstance().font, left + width - 40,5,40,15, Component.literal("1"));
        betterExperience$countBox.setValue("1");
        betterExperience$countBox.setFilter(s -> s.matches("\\d*"));
        betterExperience$countBox.setHint(Component.literal("Fetch Count"));
        ((IRecipeGuiLayouts) this.layouts).betterExperience$setRecipesGui((RecipesGui)(Object)this);
    }

    @Inject(method = "render", at = @At("HEAD"))
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        if(betterExperience$countBox != null){
            betterExperience$countBox.render(guiGraphics, mouseX, mouseY, partialTicks);
        }
    }

    @Override
    public EditBox getBetterExperience$countBox() {
        return betterExperience$countBox;
    }
}
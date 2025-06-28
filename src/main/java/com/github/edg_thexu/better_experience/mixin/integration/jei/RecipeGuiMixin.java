package com.github.edg_thexu.better_experience.mixin.integration.jei;

import com.github.edg_thexu.better_experience.intergration.jei.IRecipeGuiLayouts;
import mezz.jei.gui.recipes.RecipeGuiLayouts;
import mezz.jei.gui.recipes.RecipesGui;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//@Mixin(RecipesGui.class)
@Pseudo
@Mixin(targets = "mezz.jei.gui.recipes.RecipesGui")
public class RecipeGuiMixin {

    @Shadow @Final private RecipeGuiLayouts layouts;

    @Inject(method = "mouseClicked", at = @At("HEAD"))
    public void mouseClicked(double mouseX, double mouseY, int mouseButton, CallbackInfoReturnable<Boolean> cir) {

        ((IRecipeGuiLayouts)this.layouts).betterExperience$click(mouseX, mouseY, mouseButton);
    }

}
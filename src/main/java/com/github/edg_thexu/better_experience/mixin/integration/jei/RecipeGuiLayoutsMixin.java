package com.github.edg_thexu.better_experience.mixin.integration.jei;

import com.github.edg_thexu.better_experience.intergration.jei.IRecipeGuiLayouts;
import com.github.edg_thexu.better_experience.intergration.jei.IRecipeLayoutWithButtons;
import com.github.edg_thexu.better_experience.intergration.jei.JeiHelper;
import com.llamalad7.mixinextras.sugar.Local;
import mezz.jei.api.gui.IRecipeLayoutDrawable;
import mezz.jei.gui.recipes.RecipeGuiLayouts;
import mezz.jei.gui.recipes.RecipeLayoutWithButtons;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.Rect2i;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;
@Pseudo
@Mixin(targets = "mezz.jei.gui.recipes.RecipeGuiLayouts")
//@Mixin(RecipeGuiLayouts.class)
public class RecipeGuiLayoutsMixin implements IRecipeGuiLayouts {

    @Shadow @Final private List<RecipeLayoutWithButtons<?>> recipeLayoutsWithButtons;


    @Inject(method = "updateRecipeButtonPositions", at = @At(value = "INVOKE", target = "Lmezz/jei/api/gui/IRecipeLayoutDrawable;getRect()Lnet/minecraft/client/renderer/Rect2i;"))
    public void updateRecipeButtonPositions(CallbackInfo ci, @Local RecipeLayoutWithButtons<?> recipeLayoutWithButtons){
        JeiHelper.updatePos(recipeLayoutWithButtons);
//        Rect2i rect = recipeLayoutWithButtons.recipeLayout().getRect();
//        ((IRecipeLayoutWithButtons) (Object) recipeLayoutWithButtons).betterExperience$getButton().setPosition( recipeLayoutWithButtons.recipeLayout().getRect().getX(), rect.getY());
    }

    @Inject(method = "draw", at = @At(value = "INVOKE", target = "Lmezz/jei/gui/recipes/RecipeLayoutWithButtons;bookmarkButton()Lmezz/jei/gui/recipes/RecipeBookmarkButton;"))
    public void draw(GuiGraphics guiGraphics, int mouseX, int mouseY, CallbackInfoReturnable<Optional<IRecipeLayoutDrawable<?>>> cir, @Local RecipeLayoutWithButtons<?> recipeLayoutWithButtons) {
        Button button = ((IRecipeLayoutWithButtons) (Object) recipeLayoutWithButtons).betterExperience$getButton();
        if(button != null){
            button.render(guiGraphics, mouseX, mouseY, 0);
        }
    }

    @Override
    public void betterExperience$click(double mouseX, double mouseY, int button) {
        JeiHelper.notifyFindIntegrations(recipeLayoutsWithButtons, mouseX, mouseY, button);
    }
}

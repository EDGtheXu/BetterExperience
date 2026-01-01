package com.github.edg_thexu.better_experience.mixin.integration.jei;

import com.github.edg_thexu.better_experience.client.gui.widget.TooltipButton;
import com.github.edg_thexu.better_experience.config.CommonConfig;
import com.github.edg_thexu.better_experience.intergration.jei.IRecipeLayoutWithButtons;
import com.github.edg_thexu.better_experience.intergration.jei.JeiHelper;
import mezz.jei.api.gui.IRecipeLayoutDrawable;
import mezz.jei.gui.recipes.RecipeBookmarkButton;
import mezz.jei.gui.recipes.RecipeLayoutWithButtons;
import mezz.jei.gui.recipes.RecipeTransferButton;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "mezz.jei.gui.recipes.RecipeLayoutWithButtons")
//@Mixin(RecipeLayoutWithButtons.class)
public class RecipeLayoutWithButtonsMixin implements IRecipeLayoutWithButtons {
    @Unique
    Button betterExperience$button;
    @Override
    public Button betterExperience$getButton() {
        return betterExperience$button;
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    public void init(IRecipeLayoutDrawable recipeLayout, RecipeTransferButton transferButton, RecipeBookmarkButton bookmarkButton, CallbackInfo ci) {
        Rect2i recipeTransferArea = recipeLayout.getRecipeBookmarkButtonArea();
        if (CommonConfig.QUICK_JEI_FETCH.get()) {
            betterExperience$button = ((TooltipButton.Builder) TooltipButton.builder(Component.literal("+"), p -> {
//            System.out.println("Fetching recipes");
                    }).tooltip(Tooltip.create(Component.translatable("better_experience.tooltip.jei.fetch_ingredients")))
                    .bounds(recipeTransferArea.getX() + recipeLayout.getRect().getWidth(), recipeTransferArea.getY() - 20, recipeTransferArea.getWidth(), recipeTransferArea.getHeight())).build();
        }
    }

    @Inject(method = "updateBounds", at = @At(value = "INVOKE", target = "Lmezz/jei/api/gui/IRecipeLayoutDrawable;getRect()Lnet/minecraft/client/renderer/Rect2i;"))
    public void updateRecipeButtonPositions(CallbackInfo ci){
        JeiHelper.updatePos((RecipeLayoutWithButtons) (Object) this);
//        Rect2i rect = recipeLayoutWithButtons.recipeLayout().getRect();
//        ((IRecipeLayoutWithButtons) (Object) recipeLayoutWithButtons).betterExperience$getButton().setPosition( recipeLayoutWithButtons.recipeLayout().getRect().getX(), rect.getY());
    }

    @Inject(method = "draw", at = @At(value = "INVOKE", target = "Lmezz/jei/gui/recipes/RecipeBookmarkButton;draw(Lnet/minecraft/client/gui/GuiGraphics;IIF)V"))
    public void draw(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        Button button = ((IRecipeLayoutWithButtons) this).betterExperience$getButton();
        if(button != null){
            button.render(guiGraphics, mouseX, mouseY, 0);
        }
    }
}

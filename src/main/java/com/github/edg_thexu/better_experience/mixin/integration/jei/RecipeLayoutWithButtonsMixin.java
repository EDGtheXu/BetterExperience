package com.github.edg_thexu.better_experience.mixin.integration.jei;

import com.github.edg_thexu.better_experience.intergration.jei.IRecipeLayoutWithButtons;
import net.minecraft.client.gui.components.Button;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;

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

//    @Inject(method = "<init>", at = @At("RETURN"))
//    public void init(IRecipeLayoutDrawable recipeLayout, RecipeTransferButton transferButton, RecipeBookmarkButton bookmarkButton, CallbackInfo ci) {
//        Rect2i recipeTransferArea = recipeLayout.getRecipeBookmarkButtonArea();
//        if (CommonConfig.QUICK_JEI_FETCH.get()) {
//            betterExperience$button = ((TooltipButton.Builder) TooltipButton.builder(Component.literal("+"), p -> {
////            System.out.println("Fetching recipes");
//                    }).tooltip(Tooltip.create(Component.translatable("better_experience.tooltip.jei.fetch_ingredients")))
//                    .bounds(recipeTransferArea.getX() + recipeLayout.getRect().getWidth(), recipeTransferArea.getY() - 20, recipeTransferArea.getWidth(), recipeTransferArea.getHeight())).build();
//        }
//    }
}

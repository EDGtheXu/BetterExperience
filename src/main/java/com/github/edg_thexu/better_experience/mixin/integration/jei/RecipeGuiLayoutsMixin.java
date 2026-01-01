package com.github.edg_thexu.better_experience.mixin.integration.jei;

import com.github.edg_thexu.better_experience.intergration.jei.IRecipeGui;
import com.github.edg_thexu.better_experience.intergration.jei.IRecipeGuiLayouts;
import com.github.edg_thexu.better_experience.intergration.jei.JeiHelper;
import mezz.jei.gui.recipes.RecipeLayoutWithButtons;
import mezz.jei.gui.recipes.RecipesGui;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.*;

import java.util.List;

@Pseudo
@Mixin(targets = "mezz.jei.gui.recipes.RecipeGuiLayouts")
//@Mixin(RecipeGuiLayouts.class)
public class RecipeGuiLayoutsMixin implements IRecipeGuiLayouts {

    @Shadow @Final private List<RecipeLayoutWithButtons<?>> recipeLayoutsWithButtons;

    @Unique private RecipesGui betterExperience$recipesGui;

    @Override
    public void betterExperience$click(double mouseX, double mouseY, int button) {
        try {
            if(betterExperience$recipesGui!= null && ((IRecipeGui) betterExperience$recipesGui).getBetterExperience$countBox() != null) {
                String str = ((IRecipeGui) betterExperience$recipesGui).getBetterExperience$countBox().getValue();
                if (!str.isEmpty()) {
                    JeiHelper.notifyFindIntegrations(recipeLayoutsWithButtons, mouseX, mouseY, button, Integer.parseInt(str));
                }
            }
        }catch (NumberFormatException e){
            Minecraft.getInstance().player.sendSystemMessage(Component.literal("Error Fetch Count"));
        }
    }

    @Override
    public void betterExperience$setRecipesGui(RecipesGui recipesGui) {
        this.betterExperience$recipesGui = recipesGui;
    }
}

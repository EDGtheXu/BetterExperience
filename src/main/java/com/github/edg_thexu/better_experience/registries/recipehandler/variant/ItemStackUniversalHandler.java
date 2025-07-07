package com.github.edg_thexu.better_experience.registries.recipehandler.variant;

import com.github.edg_thexu.better_experience.registries.itemmatcher.IItemMatcher;
import com.github.edg_thexu.better_experience.registries.itemmatcher.ItemStackWrapper;
import com.github.edg_thexu.better_experience.registries.recipehandler.IRecipeHandler;
import com.github.edg_thexu.better_experience.registries.recipehandler.RecipeHandlerProvider;
import com.github.edg_thexu.better_experience.registries.recipehandler.RecipeHandlerProviderTypes;
import com.github.edg_thexu.better_experience.registries.recipehandler.visitor.IRecipeHandlerVisitor;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.library.gui.ingredients.RecipeSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * jei使用自己的api时，原版的recipeManager获取不到配方，此时需要转成List&lt;Ingredient&gt;，方便和原版进行一样的方式处理
 */
public record ItemStackUniversalHandler(List<List<ItemStackWrapper>> ingredients, int count) implements IRecipeHandler<List<ItemStackWrapper>> {

    public static Supplier<MapCodec<? extends IRecipeHandler>> CODEC = ()-> {
        try {
            return RecordCodecBuilder.<ItemStackUniversalHandler>mapCodec(instance -> instance.group(
                            ItemStackWrapper.CODEC.listOf().listOf().fieldOf("ingredients").forGetter(ItemStackUniversalHandler::ingredients),
                            Codec.INT.fieldOf("count").forGetter(ItemStackUniversalHandler::count)
                    ).apply(instance, ItemStackUniversalHandler::new));
        }catch (Exception e){
            return null;
        }

    };



    public static ItemStackUniversalHandler create(List<IRecipeSlotDrawable> slotDrawables, IItemMatcher matcher, int count) {
        List<List<ItemStackWrapper>> result = new ArrayList<>();
        for(var it : slotDrawables){
            RecipeSlot slot = (RecipeSlot) it;
            if(slot.getRole() != RecipeIngredientRole.INPUT && slot.getRole() != RecipeIngredientRole.CATALYST) {
                continue;
            }
            List<ITypedIngredient<?>> ingredients = slot.getAllIngredientsList();

            result.add(ingredients.stream().map(ig->new ItemStackWrapper(ig, matcher)).toList());

//            TypedIngredientCodecs.getIngredientCodec(ingredients.get(0).getType(), Internal.getJeiRuntime().getIngredientManager()).encodeStart(Minecraft.getInstance().level.registryAccess().createSerializationContext(JsonOps.INSTANCE), ingredients.get(0)).result().get()
        }
        if(result.isEmpty()){
            return null;
        }
        return new ItemStackUniversalHandler(result,count);
    }

    public static ItemStackUniversalHandler create(List<IRecipeSlotDrawable> slotDrawables, int count) {
        return create(slotDrawables, null, count);
    }


    @Override
    public List<List<ItemStackWrapper>> getIngredient(Level level) {

//        List<ITypedIngredient<?>> ingredients = slot.getAllIngredientsList();
//        List<Ingredient> res = new ArrayList<>();
//        for(var ings : ingredients){
//            List<ItemStack> itemStacks = new ArrayList<>();
//            for(var ing : ings){
//                ing.getItemStack().ifPresent(itemStacks::add);
//            }
//            res.add(Ingredient.of(itemStacks.stream()));
//        }

        return ingredients;
    }

    @Override
    public IRecipeHandlerVisitor<List<ItemStackWrapper>> getVisitor() {
        return IRecipeHandlerVisitor.jeiIngredientVisitor;
    }

    @Override
    public boolean match(ItemStack stack, int slot) {
        return false;
    }

    @Override
    public RecipeHandlerProvider getCodec() {
        return RecipeHandlerProviderTypes.BREW_TYPE.get();
    }
}

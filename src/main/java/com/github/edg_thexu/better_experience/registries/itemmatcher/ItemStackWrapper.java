package com.github.edg_thexu.better_experience.registries.itemmatcher;

import com.github.edg_thexu.better_experience.registries.itemmatcher.variant.IdCountMatcher;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.common.Internal;
import mezz.jei.common.codecs.TypedIngredientCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class ItemStackWrapper {

    ITypedIngredient<?> ingredient;
    IItemMatcher matcher;

    public static Codec<ItemStackWrapper> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            TypedIngredientCodecs.getIngredientCodec(Internal.getJeiRuntime().getIngredientManager()).codec().fieldOf("ingredients").forGetter(ItemStackWrapper::ingredients),
            IItemMatcher.TYPE_CODEC.optionalFieldOf("matcher").forGetter(i->Optional.ofNullable(i.matcher))
    ).apply(instance, (ingredients, matcher)-> new ItemStackWrapper(ingredients, matcher.orElse(IdCountMatcher.INSTANCE))));

    public ItemStackWrapper(ITypedIngredient<?> ingredient, IItemMatcher matcher){
        this.ingredient = ingredient;
        this.matcher = matcher;
    }

    public ItemStackWrapper(ITypedIngredient<?> ingredient){
        this(ingredient, IdCountMatcher.INSTANCE);
    }

    public ITypedIngredient<?> ingredients() {
        return ingredient;
    }

    private IItemMatcher getMatcher() {
        return matcher;
    }
    public ItemStack normalize(){
        return matcher.normalize(this);
    }
//
//    public ItemStack toStack(){
//        return new ItemStack(item, count, tag);
//    }

    public boolean test(ItemStack stack){
        return matcher.matches(this, stack);
    }

    public Item getItem() {
        if(ingredient.getIngredient() instanceof ItemStack stack){
            return stack.getItem();
        }
        return null;
    }

    public int getCount() {
        if(ingredient.getIngredient() instanceof ItemStack stack){
            return stack.getCount();
        }
        return 1;
    }

//    public static List<ItemStackWrapper> fromList(List<ItemStack> stacks, IItemMatcher matcher, boolean tag){
//        return stacks.stream().map(stack -> {
//            if(tag){
//                return new ItemStackWrapper(stack, matcher);
//            }
//           return new ItemStackWrapper(stack.getItem(), stack.getCount(), null, matcher);
//        }).toList();
//    }
//
//    public static ItemStackWrapper ofIdCount(ItemStack stack){
//        return new ItemStackWrapper(stack.getItem(), stack.getCount(), null, IdCountMatcher.INSTANCE);
//    }
//
//    public static ItemStackWrapper ofFull(ItemStack stack, IItemMatcher matcher){
//        return new ItemStackWrapper(stack, matcher);
//    }


}

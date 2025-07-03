package com.github.edg_thexu.better_experience.registries.itemmatcher;

import com.github.edg_thexu.better_experience.registries.itemmatcher.variant.IdCountMatcher;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class ItemStackWrapper {

    public Item item;
    public int count;
    public @Nullable CompoundTag tag;
    IItemMatcher matcher;

    public static Codec<ItemStackWrapper> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ForgeRegistries.ITEMS.getCodec().fieldOf("item").forGetter(ItemStackWrapper::getItem),
            Codec.INT.fieldOf("count").forGetter(ItemStackWrapper::getCount),
            CompoundTag.CODEC.optionalFieldOf("tag").forGetter(ItemStackWrapper::getTag),
            IItemMatcher.TYPE_CODEC.fieldOf("matcher").forGetter(ItemStackWrapper::getMatcher)
    ).apply(instance, (item, count, tag, matcher)-> new ItemStackWrapper(item, count, tag.orElse(null), matcher)));

    private IItemMatcher getMatcher() {
        return matcher;
    }

    private Optional<CompoundTag> getTag() {
        return Optional.ofNullable(tag);
    }

    private Integer getCount() {
        return count;
    }

    private Item getItem() {
        return item;
    }
    public ItemStackWrapper(Item item, int count, @Nullable CompoundTag tag, IItemMatcher matcher){
        this.item = item;
        this.count = count;
        this.tag = tag;
        this.matcher = matcher;
    }
    public ItemStackWrapper(ItemStack stack, IItemMatcher matcher){
        this.item = stack.getItem();
        this.count = stack.getCount();
        this.tag = stack.getTag();
        this.matcher = matcher;
    }

    public ItemStack normalize(){
        return matcher.normalize(this);
    }

    public ItemStack toStack(){
        return new ItemStack(item, count, tag);
    }

    public boolean test(ItemStack stack){
        return matcher.matches(this, stack);
    }

    public static List<ItemStackWrapper> fromList(List<ItemStack> stacks, IItemMatcher matcher, boolean tag){
        return stacks.stream().map(stack -> {
            if(tag){
                return new ItemStackWrapper(stack, matcher);
            }
           return new ItemStackWrapper(stack.getItem(), stack.getCount(), null, matcher);
        }).toList();
    }

    public static ItemStackWrapper ofIdCount(ItemStack stack){
        return new ItemStackWrapper(stack.getItem(), stack.getCount(), null, IdCountMatcher.INSTANCE);
    }

    public static ItemStackWrapper ofFull(ItemStack stack, IItemMatcher matcher){
        return new ItemStackWrapper(stack, matcher);
    }


}

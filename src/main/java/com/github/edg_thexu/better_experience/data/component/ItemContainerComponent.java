package com.github.edg_thexu.better_experience.data.component;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class ItemContainerComponent implements DataComponentType<ItemContainerComponent>{

    public static Codec<ItemContainerComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ItemContainerContents.CODEC.fieldOf("container").forGetter(ins->ins.container),
            Codec.BOOL.optionalFieldOf("autoCollect").forGetter(ins->Optional.of(ins.autoCollect)),
            Codec.INT.fieldOf("size").forGetter(ins->ins.size)
    ).apply(instance, (container, autoCollect,size)->{
        boolean autoCollectValue = autoCollect.orElse(true);
        return new ItemContainerComponent(container, autoCollectValue,size);
    }));

    public static StreamCodec<ByteBuf, ItemContainerComponent> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);

    private boolean autoCollect;
    public ItemContainerContents container;

    public int size;

    public ItemContainerComponent(int size) {
        this(ItemContainerContents.fromItems(List.of()),true,size);
    }

    public ItemContainerComponent(ItemContainerContents container, boolean autoCollect, int size) {
        this.autoCollect = autoCollect;
        this.container = container;
        this.size = size;
    }

    @Override
    public @Nullable Codec<ItemContainerComponent> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, ItemContainerComponent> streamCodec() {
        return STREAM_CODEC;
    }

    @Override
    public boolean equals(Object obj) {
        return false;
    }

    @Override
    public int hashCode() {
        return container.hashCode();
    }

    public boolean isAutoCollect() {
        return autoCollect;
    }

    public void setAutoCollect(boolean autoCollect) {
        this.autoCollect = autoCollect;
    }

    public List<ItemStack> getItems() {
        NonNullList<ItemStack> list = NonNullList.withSize(size, ItemStack.EMPTY);
        List<ItemStack> items = container.stream().toList();
        for (int i = 0; i < items.size(); i++) {
            list.set(i, items.get(i));
        }
        return list;
    }
}

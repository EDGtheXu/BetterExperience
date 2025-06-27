package com.github.edg_thexu.better_experience.data.component;

import com.github.edg_thexu.better_experience.data.codec.CodecUtil;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.SimpleContainer;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.Nullable;

public class ItemContainerComponent extends SimpleContainer implements DataComponentType<ItemContainerComponent>{

    public static Codec<ItemContainerComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("size").forGetter(ItemContainerComponent::getContainerSize),
            CodecUtil.TAG_CODEC.fieldOf("tag").forGetter(ins->{
                if(ServerLifecycleHooks.getCurrentServer() == null){
                    return ins.createTag(null);
                }
                return ins.createTag(ServerLifecycleHooks.getCurrentServer().registryAccess());
            })
    ).apply(instance, (size, tag)->{
        ItemContainerComponent itemContainerComponent = new ItemContainerComponent(size);
        if(ServerLifecycleHooks.getCurrentServer() == null){
            itemContainerComponent.fromTag((ListTag) tag, null);
        }else{
            itemContainerComponent.fromTag((ListTag) tag, ServerLifecycleHooks.getCurrentServer().registryAccess());
        }
        return itemContainerComponent;
    }));

    public static StreamCodec<ByteBuf, ItemContainerComponent> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);

    public ItemContainerComponent(int size) {
        super(size);
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
        if(ServerLifecycleHooks.getCurrentServer() == null) {
            return this.createTag(null).hashCode();
        }
        return this.createTag(ServerLifecycleHooks.getCurrentServer().registryAccess()).hashCode();
    }
}

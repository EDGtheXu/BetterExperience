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

import java.util.Optional;
import java.util.UUID;

public class ItemContainerComponent extends SimpleContainer implements DataComponentType<ItemContainerComponent>{

    public static Codec<ItemContainerComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("size").forGetter(ItemContainerComponent::getContainerSize),
            Codec.STRING.optionalFieldOf("uuid").forGetter(ins-> Optional.ofNullable(ins.id.toString())),
            CodecUtil.TAG_CODEC.fieldOf("tag").forGetter(ins->{
                if(ServerLifecycleHooks.getCurrentServer() == null){
                    return ins.createTag(null);
                }
                return ins.createTag(ServerLifecycleHooks.getCurrentServer().registryAccess());
            })
    ).apply(instance, (size, id, tag)->{
        ItemContainerComponent itemContainerComponent = id.map(s -> new ItemContainerComponent(size, UUID.fromString(s))).orElseGet(() -> new ItemContainerComponent(size));
        if(ServerLifecycleHooks.getCurrentServer() == null){
            itemContainerComponent.fromTag((ListTag) tag, null);
        }else{
            itemContainerComponent.fromTag((ListTag) tag, ServerLifecycleHooks.getCurrentServer().registryAccess());
        }
        return itemContainerComponent;
    }));

    public static StreamCodec<ByteBuf, ItemContainerComponent> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);

    UUID id; // 只是为了hashCode
    public ItemContainerComponent(int size) {
        this(size, UUID.randomUUID());
    }
    public ItemContainerComponent(int size, UUID id) {
        super(size);
        this.id = id;
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
        return id.hashCode();
    }

}

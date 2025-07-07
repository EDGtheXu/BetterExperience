package com.github.edg_thexu.better_experience.data.component;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.nbt.CompoundTag;
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
            Codec.BOOL.optionalFieldOf("autoCollect").forGetter(ins->Optional.of(ins.autoCollect)),
            Codec.STRING.optionalFieldOf("uuid").forGetter(ins-> Optional.ofNullable(ins.id.toString())),
            CompoundTag.CODEC.fieldOf("tag").forGetter(ins->{
                CompoundTag tag = new CompoundTag();
                if(ServerLifecycleHooks.getCurrentServer() != null){
                    tag.put("Items", ins.createTag(ServerLifecycleHooks.getCurrentServer().registryAccess()));
                    return tag;
                }else{
                    tag.put("Items", null);
                }
                return tag;
            })
    ).apply(instance, (size, autoCollect, id, tag)->{
        boolean collect = autoCollect.orElse(true);
        ItemContainerComponent itemContainerComponent = id.map(s -> new ItemContainerComponent(size, collect, UUID.fromString(s))).orElseGet(() -> new ItemContainerComponent(size));
        if(ServerLifecycleHooks.getCurrentServer() == null){
            itemContainerComponent.fromTag(tag.getList("Items", 10), null);
        }else{
            itemContainerComponent.fromTag(tag.getList("Items", 10), ServerLifecycleHooks.getCurrentServer().registryAccess());
        }
        return itemContainerComponent;
    }));

    public static StreamCodec<ByteBuf, ItemContainerComponent> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);

    UUID id; // 只是为了hashCode
    private boolean autoCollect;

    public ItemContainerComponent(int size) {
        this(size,true,  UUID.randomUUID());
    }
    public ItemContainerComponent(int size, boolean autoCollect, UUID id) {
        super(size);
        this.id = id;
        this.autoCollect = autoCollect;
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

    public boolean isAutoCollect() {
        return autoCollect;
    }

    public void setAutoCollect(boolean autoCollect) {
        this.autoCollect = autoCollect;
    }

}

package com.github.edg_thexu.better_experience.data.component;

import com.github.edg_thexu.better_experience.data.codec.CodecUtil;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.nbt.ListTag;
import net.minecraft.world.SimpleContainer;

import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.Optional;
import java.util.UUID;

public class ItemContainerComponent extends SimpleContainer {

    public static Codec<ItemContainerComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("size").forGetter(ItemContainerComponent::getContainerSize),
            Codec.BOOL.optionalFieldOf("autoCollect").forGetter(ins->Optional.of(ins.autoCollect)),
            Codec.STRING.optionalFieldOf("uuid").forGetter(ins-> Optional.ofNullable(ins.id.toString())),
            CodecUtil.TAG_CODEC.fieldOf("tag").forGetter(ins->{
                if(ServerLifecycleHooks.getCurrentServer() == null){
                    return ins.createTag();
                }
                return ins.createTag();
            })
    ).apply(instance, (size, autoCollect, id, tag)->{
        boolean collect = autoCollect.orElse(true);
        ItemContainerComponent itemContainerComponent = id.map(s -> new ItemContainerComponent(size, collect, UUID.fromString(s))).orElseGet(() -> new ItemContainerComponent(size));
        if(ServerLifecycleHooks.getCurrentServer() == null){
            itemContainerComponent.fromTag((ListTag) tag);
        }else{
            itemContainerComponent.fromTag((ListTag) tag);
        }
        return itemContainerComponent;
    }));

//    public static StreamCodec<ByteBuf, ItemContainerComponent> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);

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

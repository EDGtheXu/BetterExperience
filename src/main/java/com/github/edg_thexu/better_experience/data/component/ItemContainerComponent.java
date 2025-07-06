package com.github.edg_thexu.better_experience.data.component;

import com.github.edg_thexu.better_experience.init.ModDataComponentTypes;
import com.github.edg_thexu.cafelib.api.datacomponent.IDataComponentType;
import com.github.edg_thexu.cafelib.data.codec.DataComponentProvider;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.SimpleContainer;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

public class ItemContainerComponent extends SimpleContainer implements IDataComponentType<ItemContainerComponent> {

    public static Supplier<Codec<ItemContainerComponent>> CODEC =  ()->RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("size").forGetter(ItemContainerComponent::getContainerSize),
            Codec.BOOL.optionalFieldOf("autoCollect").forGetter(ins->Optional.of(ins.autoCollect)),
            Codec.STRING.optionalFieldOf("uuid").forGetter(ins-> Optional.ofNullable(ins.id.toString())),
            CompoundTag.CODEC.fieldOf("tag").forGetter(ins-> {
                CompoundTag tag = new CompoundTag();
                tag.put("Items", ins.createTag());
                return tag;
            })
    ).apply(instance, (size, autoCollect, id, tag)->{
        boolean collect = autoCollect.orElse(true);
        ItemContainerComponent itemContainerComponent = id.map(s -> new ItemContainerComponent(size, collect, UUID.fromString(s))).orElseGet(() -> new ItemContainerComponent(size));
        itemContainerComponent.fromTag(tag.getList("Items", 10));
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

    @Override
    public DataComponentProvider<ItemContainerComponent> provider() {
        return ModDataComponentTypes.ITEM_CONTAINER_COMPONENT.get();
    }
}

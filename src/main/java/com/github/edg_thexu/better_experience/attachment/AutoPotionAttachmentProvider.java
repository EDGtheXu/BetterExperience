package com.github.edg_thexu.better_experience.attachment;

import com.github.edg_thexu.better_experience.init.ModAttachments;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AutoPotionAttachmentProvider implements ICapabilitySerializable<CompoundTag> {


    private AutoPotionAttachment playerAbility;
    private final LazyOptional<AutoPotionAttachment> abilityLazyOptional = LazyOptional.of(this::getOrCreateStorage);

    private AutoPotionAttachment getOrCreateStorage() {
        if (playerAbility == null) {
            this.playerAbility = new AutoPotionAttachment();
        }
        return playerAbility;
    }

    public void setAttachment(AutoPotionAttachment attachment) {
        this.playerAbility = attachment;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction direction) {
        return ModAttachments.AUTO_POTION.orEmpty(capability, abilityLazyOptional);
    }

    @Override
    public CompoundTag serializeNBT() {
        return getOrCreateStorage().serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        getOrCreateStorage().deserializeNBT(nbt);
    }
}

package com.github.edg_thexu.better_experience.attachment;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.UnknownNullability;

public class TempAttachment implements INBTSerializable<CompoundTag> {

    boolean betterReforge = true; // 更好重铸状态
    public boolean enableBetterReforge(){
        return betterReforge;
    }

    public void setBetterReforge(boolean betterReforge){
        this.betterReforge = betterReforge;
    }


    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        return new CompoundTag();
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {

    }
}

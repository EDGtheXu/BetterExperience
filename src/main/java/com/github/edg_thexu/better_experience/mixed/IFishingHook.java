package com.github.edg_thexu.better_experience.mixed;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public interface IFishingHook {

    Vec3 betterExperience$getPos();

    void betterExperience$setPos(Vec3 pos);

    boolean betterExperience$isSimulation();

    void betterExperience$setSimulation(boolean simulation);

    List<ItemStack> betterExperience$getItems();

    void betterExperience$setItems(List<ItemStack> items);

    boolean betterExperience$isAutoCatch();

    void betterExperience$setAutoCatch(boolean autoCatch);
}

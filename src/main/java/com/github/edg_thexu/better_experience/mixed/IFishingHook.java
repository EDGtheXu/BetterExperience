package com.github.edg_thexu.better_experience.mixed;

import net.minecraft.world.phys.Vec3;

public interface IFishingHook {

    Vec3 betterExperience$getPos();

    void betterExperience$setPos(Vec3 pos);

    boolean betterExperience$isSimulation();

    void betterExperience$setSimulation(boolean simulation);

}

package com.github.edg_thexu.better_experience.intergration.terra_entity;

import net.neoforged.fml.ModList;

public class TEHelper {
    static Boolean isLoad;

    public static boolean isLoaded() {
        if(isLoad == null){
            isLoad = ModList.get().isLoaded("terra_entity");
        }
        return isLoad;
    }
}

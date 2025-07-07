package com.github.edg_thexu.better_experience.intergration.curios;

import net.neoforged.fml.ModList;

public class CuriosHelper {
    static Boolean isLoad;

    public static boolean isLoaded() {
        if(isLoad == null){
            isLoad = ModList.get().isLoaded("curios");
        }
        return isLoad;
    }
}

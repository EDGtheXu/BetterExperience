package com.github.edg_thexu.better_experience.intergration.terra_curios;


import net.minecraftforge.fml.ModList;

public class TCHelper {
    static Boolean isLoad;

    public static boolean isLoaded() {
        if(isLoad == null){
            isLoad = ModList.get().isLoaded("terra_curio");
        }
        return isLoad;
    }
}

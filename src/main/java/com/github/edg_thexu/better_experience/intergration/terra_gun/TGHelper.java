package com.github.edg_thexu.better_experience.intergration.terra_gun;


import net.minecraftforge.fml.ModList;

public class TGHelper {
    static Boolean isLoad;

    public static boolean isLoaded() {
        if(isLoad == null){
            isLoad = ModList.get().isLoaded("terra_guns");
        }
        return isLoad;
    }
}

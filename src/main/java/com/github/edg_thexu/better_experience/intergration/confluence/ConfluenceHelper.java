package com.github.edg_thexu.better_experience.intergration.confluence;


import net.minecraftforge.fml.ModList;

public class ConfluenceHelper {
    static Boolean isLoad;

    public static boolean isLoaded() {
        if(isLoad == null){
            isLoad = ModList.get().isLoaded("confluence");
        }
        return isLoad;
    }
}

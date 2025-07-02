package com.github.edg_thexu.better_experience.intergration.confluence_lib;


import net.minecraftforge.fml.ModList;

public class ConfluenceLibHelper {
    static Boolean isLoad;

    public static boolean isLoaded() {
        if(isLoad == null){
            isLoad = ModList.get().isLoaded("confluence_magic_lib");
        }
        return isLoad;
    }
}

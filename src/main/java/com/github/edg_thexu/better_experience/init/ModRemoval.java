package com.github.edg_thexu.better_experience.init;

import com.github.edg_thexu.better_experience.intergration.confluence.ConfluenceHelper;
import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public class ModRemoval {
    public static void processRecipes(Map<ResourceLocation, JsonElement> objectMap) {

        objectMap.entrySet().removeIf((entry) -> {
            // 加载confluence后移除自带的配方
            return ConfluenceHelper.isLoaded() && entry.getKey().getPath().startsWith("replace/by/confluence/");
        });
    }
}

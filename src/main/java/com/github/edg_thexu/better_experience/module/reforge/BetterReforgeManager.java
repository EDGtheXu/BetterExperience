package com.github.edg_thexu.better_experience.module.reforge;

import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BetterReforgeManager {

//    public static int getBetterPrefix(PrefixType prefixType, ItemStack itemStack){
//
//        var available = prefixType.getAvailable();
//        PrefixComponent cp = itemStack.get(ModDataComponentTypes.PREFIX);
//        float originalValue = cp.value();
//        List<Integer> availableList = new ArrayList<>();
//        for(int i = 1; i < available.length; i++){
//            int ii = ModPrefix.ID_MAP.inverse().getOrDefault(available[i], -1);
//            ModPrefix modPrefix = ModPrefix.ID_MAP.get(ii);
//            float value =modPrefix.createComponent(prefixType).value();
//            if(value >= originalValue){
//                availableList.add(ii);
//            }
//        }
//        Collections.shuffle(availableList);
//        return availableList.getFirst();
//    }
}

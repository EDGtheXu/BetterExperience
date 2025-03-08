package com.github.edg_thexu.better_experience.module.autofish;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import org.confluence.mod.common.init.item.AccessoryItems;
import org.confluence.mod.common.item.fishing.BaitItem;
import org.confluence.mod.util.EnchantmentUtil;
import org.confluence.terra_curio.util.TCUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class AutoFishManager {

    /**
     * 计算渔力
     * @param player
     * @param pole
     * @param bait
     * @return
     */
    public static float computeFishingPower(@Nullable Player player, @Nullable ItemStack pole, @Nullable BaitItem bait, @Nullable ItemStack curios){
        float base = 2 + EnchantmentUtil.getEnchantmentLevel(Enchantments.LUCK_OF_THE_SEA, pole); // 0 1 2 3

        // 人物属性
        if (player != null) {
            base += TCUtils.getAccessoriesValue(player, AccessoryItems.FISHING$POWER);
        }

        float multiplier = 1;

        // 鱼竿加成
        if (pole != null) {
            List<AttributeModifier> mul = new ArrayList<>();
            for(var modify : pole.getAttributeModifiers().modifiers()){
                if(modify.attribute() == Attributes.LUCK){
                    if(modify.modifier().operation() == AttributeModifier.Operation.ADD_VALUE)
                        base += (float) modify.modifier().amount();
                    else if(modify.modifier().operation() == AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                        mul.add(modify.modifier());
                }
            }
            for(var modify : mul){
                multiplier += (float) (modify.amount());
            }
        }


        // 鱼饵加成
        float bounds = 1 + (bait == null? 0 : bait.getBaitBonus());
        // 饰品加成
        float curiosBonus = 0;
        if(curios != null) {
            var component = TCUtils.getAccessoriesComponent(curios);
            if( component != null && component.contains(AccessoryItems.FISHING$POWER)) {
                curiosBonus = component.get(AccessoryItems.FISHING$POWER).get();
            }
        }
        return (base )* bounds * multiplier + curiosBonus;
    }

}

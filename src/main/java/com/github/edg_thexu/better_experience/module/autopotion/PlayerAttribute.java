package com.github.edg_thexu.better_experience.module.autopotion;

import com.github.edg_thexu.better_experience.Better_experience;
import com.github.edg_thexu.better_experience.config.ServerConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class PlayerAttribute {

    public static boolean dirty = false;
    /**
     * 给玩家添加额外属性
     * @param player
     */
    public static void applyAdditionalAttributes(ServerPlayer player) {
        // 增加摔落高度
        ResourceLocation location = Better_experience.space("fall_distance_modifier");
        if(player.getAttribute(Attributes.SAFE_FALL_DISTANCE).hasModifier(location)){
            player.getAttribute(Attributes.SAFE_FALL_DISTANCE).removeModifier(location);
        }
        player.getAttribute(Attributes.SAFE_FALL_DISTANCE).addTransientModifier(
                new AttributeModifier(location, ServerConfig.ADDITIONAL_FALL_DISTANCE.get(), AttributeModifier.Operation.ADD_VALUE));
    }

    /**
     * 通知所有玩家更新属性
     * @param server
     */
    public static void notifyDirty(ServerLevel server) {
        if(dirty) {
            dirty = false;
            for (ServerPlayer player : server.players()) {
                applyAdditionalAttributes(player);
            }
        }
    }
}

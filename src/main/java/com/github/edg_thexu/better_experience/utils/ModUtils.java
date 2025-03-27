package com.github.edg_thexu.better_experience.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class ModUtils {


    public static EntityHitResult getEyeTraceHitResult(Player player, double distance){
        AABB aabb = player.getBoundingBox().inflate(distance);
        Vec3 from = player.getEyePosition();
        Vec3 to = player.getEyePosition().add(player.getLookAngle().scale(distance));
        return ProjectileUtil.getEntityHitResult(player.level(), player, from, to, aabb, e-> true, 0.1F);
    }

    public static BlockPos getEyeBlockHitResult(Player player, int maxDistance){
        Vec3 vec3 = player.getEyePosition();
        Vec3 vec31 = vec3.add(player.calculateViewVector(player.getXRot(), player.getYRot()).scale(maxDistance));

        final BlockHitResult result  = player.level().clip(new ClipContext(vec3, vec31, net.minecraft.world.level.ClipContext.Block.OUTLINE, ClipContext.Fluid.SOURCE_ONLY, player));
        final BlockHitResult raytraceResult = result.withPosition(result.getBlockPos());
        final BlockPos pos = raytraceResult.getBlockPos();
        return pos;
    }

    /**
     * 尝试将新物品堆叠到已有的相同物品槽中
     * @param item 新物品
     * @param items 已有物品槽
     */
    public static boolean tryAddItemStackToItemStacks(ItemStack item, List<ItemStack> items){
        // 尝试将新物品堆叠到已有的相同物品槽中
        for (ItemStack current : items) {
            if (!current.isEmpty() && current.is(item.getItem()) && current.getCount() < current.getMaxStackSize()) {
                // 计算可以堆叠的数量
                int transfer = Math.min(item.getCount(), current.getMaxStackSize() - current.getCount());
                current.grow(transfer); // 增加当前物品的数量
                item.shrink(transfer);  // 减少新物品的数量
                if (item.isEmpty()) {
                    return true; // 如果新物品被完全堆叠，返回true
                }
            }
        }
        return false; // 如果没有找到可以堆叠的物品槽，返回false
    }

    /**
     * 合并堆叠物品
     * @param items 物品列表
     */
    public static void unionItemStacks(List<ItemStack> items){
        for (int i = 0; i < items.size(); i++) {
            ItemStack current = items.get(i);
            if (current.isEmpty()) {
                continue; // 跳过空槽
            }
            for (int j = i + 1; j < items.size(); j++) {
                ItemStack next = items.get(j);
                if (!next.isEmpty() && current.is(next.getItem()) && current.getCount() < current.getMaxStackSize()) {
                    // 合并堆叠
                    int transfer = Math.min(next.getCount(), current.getMaxStackSize() - current.getCount());
                    current.grow(transfer); // 增加当前物品的数量
                    next.shrink(transfer);  // 减少下一个物品的数量
                    if (next.isEmpty()) {
                        items.set(j, ItemStack.EMPTY); // 如果下一个物品被完全合并，设置为空
                    }
                }
            }
        }
    }
}

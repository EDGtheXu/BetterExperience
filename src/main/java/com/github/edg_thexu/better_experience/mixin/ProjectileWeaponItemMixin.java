package com.github.edg_thexu.better_experience.mixin;

import com.github.edg_thexu.better_experience.config.CommonConfig;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = ProjectileWeaponItem.class, priority = 999)
public class ProjectileWeaponItemMixin {

//    @Redirect(method = "useAmmo", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;processAmmoUse(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;I)I"))
//    private static int injected(ServerLevel level, ItemStack weapon, ItemStack ammo, int count) {
//        if(CommonConfig.INFINITE_AMMO.get() &&  ammo.getCount() > CommonConfig.INFINITE_AMMO_STACK_SIZE.get()){
//            return 0;
//        }
//        return EnchantmentHelper.processAmmoUse(level, weapon, ammo, count);
//    }

    @WrapOperation(method = "useAmmo", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;processAmmoUse(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;I)I"))
    private static int injected(ServerLevel level, ItemStack weapon, ItemStack ammo, int count, Operation<Integer> original) {
        if(CommonConfig.INFINITE_AMMO.get() &&  ammo.getCount() > CommonConfig.INFINITE_AMMO_STACK_SIZE.get()){
            return 0;
        }
        return original.call(level, weapon, ammo, count);
    }
}

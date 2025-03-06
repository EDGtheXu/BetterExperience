package com.github.edg_thexu.better_experience.mixin;

import com.github.edg_thexu.better_experience.config.ServerConfig;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ProjectileWeaponItem.class)
public class ProjectileWeaponItemMixin {

    @Redirect(method = "useAmmo", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;processAmmoUse(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;I)I"))
    private static int injected(ServerLevel level, ItemStack weapon, ItemStack ammo, int count) {
        if(ServerConfig.INFINITE_AMMO.get() &&  ammo.getCount() > ServerConfig.INFINITE_AMMO_STACK_SIZE.get()){
            return 0;
        }
        return EnchantmentHelper.processAmmoUse(level, weapon, ammo, count);
    }
}

package com.github.edg_thexu.better_experience.mixin.integration.confluence;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.confluence.mod.common.item.fishing.AbstractFishingPole;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Invoker;
@Pseudo
@Mixin(targets = "org.confluence.mod.common.item.fishing.AbstractFishingPole",remap = false)
//@Mixin(value = AbstractFishingPole.class,remap = false)
public interface AbstractFishingPoleAccessor {
    @Invoker
    FishingHook callGetHook(ItemStack var1, Player var2, Level var3, int var4, int var5);
}

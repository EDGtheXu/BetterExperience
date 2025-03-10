package com.github.edg_thexu.better_experience.mixin;

import com.github.edg_thexu.better_experience.mixed.IFishingHook;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.confluence.terraentity.mixinauxiliary.SelfGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.List;

@Mixin(FishingHook.class)
public class FishingHookMixin implements IFishingHook, SelfGetter<FishingHook> {

    // 是否是钓鱼机
    @Unique
    boolean betterExperience$isSimulation = false;

    // 钓鱼机的位置
    @Unique
    Vec3 betterExperience$pos = Vec3.ZERO;

    // 钓的物品
    @Unique
    List<ItemStack> betterExperience$items = List.of();

    @Override
    public Vec3 betterExperience$getPos() {
        return betterExperience$pos;
    }

    @Override
    public void betterExperience$setPos(Vec3 pos) {
        betterExperience$pos = pos;
    }

    @Override
    public boolean betterExperience$isSimulation() {
        return betterExperience$isSimulation;
    }

    @Override
    public void betterExperience$setSimulation(boolean simulation) {
        betterExperience$isSimulation = simulation;
    }

    @Override
    public List<ItemStack> betterExperience$getItems() {
        return betterExperience$items;
    }

    @Override
    public void betterExperience$setItems(List<ItemStack> items) {
        betterExperience$items = items;
    }

    @Inject(method = "retrieve", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z", ordinal = 0))
    private void onRetrieveMixin(ItemStack stack, CallbackInfoReturnable<Integer> cir, @Local ItemEntity itementity) {
        if(betterExperience$isSimulation){
            double d0 = betterExperience$pos.x - te$getSelf().getX();
            double d1 = betterExperience$pos.y - te$getSelf().getY();
            double d2 = betterExperience$pos.z - te$getSelf().getZ();
            double d3 = 0.1;

            itementity.setDeltaMovement(d0 * 0.1, d1 * 0.1 + Math.sqrt(Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2)) * 0.08, d2 * 0.1);
        }
    }


    @Redirect(method = "retrieve", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z", ordinal = 1))
    private boolean onRetrieve(Level instance, Entity entity, @Local Player player) {
        if(!betterExperience$isSimulation){
            instance.addFreshEntity(new ExperienceOrb(player.level(), player.getX(), player.getY() + 0.5, player.getZ() + 0.5, te$getSelf().getRandom().nextInt(6) + 1));
        }
        return true;
    }

    // 模拟钓鱼可通过
    @Inject(method = "shouldStopFishing", at = @At(value = "HEAD"), cancellable = true)
    private void onShouldStopFishingMixin(CallbackInfoReturnable<Boolean> cir) {
        if(betterExperience$isSimulation){
            cir.setReturnValue(false);
        }
    }

    // 取消更新Player.fishing
    @Inject(method = "updateOwnerInfo", at = @At(value = "HEAD"),cancellable = true)
    private void updateOwnerInfoMixin(FishingHook fishingHook, CallbackInfo ci) {
        if(betterExperience$isSimulation){
            ci.cancel();
        }
    }

}
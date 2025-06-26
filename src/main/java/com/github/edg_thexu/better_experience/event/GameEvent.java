package com.github.edg_thexu.better_experience.event;

import com.github.edg_thexu.better_experience.Better_experience;
import com.github.edg_thexu.better_experience.intergration.confluence.ConfluenceHelper;
import com.github.edg_thexu.better_experience.mixed.IFishingHook;
import com.github.edg_thexu.better_experience.module.boomstaff.ExplodeManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent;
import net.neoforged.neoforge.event.entity.player.ItemFishedEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import org.confluence.mod.common.init.ModEffects;
import org.confluence.mod.common.init.ModLootTables;

import java.util.List;

@EventBusSubscriber(modid = Better_experience.MODID, bus = EventBusSubscriber.Bus.GAME)
public class GameEvent {

    @SubscribeEvent
    public static void entitySpawn(FinalizeSpawnEvent event){
    }

    @SubscribeEvent
    public static void serverTick(ServerTickEvent.Post event){
        ExplodeManager.getInstance().tickHandle();

    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void itemFished(ItemFishedEvent event){

        FishingHook hook = event.getHookEntity();
        IFishingHook ihook = (IFishingHook)hook;
        Level level = event.getHookEntity().level();
        if(ihook.betterExperience$isSimulation() && level instanceof ServerLevel serverLevel){
            List<ItemStack> items = event.getDrops();
            if(ConfluenceHelper.isLoaded()) {
                // 宝匣掉率增加
                int luck = hook.luck;

                Player player = hook.getPlayerOwner();
                float chance = player != null && player.hasEffect(ModEffects.CRATE) ? 0.25f : 0.1f;
                chance += luck / 30f;

                if (level.random.nextFloat() < chance) {
                    items = serverLevel.getServer().reloadableRegistries().getLootTable(ModLootTables.CRATE)
                            .getRandomItems(new LootParams.Builder(serverLevel)
                                    .withParameter(LootContextParams.ORIGIN, hook.position())
                                    .withParameter(LootContextParams.THIS_ENTITY, hook)
                                    .create(LootContextParamSets.GIFT));
                }
            }
            // 取消原版收回物品
            ihook.betterExperience$setItems(items);
            event.setCanceled(true);
        }

    }

//    @SubscribeEvent
//    public static void consumeBullet(GunEvent.ShrinkBulletEvent event) {
//        Player player = event.getPlayer();
//        Inventory inventory = player.getInventory();
//        NonNullList<ItemStack> stackNonNullList = inventory.items;
//        List<ItemStack> copyList = new ArrayList<>(stackNonNullList);
//
//        GunEvent.InventoryExtraEvent inventoryExtraEvent = new GunEvent.InventoryExtraEvent(player, (BaseGun) event.getGunStack().getItem(), copyList);
//        NeoForge.EVENT_BUS.post(inventoryExtraEvent);
//
//        for (ItemStack item : inventoryExtraEvent.getAmmoList()) {
//            if (item == null || item.is(Items.AIR)) continue;
//            if (item.is(TGTags.AMMO) && isCompatible(player, item, event.getGunStack())) {
//                if(item.getCount() >= CommonConfig.INFINITE_AMMO_STACK_SIZE.get()){
//                    event.setCanceled(true);
//                    return;
//                }
//            }
//        }

//        if(event.getBulletStack().getCount() >= CommonConfig.INFINITE_AMMO_STACK_SIZE.get()){
//            event.setCanceled(true);
//        }
//    }
}

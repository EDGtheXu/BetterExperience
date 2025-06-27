package com.github.edg_thexu.better_experience.intergration.terra_gun;

import com.github.edg_thexu.better_experience.config.CommonConfig;
import net.neoforged.bus.api.SubscribeEvent;
import org.confluence.terra_guns.api.event.GunEvent;

public class TerraGunEvents {

    @SubscribeEvent
    public void consumeBullet(GunEvent.ShrinkBulletEvent event) {
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

        if(event.getBulletStack().getCount() >= CommonConfig.INFINITE_AMMO_STACK_SIZE.get()){
            event.setCanceled(true);
        }
    }

}

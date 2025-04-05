package com.github.edg_thexu.better_experience.attachment;

import com.github.edg_thexu.better_experience.module.autopotion.PlayerInventoryManager;
import com.github.edg_thexu.better_experience.networks.s2c.EnderChestItemsS2C;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.UnknownNullability;

import java.util.ArrayList;
import java.util.List;

public class EnderChestAttachment implements INBTSerializable<CompoundTag> {

    List<Item> items = new ArrayList<>();

    public void refresh(EnderChestAttachment attachment){
        items = attachment.items;
    }
    public List<Item> getItems() {
        return items;
    }


    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        for (int i = 0; i < items.size(); i++) {
            tag.putString("item" + i, BuiltInRegistries.ITEM.getKey(items.get(i)).toString());
        }
        return tag;
    }


    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        for (int i = 0; i < tag.size(); i++) {
            String key = tag.getString("item" + i);
            if (key.isEmpty()) continue;
            items.add(BuiltInRegistries.ITEM.get(ResourceLocation.parse(key)));
        }
    }

    public static void sync(ServerPlayer player){
        EnderChestAttachment att = new EnderChestAttachment();
        var items = player.getEnderChestInventory().getItems();
        List<Item> itemList = new ArrayList<>();
        for (ItemStack stack : items) {
            if(PlayerInventoryManager.canApply.test(stack)){
                itemList.add(stack.getItem());
            }
        }
        att.items = itemList;
        PacketDistributor.sendToPlayer(player, new EnderChestItemsS2C(att));
    }
}

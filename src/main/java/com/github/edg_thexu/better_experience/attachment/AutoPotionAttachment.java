package com.github.edg_thexu.better_experience.attachment;

import com.github.edg_thexu.better_experience.networks.c2s.PotionApplyPacketC2S;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.UnknownNullability;

import java.util.*;

public class AutoPotionAttachment implements INBTSerializable<CompoundTag> {


    Map<Holder<MobEffect>, Integer> potions = new HashMap<>();
    Map<Holder<MobEffect>, Integer> _potions = new HashMap<>();
    // client-side only
    HashSet<Holder<MobEffect>> forbiddens = new HashSet<>();
    boolean dirty = false;

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        List<String> list = potions.keySet().stream().map(integer -> BuiltInRegistries.MOB_EFFECT.getKey(integer.value()).toString()).toList();
        List<Integer> amps = potions.values().stream().toList();
        int size = list.size();
        tag.putInt("size", size);
        for (int i = 0; i < size; i++) {
            tag.putString("potion" + i, list.get(i));
            tag.putInt("amp" + i, amps.get(i));
        }
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        int size = tag.getInt("size");
        potions = new HashMap<>();
        for (int i = 0; i < size; i++) {
            String potion = tag.getString("potion" + i);
            int amp = tag.getInt("amp" + i);
            ResourceLocation location = ResourceLocation.tryParse(potion);
            if (location == null) {
                continue;
            }
            MobEffect effect = BuiltInRegistries.MOB_EFFECT.get(location);
            if (effect == null) {
                continue;
            }
            potions.put(BuiltInRegistries.MOB_EFFECT.wrapAsHolder(effect), amp);
        }
    }

    public void removePotion(Holder<MobEffect> potion) {
        if(potions.remove(potion)!= null) {
            dirty = true;
        }
    }

    public void addForbidden(Holder<MobEffect> potion) {
        if(forbiddens.add(potion)){
            dirty = true;
        }
    }

    public void removeForbidden(Holder<MobEffect> potion) {
        if(forbiddens.remove(potion)){
            dirty = true;
        }
    }

    public boolean isForbidden(Holder<MobEffect> potion) {
        return forbiddens.contains(potion);
    }

    public void addPotion(Holder<MobEffect> potion, int amp) {
        if(potions.containsKey(potion)) {
            if(potions.get(potion) > amp){
                potions.put(potion, amp);
                dirty = true;
            }
        }else{
            potions.put(potion, amp);
            dirty = true;
        }
    }

    public Map<Holder<MobEffect>, Integer> getPotions() {
        return potions;
    }

    public void sync(){
        sync(false);
    }

    public void sync(boolean force){
        // 防止地址相等时数据始终一致
        if(force ||  !_potions.equals(potions) || _potions == potions) {
            if(_potions != potions)
                _potions.clear();
            _potions = new HashMap<>(potions);

            AutoPotionAttachment attachment = new AutoPotionAttachment();

            for(var potion : potions.entrySet()){
                if(!forbiddens.contains(potion.getKey()))
                    attachment.potions.put(potion.getKey(), potion.getValue());
            }
            PacketDistributor.sendToServer(new PotionApplyPacketC2S(attachment));
            dirty = false;
        }
    }

}

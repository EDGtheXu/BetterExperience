package com.github.edg_thexu.better_experience.attachment;

import com.github.edg_thexu.better_experience.networks.c2s.PotionApplyPacketC2S;
import com.github.edg_thexu.better_experience.utils.ModUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;

import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.UnknownNullability;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class AutoPotionAttachment implements INBTSerializable<CompoundTag> {


    Map<MobEffect, Integer> potions = new HashMap<>();
    Map<MobEffect, Integer> _potions = new HashMap<>();
    // client-side only
    HashSet<MobEffect> forbiddens = new HashSet<>();
    boolean dirty = false;

    public void setAttachment(AutoPotionAttachment attachment){
        potions = attachment.potions;
        forbiddens = attachment.forbiddens;
        _potions = attachment._potions;
        dirty = attachment.dirty;
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        List<String> list = potions.keySet().stream().map(integer -> ForgeRegistries.MOB_EFFECTS.getKey(integer).toString()).toList();
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
    public void deserializeNBT(CompoundTag tag) {
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
            potions.put(effect, amp);
        }
    }

    public void removePotion(MobEffect potion) {
        if(potions.remove(potion)!= null) {
            dirty = true;
        }
    }

    public void addForbidden(MobEffect potion) {
        if(forbiddens.add(potion)){
            dirty = true;
        }
    }

    public void removeForbidden(MobEffect potion) {
        if(forbiddens.remove(potion)){
            dirty = true;
        }
    }

    public boolean isForbidden(MobEffect potion) {
        return forbiddens.contains(potion);
    }

    public void addPotion(MobEffect potion, int amp) {
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

    public Map<MobEffect, Integer> getPotions() {
        return potions;
    }

    public void sync(){
        sync(false);
    }

    public void sync(boolean force){
        // 防止地址相等时数据始终一致
        if(force || dirty ||  !_potions.equals(potions) || _potions == potions) {
            if(_potions != potions)
                _potions.clear();
            _potions = new HashMap<>(potions);

            AutoPotionAttachment attachment = new AutoPotionAttachment();

            for(var potion : potions.entrySet()){
                if(!forbiddens.contains(potion.getKey()))
                    attachment.potions.put(potion.getKey(), potion.getValue());
            }
            ModUtils.sendToServer(new PotionApplyPacketC2S(attachment));
//            PacketDistributor.sendToServer(new PotionApplyPacketC2S(attachment));
            dirty = false;
        }
    }

}

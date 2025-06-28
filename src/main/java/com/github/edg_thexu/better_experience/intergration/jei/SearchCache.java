package com.github.edg_thexu.better_experience.intergration.jei;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.apache.commons.lang3.mutable.MutableInt;
import oshi.util.tuples.Pair;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchCache {
    Map<Item, ContainerItem> itemMap;
    ContainerItem markedMap;

    public SearchCache() {
        this.itemMap = new HashMap<>();
        this.markedMap = new ContainerItem();
    }

    public void add(Item item, BlockPos pos, int slot){
        if(!itemMap.containsKey(item)){
            ContainerItem containerItem = new ContainerItem();
            containerItem.add(pos, slot);
            itemMap.put(item, containerItem);
        }else{
            itemMap.get(item).add(pos, slot);
        }
    }

    public ContainerItem get(Item item){

        return itemMap.get(item);
    }

    public @Nullable List<Pair<Integer, MutableInt>> getSlots(Item item, BlockPos pos){
        if(itemMap.containsKey(item)){
            return itemMap.get(item).getSlots(pos);
        }
        return null;
    }

    public void clear(){
        itemMap.clear();
    }

    public void remove(Item item, BlockPos pos){
        if(itemMap.containsKey(item)){
            itemMap.get(item).remove(pos);
        }
    }

    public List<ItemStack> checkItemInBlock(Level level, Item item, BlockPos pos){
        BlockEntity blockEntity = level.getBlockEntity(pos);
        List<ItemStack> stacks = new ArrayList<>();
        if(blockEntity instanceof Container container){
            ContainerItem containerItem = itemMap.get(item);
            if(containerItem!= null && containerItem.contains(pos)){
                List<Pair<Integer, MutableInt>> slots = containerItem.getSlots(pos);

                for(Pair<Integer, MutableInt> slot_p : slots){
                    int slot = slot_p.getA();
                    ItemStack stack = container.getItem(slot);
                    if(stack.is(item)){
                        stacks.add(stack);
                    }else{
                        slots.remove(slot);
                    }
                }
                if(slots.isEmpty()){
                    containerItem.remove(pos);
                }
                return stacks;
            }
        }
        return stacks;
    }

    public void mark(Item item, BlockPos pos, int slot, int count){
        if(itemMap.containsKey(item)){
            markedMap.add(pos, slot, count);
        }
    }

    public void checkBlock(Level level, BlockPos pos){
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if(blockEntity instanceof Container container){
            for(int i = 0; i < container.getContainerSize(); i++){
                ItemStack stack = container.getItem(i);
                if(!stack.isEmpty()) {
                    Item item = stack.getItem();
                    this.add(item, pos, i);
                }
            }
        }
    }


    public static class ContainerItem {
        public Map<BlockPos, List<Pair<Integer, MutableInt>>> map;

        ContainerItem(){
            this.map = new HashMap<>();
        }

        boolean contains(BlockPos pos){
            return map.containsKey(pos);
        }

        List<Pair<Integer, MutableInt>> getSlots(BlockPos pos){
            return map.get(pos);
        }

        void remove(BlockPos pos){
            map.remove(pos);
        }

        void add(BlockPos pos, int slot){
            if(!map.containsKey(pos)){
                map.put(pos, new ArrayList<>());
            }
            map.get(pos).add(new Pair<>(slot, new MutableInt(0)));
        }

        void add(BlockPos pos, int slot, int count){
            if(!map.containsKey(pos)){
                map.put(pos, new ArrayList<>());
            }
            map.get(pos).stream().filter(p -> p.getA() == slot).findFirst().ifPresentOrElse(p -> p.getB().add(count),
                    ()-> map.get(pos).add(new Pair<>(slot, new MutableInt(count))));
        }

    }
}


package com.github.edg_thexu.better_experience.registries.recipehandler.visitor;

import com.github.edg_thexu.better_experience.intergration.jei.SearchCache;
import com.github.edg_thexu.better_experience.registries.recipehandler.IRecipeHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.apache.commons.lang3.mutable.MutableInt;
import oshi.util.tuples.Pair;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 原料类型的访问者
 */
public abstract class AbstractVisitor<T> implements IRecipeHandlerVisitor<T> {

    @Override
    public boolean visit(Level level, SearchCache searchCache, T ing, IRecipeHandler<T> handler) {
        if(isEmpty(ing)){
            return true;
        }
        Set<Item> items = new HashSet<>();
        ItemStack[] stacks = getStacks(ing);
        int count = handler.count();

        boolean flag1 = false;
        int c = -1;
        for(ItemStack stack : stacks){
            c++;
            if(stack.isEmpty()){
                continue;
            }
            Item item = stack.getItem();
            if(items.contains(item)){
                continue;
            }
            int need = stack.getCount() * count;

            SearchCache.ContainerItem containerItem = searchCache.get(item);
            if(containerItem == null){
                continue;
            }
            for(Map.Entry<BlockPos, List<Pair<Integer, MutableInt>>> pos : containerItem.map.entrySet()){
                BlockPos blockPos = pos.getKey();
                List<Pair<Integer, MutableInt>> slots = pos.getValue();
                BlockEntity blockEntity = level.getBlockEntity(blockPos);
                if(blockEntity instanceof Container container){
                    for(Pair<Integer, MutableInt> slot : slots){
                        int index = slot.getA();
                        ItemStack have = container.getItem(index);
                        if(!match(have, ing, c)){
                            continue;
                        }

                        int haveCount = have.getCount();
                        MutableInt occupied = slot.getB();
                        int addition = haveCount - occupied.getValue();
                        if(addition >= need) {
                            occupied.add(need);
                            searchCache.mark(item, blockPos, index, need);
                            need = 0;
                            items.add(item);
                            break;
                        }
                        need -= addition;
                        occupied.add(addition);
                        searchCache.mark(item, blockPos, index, addition);
                    }
                }
            }
            if(need <= 0){
                flag1 = true;
                break;
            }

        }
        if(!flag1){
            searchCache.addRequiredCount(Ingredient.of(stacks), count);
            return false;
        }
        return true;
    }


    abstract boolean match(ItemStack stack, T ing, int index);

    abstract ItemStack[] getStacks(T ing);

    abstract boolean isEmpty(T ing);
}

package com.github.edg_thexu.better_experience.init;

import com.github.edg_thexu.better_experience.Better_experience;
import com.github.edg_thexu.better_experience.block.AutoFishBlock;
import com.github.edg_thexu.better_experience.block.AutoSellBlock;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;


public class ModBlocks {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Better_experience.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Better_experience.MODID);;

    public static final DeferredBlock<AutoFishBlock> AUTO_FISH_BLOCK = registerSingleBlock("auto_fish_block", ()-> new AutoFishBlock(Block.Properties.ofFullCopy(Blocks.GOLD_BLOCK).lightLevel(state->state.getValue(AutoFishBlock.TURN)? 10 : 0)));
    public static final Supplier<BlockEntityType<AutoFishBlock.AutoFishMachineEntity>> AUTO_FISH_BLOCK_ENTITY = BLOCK_ENTITIES.register("auto_fish_block_entity", () -> BlockEntityType.Builder.of(AutoFishBlock.AutoFishMachineEntity::new, AUTO_FISH_BLOCK.get()).build(null));
    public static final DeferredItem<BlockItem> AUTO_FISH_BLOCK_ITEM = ModItems.ITEMS.register("auto_fish_block", () -> new AutoFishBlock.Item(AUTO_FISH_BLOCK.get()));

    public static final DeferredBlock<AutoSellBlock> AUTO_SELL_BLOCK = registerWithItem("auto_sell_block", ()-> new AutoSellBlock(Block.Properties.ofFullCopy(Blocks.STONE)));
    public static final Supplier<BlockEntityType<AutoSellBlock.AutoSellBlockEntity>> AUTO_SELL_BLOCK_ENTITY = BLOCK_ENTITIES.register("auto_sell_block_entity", () -> BlockEntityType.Builder.of(AutoSellBlock.AutoSellBlockEntity::new, AUTO_SELL_BLOCK.get()).build(null));

    private static <B extends Block> DeferredBlock<B> registerWithItem(String id, Supplier<B> block) {
        DeferredBlock<B> object = BLOCKS.register(id, block);
        ModItems.ITEMS.registerSimpleBlockItem(object);
        return object;
    }


    private static <B extends Block> DeferredBlock<B> registerSingleBlock(String id, Supplier<B> block) {
        DeferredBlock<B> object = BLOCKS.register(id, block);
        return object;
    }

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
        BLOCK_ENTITIES.register(bus);
    }

}

package com.github.edg_thexu.better_experience.init;

import com.github.edg_thexu.better_experience.Better_experience;
import com.github.edg_thexu.better_experience.block.AutoFishBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static org.confluence.mod.common.init.block.ModBlocks.BLOCK_ENTITIES;

public class ModBlocks {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Better_experience.MODID);

    public static final DeferredBlock<AutoFishBlock> AUTO_FISH_BLOCK = registerWithItem("auto_fish_block", ()-> new AutoFishBlock(Block.Properties.ofFullCopy(Blocks.CHEST)));
    public static final Supplier<BlockEntityType<AutoFishBlock.AutoFishMachineEntity>> AUTO_FISH_BLOCK_ENTITY = BLOCK_ENTITIES.register("auto_fish_block_entity", () -> BlockEntityType.Builder.of(AutoFishBlock.AutoFishMachineEntity::new, AUTO_FISH_BLOCK.get()).build(null));


    private static <B extends Block> DeferredBlock<B> registerWithItem(String id, Supplier<B> block) {
        DeferredBlock<B> object = BLOCKS.register(id, block);
        ModItems.ITEMS.registerSimpleBlockItem(object);
        return object;
    }

}
